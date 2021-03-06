package BaseFiles;

import org.apache.commons.lang3.ArrayUtils;

import static BaseFiles.PrivateStrings.*;

public class GeodisyStrings {
    private final static boolean DEV = false;
    public final static boolean GEOSPATIAL_ONLY = false;
    public final static String GIT_PASSWORD = PRIVATE_GIT_PASSWORD;
    public final static String GEOSERVER_PASSWORD = PRIVATE_GEOSERVER_PASSWORD;
    public final static String GEOSERVER_USERNAME = PRIVATE_GEOSERVER_USERNAME;
    public final static String GEONAMES_USERNAME = PRIVATE_GEONAMES_USERNAME;
    public final static String POSTGIS_USER_PASSWORD = PRIVATE_POSTGIS_USER_PASSWORD;
    public final static String OPENGEOMETADATA_USERNAME = PRIVATE_OPENGEOMETADATA_USERNAME;
    public final static String OPENGEOMETADATA_PASSWORD = PRIVATE_OPENGEOMETADATA_PASSWORD;
    public final static boolean TEST = false; //change this to false when in production
    public final static int NUMBER_OF_RECS_TO_HARVEST = 50000;
    public final static String[] HUGE_RECORDS_TO_IGNORE_UNTIL_LATER = {"10.5683/SP2/FJWIL8","10.5683/SP/Y3HRN","10864/GSSJX","10.5683/SP2/JP4WDF","10864/9KJ1L","10864/11086","10864/9VNIK","10.5683/SP/Y3HMRN","10.5683/SP/OEIP77","10.5683/SP/IP9ERW","10.5683/SP/NTUOK9","10864/11369","10864/11175","10.5683/SP/BT7HN2","10.5683/SP/4RFHBJ","10.5683/SP/T7ZJAF","10.5683/SP/RZM9HE","10.5683/SP/RAJQ2P","10.5683/SP2/AAGZDG","10.5683/SP2/1XRF9U","10.5683/SP2/MICSLT"};
    public final static String[] PROCESS_THESE_DOIS = {}; //"10.5683/SP2/UEJGTV" "10864/11669" "10.5683/SP2/GKJPIQ""10.5683/SP2/KYHUNF""10.5683/SP/EGOYE3""10.5683/SP2/LAWLTI""10.5072/FK2/PLD5VK","10.5683/SP2/UEJGTV","10.5683/SP/SBTXLS","10.5683/SP/UYBROL","10864/XER6B","10864/10197""10.5683/SP/OZ0WB0","10.5683/SP/S0MQVP","10.5683/SP/5S5Y9T","10.5683/SP/30JPOR","10.5683/SP/ASR2XE","10.5683/SP2/1VWZNC","10.5683/SP/AB5A9O","10.5683/SP2/YNOJSD","10.5683/SP/AB5A9O","10.5683/SP/2ZARY2","10.5683/SP2/ZDAHQG","10.5683/SP2/JFQ1SZ"
    //Repositories (add new repository URLS to a appropriate repository URL array below)
        // New Repository Types need new URL Arrays [Geodisy 2]

        public final static String SANDBOX_DV_URL = "https://206-12-90-131.cloud.computecanada.ca/"; //currently our sandbox
        public final static String TEST_SCHOLARS_PORTAL = "https://demodv.scholarsportal.info/";
        public final static String SCHOLARS_PORTAL = "https://dataverse.scholarsportal.info/"; //Don't use this unless SP gives approval
        public final static String SCHOLARS_PORTAL_CLONE = "https://dvtest.scholarsportal.info/";
        public final static String[] DATAVERSE_URLS = new String[]{SCHOLARS_PORTAL_CLONE};

        public static boolean windowsComputerType(){
            return  System.getProperty("os.name")
                    .toLowerCase().startsWith("windows");
        }

        public final static boolean IS_WINDOWS = windowsComputerType();
        public static String getRoot(){
            boolean isWindows = System.getProperty("os.name")
                    .toLowerCase().startsWith("windows");
            if(isWindows)
                return WINDOWS_ROOT;
            else {
                if(DEV)
                    return EVAN_VM_CENTOS_ROOT;
                else
                    return FRDR_VM_CENTOS_ROOT;
            }
        }

