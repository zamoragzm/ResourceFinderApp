package resourcefinder.tests;

import resourcefinder.parser.IResourceParser;
import resourcefinder.parser.ResourceParsingException;
import resourcefinder.parser.XMLResourceParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

// Unit tests for XMLResourceParser class
public class ResourceParserBadLocationTest {
    private static final String FILE_NAME = "./data/resourceWithBadLocation.xml";
    private IResourceParser resourceParser;

    @BeforeEach
    public void runBefore() {
        resourceParser = new XMLResourceParser(FILE_NAME);
    }

    @Test
    public void testParse() throws ResourceParsingException, IOException {
        try {
            resourceParser.parse();
            fail("ResourceParsingException should have been thrown");
        } catch (ResourceParsingException e) {
            // expected
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException should not have been thrown");
        }
    }
}