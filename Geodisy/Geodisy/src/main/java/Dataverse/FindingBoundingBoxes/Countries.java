package Dataverse.FindingBoundingBoxes;

import Dataverse.FindingBoundingBoxes.LocationTypes.Country;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.text.WordUtils;


/**
 * A basically static class that opens a file that holds all the Country codes and bounding box values
 * This can then be accessed to quickly grab a bounding box for a dataset that has a country label but
 * no defined bounding box or geospatial file.
 */
public class Countries {
    static HashMap<String, Country> countries = new HashMap<>();;
    static HashMap<String, String> countryCodes = new HashMap<>();;

    static{
        String countryBoundingBoxesXML = "./Geoname_countries.xml";
        File xmlFile = new File(countryBoundingBoxesXML);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("country");
            for (int i = 0; i <nodeList.getLength(); i++){
                getCountry(nodeList.item(i));
            }
            Country junk = new Country("Junk");
            junk.setCountryCode("_JJ");
            countries.put("Junk", junk);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    private static void getCountry(Node node) {

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String name = getTagValue("countryName", element);
            Country country = new Country(name);
            String cCode = getTagValue("countryCode", element);
            country.setCountryCode(cCode);
            country.setLongEast(Double.parseDouble(getTagValue("east", element)));
            country.setLongWest(Double.parseDouble(getTagValue("west", element)));
            country.setLatSouth(Double.parseDouble(getTagValue("south", element)));
            country.setLatNorth(Double.parseDouble(getTagValue("north", element)));
            countries.put(country.getName(),country);
            countryCodes.put(cCode,name);
        }
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    public static Country getCountryByName(String name){
        String capName = WordUtils.capitalizeFully(name);
        if(countries.containsKey(capName))
            return countries.get(capName);
        else
            return countries.get("Junk");
    }

    public static Country getCountryByCode(String code){
        String codeCorrect = code.toUpperCase();
        if(countryCodes.containsKey(codeCorrect))
            return getCountryByName(countryCodes.get(codeCorrect));
        else
            return countries.get("Junk");
    }
}