    //File paths
        private final static String WINDOWS_ROOT = "C:\\geodisy\\Geodisy\\Geodisy\\";
        private final static String EVAN_VM_CENTOS_ROOT = "/home/centos/Geodisy/";
        private final static String FRDR_VM_CENTOS_ROOT = "/home/centos/geodisy/Geodisy/Geodisy/";
        public final static String GEODISY_PATH_ROOT = getRoot();
        public final static String SAVED_FILES = GEODISY_PATH_ROOT + replaceSlashes("savedFiles");
        public final static String LOGS = GEODISY_PATH_ROOT + replaceSlashes("logs");
        public final static String EXISTING_RECORDS = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingRecords.txt");
        public final static String EXISTING_CHECKS = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingChecks.txt");
        public final static String EXISTING_BBOXES = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingBBoxes.txt");
        public final static String DOWNLOADED_FILES = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/DownloadedFiles.csv");
        public final static String VECTOR_RECORDS = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingVectorRecords.txt");
        public final static String TEST_EXISTING_RECORDS = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/TestExistingRecords.txt");
        public final static String TEST_EXISTING_BBOXES = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/TestExistingBBoxes.txt");
        public final static String RASTER_RECORDS = GEODISY_PATH_ROOT + replaceSlashes("savedFiles/ExistingRasterRecords.txt");
        public final static String RECORDS_TO_CHECK = GEODISY_PATH_ROOT + replaceSlashes("logs/recordsToCheck.log");
        public final static String EXISTING_CALL_TO_CHECK = GEODISY_PATH_ROOT + replaceSlashes("logs/existingCallToCheck.txt");
        public final static String ERROR_LOG = GEODISY_PATH_ROOT + replaceSlashes("logs/error.log");
        public final static String WARNING_LOG = GEODISY_PATH_ROOT + replaceSlashes("logs/warning.log");
        public final static String XML_NS = "http://www.isotc211.org/2005/";
        public final static String COUNTRY_VALS =  GEODISY_PATH_ROOT + replaceSlashes("geodisyFiles/Geoname_countries.xml");
        public final static String ALL_CITATION_METADATA = GEODISY_PATH_ROOT + replaceSlashes("geodisyFiles/AllCitationMetadata.json");
        public final static String TEST_GEO_COVERAGE = GEODISY_PATH_ROOT + replaceSlashes("geodisyFiles/geocoverage.json");
        public final static String XML_TEST_FILE = GEODISY_PATH_ROOT + replaceSlashes("geodisyFiles/XMLTestDJO.xml");
        public final static String DATASET_FILES_PATH = replaceSlashes("datasetFiles/");
        public final static String OPEN_GEO_METADATA_BASE = "https://github.com/OpenGeoMetadata/ca.frdr.geodisy/";

    //Geonames
        public final static String GEONAMES_SEARCH_BASE = "http://api.geonames.org/search?q=";


