package Dataverse.DataverseJSONFieldClasses;

import BaseFiles.GeoLogger;
import Crosswalking.JSONParsing.DataverseParser;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;

import static org.apache.commons.validator.routines.UrlValidator.ALLOW_ALL_SCHEMES;


public abstract class JSONField {
    protected GeoLogger logger = new GeoLogger(DataverseParser.class.toString());

    /**
     * @param value String that hopefully is a properly formated URL
     * @return Either a validly formated URL or an empty String
     */
    protected String filterURL(String value) {
        try {
            new URI(value).parseServerAuthority();
            return value;
        } catch (URISyntaxException e) {
            logger.warn("Got an invalid url: " + value);
            return "";
        }
    }








    public abstract String getField(String fieldName);

    public void errorParsing(String className, String fieldName){
        logger.error("Something wrong parsing "+ className + ". Title is " + fieldName);
        System.out.println("Something wrong with " + className + " parsing. Title is " + fieldName);
    }

    public void errorGettingValue(String className, String fieldName){
        logger.error("Something wrong getting field from "+ className + ". Field name is " + fieldName);
        System.out.println("Something wrong with " + className + " getting value.");
    }

    protected String stringed(float val) {
        return String.valueOf(val);
    }
    protected String stringed(int val){
        return String.valueOf(val);
    }

}
