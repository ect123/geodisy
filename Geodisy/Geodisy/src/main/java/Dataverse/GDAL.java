package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;
import Dataverse.DataverseJSONFieldClasses.Fields.DataverseJSONGeoFieldClasses.GeographicBoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;

import java.io.*;
import java.util.LinkedList;

import static BaseFiles.GeodisyStrings.*;
import static Dataverse.DVFieldNameStrings.*;

public class GDAL {
    GeoLogger logger = new GeoLogger(this.getClass());

    public String getGDALInfo(String filePath, String name) throws IOException {
        String gdal;
        StringBuilder gdalString = new StringBuilder();

        Process process;
        if(GeodisyStrings.gdalinfoRasterExtention(name)) {
            gdal = GDALINFO;
        }
        else if(GeodisyStrings.ogrinfoVectorExtension(name) && !GeodisyStrings.otherShapeFilesExtensions(name)) {
            gdal = OGRINFO;
        }else
            return "FAILURE";

        ProcessBuilder processBuilder= new ProcessBuilder();
        processBuilder.command("/usr/bin/bash", "-c", gdal+filePath);
        //System.out.println(gdal+filePath);
        int counter = 0;
        if (IS_WINDOWS) {
            process = Runtime.getRuntime()
                    .exec(String.format(gdal + filePath));
        } else {
            process = processBuilder.start();
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(process.getInputStream()));

        String s;
        while ((s = stdInput.readLine()) != null) {
            gdalString.append(s);
        }

        /*if(!name.endsWith(".csv")&& gdalString.toString().contains("FAILURE"))
            System.out.println(gdalString.toString());*/
        return gdalString.toString();
    }
    //Not used by main program
    public DataverseJavaObject generateBB(DataverseJavaObject djo) {
        String doi = djo.getDOI();
        String path = doi.replace(".","/");
        String folderName = DATASET_FILES_PATH +path+"/";
        LinkedList<DataverseGeoRecordFile> origRecords = djo.getGeoDataFiles();
        if(origRecords.size()==0)
            return djo;
        File folder = new File(folderName);
        if(!folder.exists())
            folder.mkdirs();


        if(folder.listFiles().length==0) {
            folder.delete();
            return djo;
        }

        int raster = 1;
        int vector = 1;
        LinkedList<DataverseGeoRecordFile> records = new LinkedList<>();
        GeographicBoundingBox temp = new GeographicBoundingBox(doi);
        for(DataverseGeoRecordFile drf : origRecords) {
            String name = drf.getTranslatedTitle();
            String filePath = DATASET_FILES_PATH + path + "/" + name;
            File file = new File(filePath);

            if (name.endsWith("tif")) {
                temp = generateBB(file, doi, String.valueOf(raster));
                if(temp.hasBB())
                    raster++;
                else
                    continue;
            } else if (name.endsWith("shp")) {
                temp = generateBB(file, doi, String.valueOf(vector));
                if(temp.hasBB())
                    vector++;
                else
                    continue;
            } else {
                logger.error("Somehow got a DataverseGeoRecordFile that isn't for a shp or tif. File name" + name);
            }
            drf.setGbb(temp);
            records.add(drf);
        }
        djo.setGeoDataFiles(records);
        return djo;
    }

    public GeographicBoundingBox generateBB(File file, String doi, String number){
        String lowerName = file.getName().toLowerCase();
        String regularName = file.getName();
        String filePath = file.getPath();
        if(!GeodisyStrings.gdalinfoRasterExtention(lowerName) && !GeodisyStrings.ogrinfoVectorExtension(lowerName))
            return new GeographicBoundingBox(doi);
        if(GeodisyStrings.otherShapeFilesExtensions(lowerName))
            return new GeographicBoundingBox(doi);
        boolean gdalInfo = GeodisyStrings.gdalinfoRasterExtention(lowerName);

        String gdalString;
        GeographicBoundingBox temp;
        String projection =  "";
        try {
            gdalString = getGDALInfo(filePath, regularName);
            if(gdalString.contains("FAILURE")) {
                logger.warn("Something went wrong parsing " + regularName + " at " + filePath);
                return new GeographicBoundingBox(doi);
            }
            if(gdalInfo) {
                temp = getRaster(gdalString);
                projection = getProjection(gdalString);
            }
            else {
                temp = getVector(gdalString, IS_WINDOWS, regularName, filePath);
                projection = temp.getField(PROJECTION);
            }
            temp.setIsGeneratedFromGeoFile(true);

            if(temp.hasBB()) {
                GeographicBoundingBox gbb = new GeographicBoundingBox(doi);
                gbb.setIsGeneratedFromGeoFile(temp.isGeneratedFromGeoFile());
                gbb.setField(FILE_NAME,lowerName);
                gbb.setField(GEOMETRY,temp.getField(GEOMETRY));
                gbb.setField(PROJECTION,projection);
                gbb.setBB(temp.getBB());
                if(lowerName.endsWith(".shp")) {
                    gbb.setField(GEOSERVER_LABEL, doi);
                    gbb.setFileNumber(Integer.valueOf(number));
                    return gbb;
                }
                else if(lowerName.endsWith(".tif")) {
                    gbb.setField(GEOSERVER_LABEL, doi);
                    gbb.setFileNumber(Integer.valueOf(number));
                    return gbb;
                }
                else {
                    logger.error("Somehow got a bounding box, but isn't a shp or tif with file " + filePath + " and persistantID=" + doi);
                    return new GeographicBoundingBox(doi);
                }

            }
        } catch (IOException e) {
            logger.error("Something went wrong trying to call GDAL with " + lowerName);
        }
        return new GeographicBoundingBox(doi);
    }

