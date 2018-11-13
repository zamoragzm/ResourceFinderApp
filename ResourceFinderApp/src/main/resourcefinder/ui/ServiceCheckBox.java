package resourcefinder.ui;

import ca.ubc.cs.cpsc210.resourcefinder.model.Service;

import javax.swing.*;

// Represents a check box for a service
public class ServiceCheckBox extends JCheckBox {
    private Service service;

    // EFFECTS: constructs check box for given service
    public ServiceCheckBox(Service service) {
        super(service.getDisplayName());
        this.service = service;
    }

    public Service getService() {
        return service;
    }
}
