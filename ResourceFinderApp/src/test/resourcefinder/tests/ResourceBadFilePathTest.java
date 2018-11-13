package resourcefinder.tests;

import resourcefinder.parser.IResourceParser;
import resourcefinder.parser.ResourceParsingException;
import resourcefinder.parser.XMLResourceParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

// Unit tests for XMLResourceParser class
public class ResourceBadFilePathTest {
    private static final String FILE_NAME = "./data/fileDoesntExist.xml";
    private IResourceParser resourceParser;

    @BeforeEach
    public void runBefore() {
        resourceParser = new XMLResourceParser(FILE_NAME);
    }

    @Test
    public void testParse() throws ResourceParsingException, IOException {
        try {
            resourceParser.parse();
            fail("IOException should have been thrown");
        } catch (ResourceParsingException e) {
            e.printStackTrace();
            fail("ResourceParsingException should not have been thrown");
        } catch (IOException e) {
            // expected
        }
    }
}