    //GDAL
        private final static String GDALINFO_LOCAL = "gdalinfo -approx_stats ";
        private final static String OGRINFO_LOCAL = "ogrinfo -ro -al -so ";
        private final static String GDALINFO_CLOUD = "/usr/gdal30/bin/gdalinfo -approx_stats ";
        private final static String OGRINFO_CLOUD = "/usr/gdal30/bin/ogrinfo -ro -al -so ";
        public final static String GDALINFO = getGdalInfo();
        public final static String OGRINFO = getOgrInfo();
        private final static String[] GDALINFO_PROCESSABLE_EXTENSIONS = { ".tif", ".tiff",".xyz", ".png"};
        private final static String[] NON_TIF_GEOTIFF_EXTENSIONS = {".aux.xml",".tab",".twf",".tifw", ".tiffw",".wld", ".tif.prj",".tfw"};
        public final static String[] GDALINFO_RASTER_FILE_EXTENSIONS = ArrayUtils.addAll(GDALINFO_PROCESSABLE_EXTENSIONS,NON_TIF_GEOTIFF_EXTENSIONS);
        private final static String[] NON_SHP_SHAPEFILE_EXTENSIONS = {".shx", ".dbf", ".sbn",".prj"};
        private final static String[] OGRINFO_PROCESSABLE_EXTENTIONS = {".geojson",".shp",".gpkg"}; //also .csv/.tab, but need to check if the csv is actually geospatial in nature
        private final static String[] CSV_EXTENTIONS = {".csv", "tab"};
        private final static String[] INTRUM_VECTOR = ArrayUtils.addAll(OGRINFO_PROCESSABLE_EXTENTIONS,CSV_EXTENTIONS);
        public final static String[] OGRINFO_VECTOR_FILE_EXTENSIONS = ArrayUtils.addAll(NON_SHP_SHAPEFILE_EXTENSIONS, INTRUM_VECTOR);
        public final static String FINAL_OGRINFO_VECTOR_FILE_EXTENSIONS = ".shp";
        public final static String[] PREVIEWABLE_FILE_EXTENSIONS = {".tif"};
        private final static String OGR2OGR_LOCAL = "ogr2ogr -f \"ESRI Shapefile\" -t_srs EPSG:4326 ";
        private final static String GDAL_TRANSLATE_LOCAL = "gdal_translate -of GTiff ";
        private final static String OGR2OGR_CLOUD = "/usr/bin/ogr2ogr -t_srs EPSG:4326 -f \"ESRI Shapefile\" ";
        private final static String GDAL_TRANSLATE_CLOUD = "/usr/bin/gdal_translate -of GTiff ";
        public final static String OGR2OGR = getOgr2Ogr();
        public final static String GDAL_TRANSLATE = getGdalTranslate();
        public final static String[] PROCESSABLE_EXTENSIONS = ArrayUtils.addAll(GDALINFO_PROCESSABLE_EXTENSIONS,OGRINFO_PROCESSABLE_EXTENTIONS);

        private static String getOgr2Ogr(){
            if(IS_WINDOWS)
                return OGR2OGR_LOCAL;
            else
                return OGR2OGR_CLOUD;
        }

        private static String getGdalTranslate(){
            if(IS_WINDOWS)
                return GDAL_TRANSLATE_LOCAL;
            else
                return GDAL_TRANSLATE_CLOUD;
        }
    private static String getGdalInfo(){
        if(IS_WINDOWS)
            return GDALINFO_LOCAL;
        else
            return GDALINFO_CLOUD;
    }
    private static String getOgrInfo(){
        if(IS_WINDOWS)
            return OGRINFO_LOCAL;
        else
            return OGRINFO_CLOUD;
    }

    public static String replaceSlashes(String s){
        if(IS_WINDOWS)
            return s.replace("/","\\");
        else
            return s.replace("\\","/");
    }

    public static String urlSlashes(String s){
            return s.replace("\\","/");
    }


    //Unused file type extensions
    public final static String[] FILE_TYPES_TO_IGNORE = {".txt",".doc",".pdf",".jpg", ".docx",".las",".xml", ".nc","bil", "xtc"};
    public final static String[] FILE_TYPES_TO_ALLOW = ArrayUtils.addAll(GDALINFO_PROCESSABLE_EXTENSIONS, OGRINFO_VECTOR_FILE_EXTENSIONS);



        public final static String RASTER = "Raster";
        public final static String VECTOR = "Vector";
        public final static String UNDETERMINED = "Undetermined";


    //XML value types
        public final static String CHARACTER = "CharacterString";
        public final static String DATE = "Date";
        public final static String DATE_TIME = "DateTime";
        public final static String INTEGER = "Integer";
        public final static String BOOLEAN = "Boolean";
        public final static String DECIMAL = "Decimal";
        public final static String MEASURE = "Measure";
        public final static String LOCAL_NAME = "LocalName";


    //Geocombine

