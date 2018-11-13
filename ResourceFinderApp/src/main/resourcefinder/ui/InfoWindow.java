package resourcefinder.ui;

import ca.ubc.cs.cpsc210.resourcefinder.model.GeoPoint;
import ca.ubc.cs.cpsc210.resourcefinder.model.Resource;
import ca.ubc.cs.cpsc210.resourcefinder.model.SelectionState;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.net.URL;

// A window for displaying information about resources
public class InfoWindow extends JScrollPane implements MouseListener {
    private static final double X_SCALE = 0.7; // proportion of map window to be used for info window
    private static final double Y_SCALE = 0.3;
    private static final int WIDTH = (int) (ResourceFinderApp.MAP_WIDTH * X_SCALE);
    private static final int HEIGHT = (int) (ResourceFinderApp.MAP_HEIGHT * Y_SCALE);
    private JEditorPane textPane;
    private SelectionState selectionState;
    private JXMapViewer mapViewer;

    // EFFECTS: constructs info window, not visible to user
    public InfoWindow(SelectionState selectionState, JXMapViewer mapViewer) {
        super();
        this.selectionState = selectionState;
        this.mapViewer = mapViewer;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        buildTextPane();

        getViewport().setView(textPane);
        setVisible(false);
    }

    // MODIFIES: this
    // EFFECTS: builds pane in which resource info will be displayed
    private void buildTextPane() {
        textPane = new JEditorPane("text/html", "");
        textPane.setEditable(false);
        textPane.setMargin(new Insets(4, 10, 4, 10));

        textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
            }
        });

        textPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    openURLInBrowser(e.getURL());
                }
            }
        });
    }

    //---------------------------------------------------------------------
    // Implementation of MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
        Resource selected = getResourceAtPoint(e.getPoint());

        if (selected != null)
            displayResourceInfo(selected);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    //---------------------------------------------------------------------

    // EFFECTS: returns resource at given point, or null if no resource sufficiently close to point
    private Resource getResourceAtPoint(Point point) {
        for (Resource next : selectionState.getResourcesWithSelectedServices()) {
            GeoPoint locn = next.getContactInfo().getGeoLocation();
            GeoPosition resourceLocn = new GeoPosition(locn.getLatitude(), locn.getLongitude());
            Point2D resourceLocnInViewer = mapViewer.convertGeoPositionToPoint(resourceLocn);
            if (isHit(point, resourceLocnInViewer))
                return next;
        }

        return null;
    }

    // EFFECTS: returns true if point is sufficiently close to resourceLocnInViewer
    private boolean isHit(Point point, Point2D resourceLocnInViewer) {
        return point.y < resourceLocnInViewer.getY() && resourceLocnInViewer.getY() - point.y < 25
                && Math.abs(point.x - resourceLocnInViewer.getX()) < 10;
    }

    // MODIFIES: this
    // EFFECTS: display information for given resource in text pane
    private void displayResourceInfo(Resource selected) {
        HTMLResourceFormatter formatter = new HTMLResourceFormatter(selected);
        textPane.setText(formatter.format());
        setVisible(true);
        getParent().validate();
        getParent().repaint();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getVerticalScrollBar().setValue(0);
            }
        });
    }

    // EFFECTS: open url in default web browser; display error message when operation cannot be completed
    private void openURLInBrowser(URL url) {
        Desktop desktop = Desktop.getDesktop();

        try {
            desktop.browse(url.toURI());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this.getParent(),
                    "Unable to open web page", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
