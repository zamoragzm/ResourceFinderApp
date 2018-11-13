package resourcefinder.parser;

import ca.ubc.cs.cpsc210.resourcefinder.model.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import java.net.MalformedURLException;
import java.net.URL;

// Handler for XML resource parsing
public class ResourceHandler extends DefaultHandler {
    private ResourceRegistry registry;
    private StringBuilder accumulator;
    private final int NUM_ELEMENTS = 7;
    private int elementCounter;
    private Resource resource;
    private GeoPoint geoPoint;
    private String name;
    private String address;
    private double lat;
    private double lon;
    private URL webadress;
    private String phone;
    private ContactInfo contactInfo;

    // EFFECTS: constructs resource handler for XML parser
    public ResourceHandler(ResourceRegistry registry) {
        this.registry = registry;
        accumulator = new StringBuilder();

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if(qName.toLowerCase().equals("resources")){

        }else if(qName.toLowerCase().equals("resource")){
             elementCounter = 0;
        }else if(qName.toLowerCase().equals("name")){
            elementCounter++;
        }else if(qName.toLowerCase().equals("address")){
            elementCounter++;
        }else if(qName.toLowerCase().equals("lat")){
            elementCounter++;
        }else if(qName.toLowerCase().equals("lon")){
            elementCounter++;
        }else if(qName.toLowerCase().equals("webaddress")){
            elementCounter++;
        }else if(qName.toLowerCase().equals("phone")){
            elementCounter++;
        }else if(qName.toLowerCase().equals("services")){
            elementCounter++;
                resource = new Resource(name,contactInfo);

        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        String data = accumulator.toString().trim();   // trim leading and trailing whitespace
        accumulator.setLength(0);
        if(qName.toLowerCase().equals("resources")){
            if(registry.getResources().isEmpty()){
                throw new SAXException();
            }
        }else if(qName.toLowerCase().equals("resource")){
            if(elementCounter == NUM_ELEMENTS) {
                registry.addResource(resource);
            }
        }else if(qName.toLowerCase().equals("name")){
            name = data;
        }else if(qName.toLowerCase().equals("address")){
            address = data;
        }else if(qName.toLowerCase().equals("lat")){
            lat = Double.parseDouble(data);
        }else if(qName.toLowerCase().equals("lon")){
            lon = Double.parseDouble(data);
            geoPoint = new GeoPoint(lat,lon);
        }else if(qName.toLowerCase().equals("webaddress")){
            try {
                webadress = new URL(data);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new SAXException();
            }
        }else if(qName.toLowerCase().equals("phone")){
            phone = data;
            contactInfo = new ContactInfo(address,geoPoint,webadress,phone);
        }else if(qName.toLowerCase().equals("services")){

        }else if(qName.toLowerCase().equals("service")){
            if(data.equals(Service.FOOD.getDisplayName())){
                resource.addService(Service.FOOD);
            }
            else if(data.equals(Service.SHELTER.getDisplayName())){
                resource.addService(Service.SHELTER);
            }
            else if(data.equals(Service.SENIOR.getDisplayName())){
                resource.addService(Service.SENIOR);
            }
            else if(data.equals(Service.COUNSELLING.getDisplayName())){
                resource.addService(Service.COUNSELLING);
            }
            else if(data.equals(Service.YOUTH.getDisplayName())){
                resource.addService(Service.YOUTH);
            }
            else if(data.equals(Service.LEGAL.getDisplayName())){
                resource.addService(Service.LEGAL);
            }
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        accumulator.append(ch, start, length);
    }
}