    public final static String SOLR_PATH_PROD = "SOLR_URL=http://www.example.com:1234/solr/collection ";
    public final static String SOLR_PATH_TEST = "";
    public final static String SOLR_PATH = IS_WINDOWS? SOLR_PATH_TEST:SOLR_PATH_PROD;
    public final static String DEV_ADDRESS = "206-12-92-97.cloud.computecanada.ca";
    public final static String PROD_ADDRESS = "geoserver.frdr.ca";
    public final static String ADDRESS = addressToUse(TEST);
    public final static String VM_BASE_PATH_DEV = "C:/geodisy/Geodisy/Geodisy/";
    public final static String VM_BASE_PATH_PROD = "https://" + ADDRESS + "/";
    public final static String BASE_PATH = vmToUse();
    public final static String END_XML_JSON_FILE_PATH = BASE_PATH + "geodisy/";
    public final static String PATH_TO_XML_JSON_FILES = END_XML_JSON_FILE_PATH;
    public final static String OGM_PATH = "OGM_PATH=/var/www/geoserver.frdr.ca/html/geodisy/";
    public final static String MOVE_METADATA = "rsync -auhv " + getRoot() + "metadata/* /var/www/" + ADDRESS + "/html/geodisy/";
    public final static String MOVE_DATA = "rsync -auhv " + getRoot() + "datasetFiles/* /var/www/" + ADDRESS + "/html/geodisy/";
    //TODO need to update the SOLR clear to access the solr index on geo.frdr.ca VM
    public final static String CLEAR_SOLR = "sudo su - root -c \"cd /root/solr-8.3.0/bin/ && ./post -c geoblacklight-prod delete_ALL.xml\"";
    public final static String DELETE_DUPLICATE_META_FOLDER = "rm -rf " + getRoot() + "metadata/*";
    public final static String DELETE_DUPLICATE_DATA_FOLDER = "rm -rf " + getRoot() + "datasetFiles/*";
    public final static String GEOCOMBINE = OGM_PATH +" /home/centos/geodisy/bin/bundle exec rake geocombine:index";
    //public final static String GEOCOMBINE = "/bin/sh /home/centos/Geodisy/combine.sh";
    public final static String BASE_LOCATION_TO_STORE_METADATA = "metadata/";

    public static String vmToUse(){
        if(IS_WINDOWS)
            return VM_BASE_PATH_DEV;
        else
            return VM_BASE_PATH_PROD;
    }

    public static String addressToUse(boolean test){
        if(test)
            return DEV_ADDRESS;
        else
            return PROD_ADDRESS;
    }
    public static boolean fileTypesToIgnore(String title){
        String[] temp = ArrayUtils.addAll(OGRINFO_VECTOR_FILE_EXTENSIONS,GDALINFO_RASTER_FILE_EXTENSIONS);
        for(String s: temp){
            if(title.toLowerCase().endsWith(s)||title.toLowerCase().endsWith("zip"))
                return false;
        }
        return true;
    }

    public static boolean fileToAllow(String title){
        for (String s : GeodisyStrings.FILE_TYPES_TO_ALLOW) {
            if (title.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean gdalinfoRasterExtention(String title){
        for(String s : GDALINFO_RASTER_FILE_EXTENSIONS) {
            if (title.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean ogrinfoVectorExtension(String title){
        for(String s : OGRINFO_VECTOR_FILE_EXTENSIONS) {
            if (title.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean otherShapeFilesExtensions(String title){
        for(String s : NON_SHP_SHAPEFILE_EXTENSIONS){
            if(title.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean otherTiffFilesExtensions(String title){
        for(String s: NON_TIF_GEOTIFF_EXTENSIONS){
            if(title.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean isProcessable(String title){
        for(String s: PROCESSABLE_EXTENSIONS){
            if(title.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }

    public static boolean hasGeospatialFile(String title){
        return gdalinfoRasterExtention(title.toLowerCase())||ogrinfoVectorExtension(title.toLowerCase());
    }

    public static boolean isPreviewable(String title){
        for(String s : PREVIEWABLE_FILE_EXTENSIONS){
            if(title.toLowerCase().endsWith(s))
                return true;
        }
        return false;
    }
}
