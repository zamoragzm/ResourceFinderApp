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
public class ResourceParserTest {
    private static final String FILE_NAME = "./data/resources.xml";
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
        assertEquals(9, registry.getResources().size());
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
        assertEquals("Law Students' Legal Advice Program", last.getName());
        ContactInfo contactInfo = last.getContactInfo();
        assertEquals("Room 129, Allard Hall, 1822 East Mall, Vancouver, BC V6T 1Z1", contactInfo.getAddress());
        assertEquals("http://www.lslap.bc.ca", contactInfo.getWebAddress().toString());
        assertEquals("604-822-5791", contactInfo.getPhoneNumber());
        GeoPoint locn = contactInfo.getGeoLocation();
        assertEquals(49.2698666, locn.getLatitude(), DELTA);
        assertEquals(-123.2535821, locn.getLongitude(), DELTA);
        Set<Service> services = last.getServices();
        assertEquals(1, services.size());
        assertTrue(services.contains(Service.LEGAL));


    }
}