    private String getProjection(String gdalString) {
        String projection = "";
        int start = gdalString.indexOf("PROJCS[\"");
        if(start == -1)
            start = gdalString.indexOf("GEOGCS[\"");
        if(start == -1)
            return projection;
        String sub = gdalString.substring(start+8);
        int end = sub.indexOf("\"");
        projection = sub.substring(0,end);
        return projection;
    }

    public GeographicBoundingBox generateBoundingBoxFromCSV(String fileName, DataverseJavaObject djo){
        String path = djo.getDOI().replace("/","_");
        path = path.replace(".","_");
        String filePath = DATASET_FILES_PATH + path + "/" + fileName;
        String name = fileName;
        String ogrString = null;
        try {
            ogrString = getGDALInfo(filePath, name);
            if(ogrString.contains("FAILURE")) {
                logger.warn("Something went wrong parsing " + name + " at " + filePath);
                return new GeographicBoundingBox(djo.getDOI());
            }
        GeographicBoundingBox temp = getVector(ogrString, IS_WINDOWS, name, filePath);
        temp.setIsGeneratedFromGeoFile(true);
        return temp;
        } catch (IOException e) {
            logger.error("Something went wrong trying to check " + name + " from record " + djo.getDOI());
        }
        return new GeographicBoundingBox("junk");
    }

    private GeographicBoundingBox getRaster(String gdalString) {

        GeographicBoundingBox temp = new GeographicBoundingBox("temp");
            temp.setBB(getLatLongGdalInfo(gdalString));
            temp.setField(GEOMETRY,RASTER);
            temp.setWidthHeight(gdalString);
        return temp;
    }

    private GeographicBoundingBox getVector(String gdalString, boolean isWindows, String name, String filePath) throws IOException {
        String geo = getGeometryType(gdalString);
        GeographicBoundingBox gbb = new GeographicBoundingBox("temp");
        BoundingBox temp;
        temp = getLatLongOgrInfo(gdalString);
        if (temp.hasUTMCoords()) {
            convertToWGS84(filePath, isWindows, name);
            gbb.setField(PROJECTION,"EPSG:4326");
            gdalString = getGDALInfo(filePath, name);
            if(gdalString.contains("FAILURE"))
                return new GeographicBoundingBox("temp");
            temp = getLatLongOgrInfo(gdalString);
        }
        else{
            try {
                    String authority = getAuthority(gdalString);
                    gbb.setField(PROJECTION, authority);
            }catch (IndexOutOfBoundsException e){
                logger.error("Couldn't determine projection for record " + name);
            }
        }
        if(gdalString.contains("Geometry:")){
            int start = gdalString.indexOf("Geometry:")+10;
            int end = gdalString.indexOf("Feature Count:");
            gbb.setField(GEOMETRY,gdalString.substring(start,end));

        }
        if(temp.hasBoundingBox())
            gbb.setBB(temp);
        return gbb;
    }

    private String getAuthority(String gdalString) throws IndexOutOfBoundsException {

        int projLoc = gdalString.lastIndexOf("AUTHORITY[\"") + 11;
        String projectionString = gdalString.substring(projLoc);
        int projLocEnd = projectionString.indexOf("\"");
        String first = projectionString.substring(0, projLocEnd);
        projLoc = projectionString.indexOf("\",\"") + 3;
        projectionString = projectionString.substring(projLoc);
        projLocEnd = projectionString.indexOf("\"]]");
        String second = projectionString.substring(0, projLocEnd);
        return first + ":" + second;
    }

    private void convertToWGS84(String filePath, boolean isWindows, String name) throws IOException {
        GDALTranslate gdalTranslate = new GDALTranslate();
        String path = new File(filePath).getPath();
        String stub;
        if(GeodisyStrings.ogrinfoVectorExtension(name))
            stub = gdalTranslate.vectorTransform(path,name);
        else
            stub = gdalTranslate.rasterTransform(path,name);
        if(!path.endsWith(stub))
            path = path.substring(0,path.lastIndexOf(GeodisyStrings.replaceSlashes("/"))+1) + stub;
        File check = new File(path);
        if(!check.exists())
            logger.warn("Couldn't convert " + name +" to  WGS84");
    }

