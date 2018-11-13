package resourcefinder.parser;

import ca.ubc.cs.cpsc210.resourcefinder.model.ResourceRegistry;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

// Parser for resources in XML format
public class XMLResourceParser implements IResourceParser {
    private String sourceFileName;

    // EFFECTS: constructs parser for data in given source file
    public XMLResourceParser(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }

    @Override
    public ResourceRegistry parse() throws ResourceParsingException, IOException {
        ResourceRegistry registry = new ResourceRegistry();

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            ResourceHandler rh = new ResourceHandler(registry);
            saxParser.parse(sourceFileName, rh);
        } catch (ParserConfigurationException e) {
            ResourceParsingException resourceParsingException = new ResourceParsingException("Parser configuration error");
            resourceParsingException.initCause(e);
            throw resourceParsingException;
        } catch (SAXException e) {
            ResourceParsingException resourceParsingException = new ResourceParsingException("SAX parser error");
            resourceParsingException.initCause(e);
            throw resourceParsingException;
        } catch(NumberFormatException e) {
            ResourceParsingException resourceParsingException = new ResourceParsingException("XML format error");
            resourceParsingException.initCause(e);
            throw resourceParsingException;
        }

        return registry;
    }


}
