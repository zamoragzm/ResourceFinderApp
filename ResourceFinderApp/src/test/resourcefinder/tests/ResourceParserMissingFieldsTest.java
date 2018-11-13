package resourcefinder.tests;

import ca.ubc.cs.cpsc210.resourcefinder.model.*;
import resourcefinder.parser.IResourceParser;
import resourcefinder.parser.XMLResourceParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


// Unit tests for XMLResourceParser class
public class ResourceParserMissingFieldsTest {
    private static final String FILE_NAME = "./data/resourcesMissingFields.xml";
    private static final double DELTA = 1.0e-8;  // tolerance for testing equality on doubles
    private ResourceRegistry registry;

    @BeforeEach
    public void runBefore() {
        IResourceParser resourceParser = new XMLResourceParser(FILE_NAME);
        try {
            registry = resourceParser.parse();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testNumResources() {
        assertEquals(3, registry.getResources().size());
    }

    @Test
    public void testFirstResource() {
        Resource first = registry.getResources().get(0);
        assertEquals("Downtown Eastside Women's Centre", first.getName());
        ContactInfo contactInfo = first.getContactInfo();
        assertEquals("302 Columbia Street, Vancouver, BC V6A 4J1", contactInfo.getAddress());
        assertEquals("http://dewc.ca/programs/health-and-wellness", contactInfo.getWebAddress().toString());
        assertEquals("604-715-8480", contactInfo.getPhoneNumber());
        GeoPoint locn = contactInfo.getGeoLocation();
        assertEquals(49.2821393, locn.getLatitude(), DELTA);
        assertEquals(-123.1020496, locn.getLongitude(), DELTA);
        Set<Service> services = first.getServices();
        assertEquals(2, services.size());
        assertTrue(services.contains(Service.FOOD));
        assertTrue(services.contains(Service.SENIOR));
    }

    @Test
    public void testLastResource() {
        Resource last = registry.getResources().get(registry.getResources().size() - 1);
        assertEquals("Family Services", last.getName());
        ContactInfo contactInfo = last.getContactInfo();
        assertEquals("1638 E Broadway, Vancouver, BC V5N 1W1", contactInfo.getAddress());
        assertEquals("http://lookoutsociety.ca", contactInfo.getWebAddress().toString());
        assertEquals("604-731-4951", contactInfo.getPhoneNumber());
        GeoPoint locn = contactInfo.getGeoLocation();
        assertEquals(49.26216, locn.getLatitude(), DELTA);
        assertEquals(-123.0709403, locn.getLongitude(), DELTA);
        Set<Service> services = last.getServices();
        assertEquals(2, services.size());
        assertTrue(services.contains(Service.COUNSELLING));
        assertTrue(services.contains(Service.YOUTH));
    }
}