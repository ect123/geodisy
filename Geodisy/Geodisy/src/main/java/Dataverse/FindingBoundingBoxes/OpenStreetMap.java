package Dataverse.FindingBoundingBoxes;


import Dataverse.FindingBoundingBoxes.LocationTypes.BoundingBox;
import Dataverse.FindingBoundingBoxes.LocationTypes.Country;
import org.geonames.ToponymSearchCriteria;
import org.geonames.WebService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;


/**
 * Accesses OpenStreetMap.org to try and generate a bounding box for the dataset
 */
public class OpenStreetMap implements FindBoundBox {

    @Override
    public BoundingBox getDVBoundingBox(String countryName) {
        Country country = Countries.getCountryByName(countryName);

        return country.getBoundingBox();
    }

    @Override
    public BoundingBox getDVBoundingBox(String country, String state) {
        String locationURL = state + ",%2C" + country;
        String boundingBox = getBBString(locationURL);
        if(boundingBox.matches(""))
            return getDVBoundingBox(country);

        return parseCoords(boundingBox);
    }

    @Override
    public BoundingBox getDVBoundingBox(String country, String state, String city) {
        String locationURL = city + ",%2C" + state + ",%2C" + country;
        String boundingBox = getBBString(locationURL);
        if(boundingBox.matches(""))
            return getDVBoundingBox(country, state);

        return parseCoords(boundingBox);
    }

    @Override
    public BoundingBox getDVBoundingBox(String country, String state, String city, String other) {
        String locationURL = city + ",%2C" + state + ",%2C" + country;
        String boundingBox = getBBString(locationURL);
        if(boundingBox.matches(""))
            return getDVBoundingBox(country,state,city);
        return parseCoords(boundingBox);
    }

    @Override
    public BoundingBox getDVBoundingBoxOther(String other) {
        String boundingBox = getBBString(other);
        return parseCoords(boundingBox);
    }


    private String getBBString(String locationURL) {
        String baseQuery = "https://nominatim.openstreetmap.org/search?q=";
        String suffix = "&format=xml";
        try {
            String url = baseQuery + locationURL + suffix;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new URL(url).openStream());
            NodeList nodes = doc.getElementsByTagName("place");
            Node node = nodes.item(0);
            Element element = (Element) node;
            return element.getAttribute("boundingbox");


        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Parses the coordinate doubles from a coordinate string from OpenStreetMap
     * @param bbString coordinate string from OpenStreetMap
     * @return Bounding Box with the parsed coordinates or 361 if there is an
     * error in the coordinates string
     *
     * String coordinate order is LatSouth, LatNorth, LongWest, LongEast
     */
    private BoundingBox parseCoords(String bbString){
        BoundingBox bb = new BoundingBox();
        int comma = bbString.indexOf(",");
        String singleVal = bbString.substring(0,comma);
        bb.setLatSouth(getDoubleVal(singleVal));
        bbString = bbString.substring(comma+1);
        comma = bbString.indexOf(",");
        singleVal = bbString.substring(0,comma);
        bb.setLatNorth(getDoubleVal(singleVal));
        bbString = bbString.substring(comma+1);
        comma = bbString.indexOf(",");
        singleVal = bbString.substring(0,comma);
        bb.setLongWest(getDoubleVal(singleVal));
        bbString = bbString.substring(comma+1);
        singleVal = bbString;
        bb.setLongEast(getDoubleVal(singleVal));

        bb = checkCoords(bb);

        return bb;
    }

    private BoundingBox checkCoords(BoundingBox bb) {
        if(bb.getLatNorth()==361|bb.getLatSouth()==361|bb.getLongEast()==361|bb.getLongWest()==361){
            bb.setLongEast(361);
            bb.setLatSouth(361);
            bb.setLatNorth(361);
            bb.setLongWest(361);
        }
        return bb;
    }

    private double getDoubleVal(String doubleString) {
        double val;
        try {
            val = Double.parseDouble(doubleString);
            return val;
        } catch (NumberFormatException e){
            val = 361;
            return val;

        }
    }
}