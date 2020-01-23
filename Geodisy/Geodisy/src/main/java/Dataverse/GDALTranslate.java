package Dataverse;

import BaseFiles.GeoLogger;
import BaseFiles.GeodisyStrings;


import java.io.File;
import java.io.IOException;


import static BaseFiles.GeodisyStrings.*;

public class GDALTranslate {
    GeoLogger logger;

    public GDALTranslate() {
        logger = new GeoLogger(this.getClass());
    }

    public String rasterTransform(String dirPath, String name){
        boolean transformed = process(dirPath,name,RASTER);

        if(transformed) {
            int period = name.lastIndexOf(".");
            return name.substring(0, period + 1) + "tif";
        }else
            return name;
    }
    public void rasterTransformTest(String dirPath, String name, boolean newLocation){
        process(dirPath,name,RASTER,newLocation);
    }

    public String vectorTransform(String dirPath, String name){
        boolean transformed = process(dirPath,name,VECTOR);

        if(transformed) {
            int period = name.lastIndexOf(".");
            return name.substring(0, period + 1) + "shp";
        }
        else{
            return name;
        }

    }
    public void vectorTransformTest(String dirPath, String name, boolean newLocation) {
        boolean transformed = process(dirPath, name, VECTOR, newLocation);
    }

    private boolean process(String dirPath, String name, String transformType) {
        return process(dirPath,dirPath,name,transformType, false);
    }

    private boolean process(String dirPath, String name, String transformType,boolean newLocation) {
        return process(dirPath,dirPath,name,transformType, newLocation);
    }
    public boolean process(String sourcePath, String destPath, String name, String transformType, boolean newLocation){

        if(newLocation){
            sourcePath = GeodisyStrings.replaceSlashes(sourcePath);
            destPath = GeodisyStrings.replaceSlashes(destPath);
        }else {
            sourcePath = GeodisyStrings.replaceSlashes(sourcePath);
            destPath = GeodisyStrings.replaceSlashes(destPath);
        }

        String call;
        String nameStub = name.substring(0,name.lastIndexOf("."));
        File file;

        ProcessBuilder processBuilder= new ProcessBuilder();

        for(int i = 0; i<8; i++){
        if(transformType.equals(RASTER)) {
            call = GDAL_TRANSLATE + sourcePath + name + " " + destPath + nameStub + ".tif";
            //System.out.println(call);
            processBuilder.command("bash", "-c", call);
            try {

                if (IS_WINDOWS) {
                    Runtime.getRuntime().exec(call);
                } else {
                    Process process = processBuilder.start();
                    process.waitFor();
                    process.destroy();
                }

                File newFile = new File(destPath+nameStub+".tif");
                if(newFile.exists()) {
                    file = new File(sourcePath + name);
                    file.delete();
                    return true;
                }else{
                    System.out.println("Translation failure #" + i);
                }

            } catch (IOException | InterruptedException e) {
                logger.error("Something went wrong converting " + name + " to geotiff");
            }
        } else{
                call = OGR2OGR + destPath + nameStub + ".shp " + sourcePath + name;
                //System.out.println(call);
                processBuilder.command("bash", "-c", call);
                try {
                    if (IS_WINDOWS) {
                        Runtime.getRuntime().exec(call);
                    } else {
                        Process p = processBuilder.start();
                        try {
                            p.waitFor();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        p.destroy();

                    }
                    File newFile = new File(destPath+nameStub+".shp");
                    if(newFile.exists()) {
                        file = new File(sourcePath + name);
                        file.delete();
                        return true;
                    }else{
                        System.out.println("Translation failure #" + i);
                    }
                } catch (IOException e) {
                    logger.error("Something went wrong converting " + name + " to shapefile");
                }
            }
        }
        System.out.println("Couldn't convert file: " + sourcePath+name);
        file = new File(sourcePath+name);
        file.delete();
        return false;
    }




}
