package resourcefinder.parser;

import ca.ubc.cs.cpsc210.resourcefinder.model.ResourceRegistry;

import java.io.IOException;

// Resource parser
public interface IResourceParser {

    // EFFECTS:
    //   return parsed resource registry;
    //   resource not added to registry if any one of the following fields is missing:
    //     - name, address, phone, webaddress, location, services;
    //   throws IOException if there is a problem reading data from file (e.g., path to file is not valid);
    //   throws ResourceParsingException if:
    //     - data in file does not follow expected syntax
    //     - URL cannot be formed from given webaddress
    //     - latitude or longitude cannot be parsed as a double
    //     - no resources were added to the registry
    ResourceRegistry parse() throws ResourceParsingException, IOException;
}
