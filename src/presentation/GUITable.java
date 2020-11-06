package presentation;


import logic.Result;
import logic.Session;
import logic.Score;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.*;

/**
 * A form that presents a user interface for browsing scores by session information which the user selecting.
 * Session information (Email-TestNmae-sessionDate)  are presented in a ComboBox
 * and scores are presented in a table.
 *
 * @author Zhimin Xi
 * @version 2018.11.3
 */
public class GUITable extends GUIForm {
    private JTable userTable;
    private JComboBox sessionInfCombo;
    private JPanel rootPanel;
    private DefaultTableModel m_UserModel;
    private ArrayList<Session> m_Sessions;

    /**
     * Constructor for the GUITable interface. Sets up the sessionInf combo and the users table
     *
     */
    public GUITable() {
        setupSessionInfCombo();
        setupUserTableModel();
        showTable(m_Sessions.get(sessionInfCombo.getSelectedIndex()));
        sessionInfCombo.addItemListener(new ItemListener() {

            // This method gets called twice when the user changes the value in the combobox.
            // The previously selected item gets a state change to deselected and the
            // new selected item gets a state change to selected. For the newly selected
            // item, we want to redisplay the results in the user table.
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {

                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    showTable(m_Sessions.get(sessionInfCombo.getSelectedIndex()));
                }
            }
        });

        }

    /**
     * Fetches all the information used and displays them in the SessionInf ComboBox.
     */
    private void setupSessionInfCombo() {

        m_Sessions = Session.fetchSessions();

        for(Session session: m_Sessions) {
            sessionInfCombo.addItem(session.getEmail() + " - " + session.getTestName() + " - " + session.getSessionDate());
        }
    }

    /**
     * Display a list of scores in the Score ArrayList.
     *
     * @param session  The sessionInf for users to be selected (Email-TestName-SessionDate)
     */
    private void showTable(Session session) {

       // Ask the Result class to fetch the results with the given sessionID
        ArrayList<Result> results = Result.fetchResults(session.getSessionID());

        ArrayList<Score> scores = Score.ReportScore(results);
        // clear the previous data out of the table
        m_UserModel.setRowCount(0);
       // Add each score to the table

        /**
         * This comparator compares two scores by their totals  (higher first).
         * sort the Score ArrayList by comparator.
         *
         */
          Comparator<Score> TotalComparator = new Comparator<Score>() {
              public int compare(Score s1, Score s2) {

                  int Total1 = s1.getTotal();
                  int Total2 = s2.getTotal();

                  /*For descending order*/
                  return Total2 - Total1;
              }

          };
         Collections.sort(scores, TotalComparator);



         // add the ItemName, Win, Losses, Ties, and Totals  of scores into the JTable
        for (Score score : scores) {
            m_UserModel.addRow(new Object[]{
                    score.getName(),
                    score.getWin(),
                    score.getLosses(),
                    score.getTies(),
                    score.getTotal()
            });
        }

    }

    /**
     * Initialize the users table in the form. Defines the columns for the table and sets some
     * properties for the table model.
     */
    private void setupUserTableModel() {
        // Create a default table model with five columns named ItemName, Wins, Losses, Ties, and Total, and with table ResultsReporting.data.
        m_UserModel = new DefaultTableModel(
                // Initial ResultsReporting.data (empty)
                new Object[][]{},
                // Initial columns
                new Object[]{ "ItemName","Wins", "Losses", "Ties", "Total" }

        ) {
            // Do not let the user edit values in the table.
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Make the userTable use that model
        userTable.setModel(m_UserModel);

        // Center values in the five columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        userTable.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        userTable.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
        userTable.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
        userTable.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );
        userTable.getColumnModel().getColumn(4).setCellRenderer( centerRenderer );
    }

    /**
     * Returns the root JPanel for the GUITable form.
     *
     * @return  The root JPanel
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }
}
