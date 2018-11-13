package resourcefinder.ui;

/**
 * This application uses the JXMapViewer2 library:
 *     https://github.com/msteiger/jxmapviewer2
 * and is used under the terms of the license presented in
 *     /data/libs/jxMapViewerLicense.txt
 */

import resourcefinder.parser.IResourceParser;
import resourcefinder.parser.XMLResourceParser;
import ca.ubc.cs.cpsc210.resourcefinder.model.*;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

// The resource finder application
public class ResourceFinderApp extends JFrame implements ISelectionListener {
    public static final int MAP_WIDTH = 800;
    public static final int MAP_HEIGHT = 600;
    private static final String DATA_SOURCE = "./data/resources.xml";
    private SelectionState selectionState;
    private ResourceRegistry registry;
    private JXMapViewer mapViewer;

    // EFFECTS: constructs resource finder application
    ResourceFinderApp() {
        super("Resource Finder");
        loadData();
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        buildMapViewer();
        add(BorderLayout.CENTER, mapViewer);
        add(BorderLayout.EAST, new ControlPanel(selectionState, this));
        InfoWindow infoWindow = new InfoWindow(selectionState, mapViewer);
        mapViewer.addMouseListener(infoWindow);
        mapViewer.setLayout(new GridBagLayout());
        mapViewer.add(infoWindow);
        updateMarkers();
        pack();
        setVisible(true);
    }

    @Override
    public void update() {
        updateMarkers();
    }

    // MODIFIES: this
    // EFFECTS: add markers to map corresponding to selected resources
    private void updateMarkers() {
        Set<Resource> resources = selectionState.getResourcesWithSelectedServices();
        Set<Waypoint> markers = new HashSet<>();

        for (Resource next : resources) {
            GeoPoint resourceLocn = next.getContactInfo().getGeoLocation();
            GeoPosition geoPosition = new GeoPosition(resourceLocn.getLatitude(), resourceLocn.getLongitude());
            markers.add(new DefaultWaypoint(geoPosition));
        }

        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setWaypoints(markers);
        mapViewer.setOverlayPainter(waypointPainter);
    }

    // MODIFIES: this
    // EFFECTS: load data from file into resource registry
    private void loadData() {
        try {
            IResourceParser parser = new XMLResourceParser(DATA_SOURCE);
            registry = parser.parse();
            selectionState = new SelectionState(registry);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    e.getMessage() + "\nCaused by: " + e.getCause().getMessage(),
                    "Parsing error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: this
    // EFFECTS: build map viewer centred on Vancouver
    private void buildMapViewer() {
        mapViewer = new JXMapViewer();
        mapViewer.setPreferredSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));

        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        tileFactory.setThreadPoolSize(8);
        mapViewer.setTileFactory(tileFactory);


        GeoPosition vancouver = new GeoPosition(49.25, -123.15);
        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(vancouver);
    }

    public static void main(String[] args) {
        new ResourceFinderApp();
    }
}