    private BoundingBox compare(BoundingBox temp, BoundingBox fullExtent) {
        if(fullExtent.getLongWest()==361||temp.getLongWest()<fullExtent.getLongWest()||(fullExtent.getLongWest()<0 && temp.getLongWest()>=0 && temp.getLongEast()< temp.getLongWest()))
            fullExtent.setLongWest(temp.getLongWest());
        if(fullExtent.getLongEast()==361 || temp.getLongEast()>fullExtent.getLongEast() ||(fullExtent.getLongEast()>=0 && temp.getLongEast()<0 && temp.getLongWest()>temp.getLongEast()))
            fullExtent.setLongEast(temp.getLongEast());
        if(fullExtent.getLatNorth()==361 || temp.getLatNorth()>fullExtent.getLatNorth())
            fullExtent.setLatNorth(temp.getLatNorth());
        if(fullExtent.getLatSouth()==361 || temp.getLatSouth()<fullExtent.getLatSouth())
            fullExtent.setLatSouth(temp.getLatSouth());
        return fullExtent;
    }

    private BoundingBox getLatLongOgrInfo(String gdalString) {
        BoundingBox bb = new BoundingBox();
        int start = gdalString.indexOf("Extent: (")+9;
        int end;

        if(start != -1+9) {
            end = gdalString.indexOf(", ", start);

            try {
                String west = gdalString.substring(start, end).trim();
                start = end + 2;
                end = gdalString.indexOf(")", start);
                String south = gdalString.substring(start, end).trim();
                start = gdalString.indexOf("- (", end) + 3;
                end = gdalString.indexOf(", ", start);
                String east = gdalString.substring(start, end).trim();
                start = end + 2;
                end = gdalString.indexOf(")", start);
                String north = gdalString.substring(start, end).trim();
                bb.setLongWest(west);
                bb.setLongEast(east);
                bb.setLatNorth(north);
                bb.setLatSouth(south);
                bb.setGenerated(true);
            } catch (StringIndexOutOfBoundsException e) {
                return bb;
            }
        }
        return bb;
    }
    private BoundingBox getLatLongGdalInfo(String gdalStringFull) {
        int start = gdalStringFull.indexOf("Upper Left  (")+13;
        if(start > 12) {
            try {
                String gdalString = gdalStringFull.substring(start);
                gdalString = gdalString.substring(gdalString.indexOf("(") + 1);
                int end = gdalString.indexOf(",");
                String west = parseDecimalDegrees(gdalString.substring(0, end));
                gdalString = gdalString.substring(end + 1);
                start = 0;
                end = gdalString.indexOf(")");
                String north = parseDecimalDegrees(gdalString.substring(start, end));
                start = gdalString.indexOf("Lower Right (") + 13;
                gdalString = gdalString.substring(start);
                gdalString = gdalString.substring(gdalString.indexOf("(") + 1);
                end = gdalString.indexOf(",");
                String east = parseDecimalDegrees(gdalString.substring(0, end));
                gdalString = gdalString.substring(end + 1);
                start = 0;
                end = gdalString.indexOf(")");
                String south = parseDecimalDegrees(gdalString.substring(start, end));
                BoundingBox bb = new BoundingBox();
                bb.setLongWest(west);
                bb.setLongEast(east);
                bb.setLatNorth(north);
                bb.setLatSouth(south);
                bb.setGenerated(true);
                return bb;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                return new BoundingBox();
            }
        }
        return new BoundingBox();
    }

    private String parseDecimalDegrees(String s) throws NumberFormatException{
        float degrees = Float.parseFloat(s.substring(0,s.indexOf("d")));
        float minutes = Float.parseFloat(s.substring(s.indexOf("d")+1,s.indexOf("'")))/60;
        float seconds = Float.parseFloat(s.substring(s.indexOf("'")+1,s.indexOf("\"")))/3600;
        degrees += minutes + seconds;
        String direction = s.substring(s.indexOf("\"")+1,s.indexOf("\"")+2);
        if(direction.equals("W")||direction.equals(("S")))
            degrees = degrees*-1;
        return String.valueOf(degrees);

    }

    public DataverseRecordFile parse(File file){
        String path = file.getAbsolutePath();
        String name = file.getName();
        DataverseRecordFile temp = new DataverseRecordFile();

        try {
            String gdalString = getGDALInfo(path, name);
            if (gdalString.contains("FAILURE")){
                logger.warn("Something went wrong parsing " + file.getName() + " at " + file.getPath());
                return temp;
        }
            temp.setProjection(getProjection(gdalString));
            temp.setGeometryType(getGeometryType(gdalString));
            GeographicBoundingBox bb;
            if(GeodisyStrings.gdalinfoRasterExtention(name))
                bb = getRaster(gdalString);
            else
                bb = getVector(gdalString,IS_WINDOWS,file.getName(),file.getAbsolutePath());
            temp.setIsFromFile(true);
            temp.setBB(bb.getBB());
        } catch (IOException e) {
            logger.error("Something went wrong parsing file: " + file.getName() + " at " + file.getAbsolutePath());
            return new DataverseRecordFile();
        }
        return temp;
    }

    private String getGeometryType(String gdalString) {
        if(gdalString.contains("Driver: GTiff/GeoTiFFFiles:"))
            return RASTER;
        int start = gdalString.indexOf("Geometry: ") + 10;
        if(start>9){
            int end = gdalString.indexOf("Feature Count:");
            if(end!=-1){
                return gdalString.substring(start,end).trim();
            }
        }
        return "";
    }
}
