package resourcefinder.ui;

import ca.ubc.cs.cpsc210.resourcefinder.model.SelectionState;
import ca.ubc.cs.cpsc210.resourcefinder.model.Service;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// The panel in which the controls are displayed
public class ControlPanel extends JPanel {
    private static final int GAP = 10;  // vertical spacing between components in pixels
    private SelectionState selectionState;
    private ISelectionListener selectionListener;
    private List<ServiceCheckBox> checkBoxes;

    // EFFECTS: constructs panel for displaying user controls
    public ControlPanel(SelectionState selectionState, ISelectionListener selectionListener) {
        this.selectionState = selectionState;
        this.selectionListener = selectionListener;
        checkBoxes = new ArrayList<>();
        Box toolHolder = Box.createVerticalBox();
        addCheckBoxes(toolHolder);
        toolHolder.add(Box.createVerticalStrut(GAP));
        addRadioButtons(toolHolder);
        toolHolder.add(Box.createVerticalStrut(GAP));
        addClearBtn(toolHolder);
        add(toolHolder);
    }

    // MODIFIES: this, toolHolder
    // EFFECTS: add one check box for each service to toolHolder and to list of check boxes
    private void addCheckBoxes(Box toolHolder) {
        Box checkBoxHolder = Box.createVerticalBox();
        checkBoxHolder.setAlignmentX(Box.LEFT_ALIGNMENT);

        checkBoxHolder.add(new JLabel("Filter Services..."));
        checkBoxHolder.add(Box.createVerticalStrut(GAP));
        for(Service next : Service.values()) {
            ServiceCheckBox checkBox = new ServiceCheckBox(next);
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    Service serviceStateChanged = ((ServiceCheckBox) e.getSource()).getService();

                    if (e.getStateChange() == ItemEvent.SELECTED)
                        selectionState.selectService(serviceStateChanged);
                    else
                        selectionState.deselectService(serviceStateChanged);

                    selectionListener.update();
                }
            });
            checkBoxes.add(checkBox);
            checkBoxHolder.add(checkBox);
        }

        toolHolder.add(checkBoxHolder);
    }

    // MODIFIES: toolHolder
    // EFFECTS: adds radio buttons (all and any) to toolHolder
    private void addRadioButtons(Box toolHolder) {
        Box radioBtnHolder = Box.createHorizontalBox();
        radioBtnHolder.setAlignmentX(Box.LEFT_ALIGNMENT);

        JRadioButton any = new JRadioButton("any", true);
        any.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectionState.setSelectAny();
                selectionListener.update();
            }
        });

        JRadioButton all = new JRadioButton("all", false);
        all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectionState.setSelectAll();
                selectionListener.update();
            }
        });

        radioBtnHolder.add(any);
        radioBtnHolder.add(all);

        ButtonGroup radioBtnGroup = new ButtonGroup();
        radioBtnGroup.add(any);
        radioBtnGroup.add(all);

        toolHolder.add(radioBtnHolder);
    }

    // MODIFIES: toolHolder
    // EFFECTS: adds 'clear' button to toolHolder
    private void addClearBtn(Box toolHolder) {
        Box buttonHolder = Box.createHorizontalBox();
        buttonHolder.setAlignmentX(Box.LEFT_ALIGNMENT);
        buttonHolder.add(buildClearBtn());

        toolHolder.add(buttonHolder);
    }

    // EFFECTS: returns 'clear' button
    private JButton buildClearBtn() {
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearCheckBoxes();
                selectionState.setSelectedServices(new HashSet<Service>());
            }

            private void clearCheckBoxes() {
                for(ServiceCheckBox next : checkBoxes)
                    next.setSelected(false);
            }
        });

        return clearBtn;
    }
}
