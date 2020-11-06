package presentation;

import logic.Item;
import data.Database;
import logic.Session;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

/**
 * The administrator setup GUI class and associating functionality.
 *
 * @author (Brady McAllister & Kit Mitchell)
 * @version (11.02.2018)
 *
 */

public class AdminSetupUI extends GUIForm {
    private JPanel rootPanel;
    private JLabel testState;
    private JLabel testStatus;
    private JButton addItemButton;
    private JButton deleteItemButton;
    private JTextField addItemTextField;
    private JList currentItemsList;
    private JButton createTestButton;
    private JButton exitButton;
    private JButton updateTest;
    private Vector items;
    private static final int maxItemID = Database.fetchItemMaxID();
    private int nextItemID = maxItemID + 1;
    private String removeID = "";
    private int currentTestID = 103;

    public AdminSetupUI() {
        items = new Vector();

        Database db = new Database();

        setupItemList();

        createTestButtonStatus();

        updateTest.setEnabled(false);

        rootPanel.setPreferredSize(new Dimension(360, 400));

        /* If the current test is equal to the test in the SESSION table, a user has taken the test,
        and Admin Setup UI is disabled. If the current test is not equal to the test in the SESSION table,
        the Admin Setup UI is enabled. */
        if (compareTestID()) {
            createTestButton.setEnabled(false);
            addItemButton.setEnabled(false);
            deleteItemButton.setEnabled(false);
            testState.setText("COMPLETED");
            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            items.clear();
        } else {
            testState.setText("OPEN");
            testState.setForeground(Color.GREEN.darker());

            /* Adds an item when add item button is pushed. */
            addItemButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String itemName = addItemTextField.getText();
                    Item name = new Item(nextItemID, currentTestID, itemName);
                    if (itemName.equals("")) {
                        JOptionPane.showMessageDialog(rootPanel, "Please Input An Item", "Error!",
                                JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (itemName.length() > 15) {
                                JOptionPane.showMessageDialog(rootPanel, "Invalid Entry. " +
                                                "Please enter an item with fewer characters. Max Characters = 15", "Error!",
                                        JOptionPane.ERROR_MESSAGE);
                            } else {
                                if (items.contains(itemName)) {
                                    JOptionPane.showMessageDialog(rootPanel, "Invalid Entry. " +
                                            "No Duplicate Items", "Error!", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    db.writeItems(nextItemID, name.getTestID(), name.getItemName());
                                    items.add(itemName);
                                    currentItemsList.setListData(items);
                                    createTestButtonStatus();
                                    addItemTextField.setText("");
                                    nextItemID++;
                                }
                            }
                    }
                }
            });

            /* Deletes a selected item from the current items list. */
            deleteItemButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int index = currentItemsList.getSelectedIndex();
                    setupItemList();
                    if (index == -1) {
                        JOptionPane.showMessageDialog(rootPanel, "Please Select An Item", "Error!",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        if (new Database().removeItem(removeID)) {
                            addItemTextField.setText("");
                            setupItemList();
                            createTestButton.setEnabled(true);
                            createTestButtonStatus();
                        } else {
                            JOptionPane.showMessageDialog(rootPanel, "Error. Item Deletion " +
                                    "Unsuccessful");
                        }
                    }
                }
            });

            /* Closes the Admin Setup UI when the exit button is pushed. */
            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            /* Current items list showing current test items. */
            currentItemsList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                }
            });

            /* Creates a new test when the create test button is pushed. */
            createTestButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(rootPanel, "Tests Created");
                    createTestButton.setEnabled(false);
                    addItemButton.setEnabled(false);
                    deleteItemButton.setEnabled(false);
                    updateTest.setEnabled(true);
                    testState.setText("SUBMITTED");
                    testState.setForeground(Color.ORANGE.darker());
                }
            });
        }

            /* Selects item from list to be added to DELETE_ITEM SQL statement. */
            currentItemsList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    int size = currentItemsList.getModel().getSize();
                    if (size >= 1) {
                        removeID = currentItemsList.getSelectedValue().toString();
                    }
                }

            });

            /* Opens up current test for editing. */
            updateTest.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createTestButton.setEnabled(true);
                    addItemButton.setEnabled(true);
                    deleteItemButton.setEnabled(true);
                    updateTest.setEnabled(false);
                    testState.setText("EDITABLE");
                    testState.setForeground(Color.GREEN.darker());
                }
            });
        }

    /**
     * Public void method to populate current items Jlist with current items in ITEM table.
     */
    public void setupItemList() {
        ArrayList<String> itemNames = Item.getItems();
        items.clear();
        for (String item : itemNames) {
            items.add(item);
            currentItemsList.setListData(items);
        }
    }

    public Boolean checkItemList(String item) {
        ArrayList<String> itemNames = Item.getItems();
        for (String items : itemNames)
            if (items.equals(item)) {
                return true;
            } else {
                return false;
            }
        return true;
    }

    /**
     * Public void method which enables or disables the create test button based on
     * the number of items in the current items list.
     */
    public void createTestButtonStatus() {
        int size = currentItemsList.getModel().getSize();
        if (size < 2) {
            createTestButton.setEnabled(false);
        } else {
            createTestButton.setEnabled(true);
        }
    }

    /**
     * Public Boolean method which compares the current test (TestID 102)
     * with the completed test (TestID 101).
     */
    public Boolean compareTestID() {
        Session sn = new Session();
        ArrayList<Integer> testID = new ArrayList();
        testID.add(currentTestID);
        if (sn.getSession().equals(testID)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Public JPanel method that returns the rootPanel.
     */
    public JPanel getRootPanel(){
        return rootPanel;
    }
}
