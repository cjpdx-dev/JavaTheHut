package presentation;


import logic.Test;
import logic.Item;
import logic.Result;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Chris Jacobs
 * @version 11.02.2018
 * ---------------------
 * This class represents the user interface for the test session.
 * It allows the user to iterate through a list of test pairs and rank
 * those comparisons, storing each ranking into the Test objects ArrayList
 * of TestResults.
 */

public class TestSessionGui extends GUIForm {

    private JPanel rootPanel;
    private JButton item2Button;
    private JButton cantDecideButton;
    private JButton item1Button;
    private JButton nextButton;
    private JButton backButton;
    private JLabel mainLabel;

    private Test newTest;
    private ArrayList<Item> testPairs;
    private int currentTestQuestion;

    private String item1Name;
    private String item2Name;

    private Boolean choosingItem1;
    private Boolean choosingItem2;
    private Boolean choosingNone;

    private int currentTestPair;

    public TestSessionGui() {

        setupTest();

        /**
         * Sets the current user choice to choosingItem1
         */
        item1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUserChoice("item1");
            }
        });

        /**
         * Sets the user's current choice to choosingItem2
         */
        item2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setUserChoice("item2");
            }
        });

        /**
         * Sets the user's current choice to choosingNone
         */
        cantDecideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setUserChoice("noChoice");
            }
        });

        /**
         * Creates a new TestResult object corresponding to the user's
         * preference for the current test question and names of the test
         * items.
         *
         * Stores the result in the test's ArrayList of test results.
         *
         * Finally, sets the user choice fields to false and displays the next
         * test pair.
         */
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Result newResult = new Result(item1Name, item2Name,
                        choosingItem1, choosingItem2, choosingNone);

                newTest.storeResult(newResult);
                clearUserChoice();
                displayNextTestPair();
            }
        });
    }

    /**
     * Sets up the test
     *
     * Sets the current test question to a value of 0
     *
     * Creates a new Test object using a predetermined userId and testId (this will soon
     * be connected with the UserLogin package).
     *
     * Retrieves new test's test pairs that will be iterated over by the user.
     *
     * Sets all userChoice boolean values to false.
     */
    private void setupTest() {

        currentTestQuestion = 0;
        newTest = new Test(1008, 101);
        testPairs = newTest.getTestItemPairs();
        displayNextTestPair();

        choosingItem1 = false;
        choosingItem2 = false;
        choosingNone = false;
    }

    /**
     * Displays the next test pair to the user by updating the text of item1Button
     * and item2Button.
     *
     * Calls the testPairs.getItemString method to retrieve the
     * item names.
     *
     * Increments the current test question. If the current test question
     * is larger than the size of the testPairs ArrayList, finish and close the test.
     */
    private void displayNextTestPair() {

        if (currentTestQuestion < testPairs.size()) {
            item1Name = testPairs.get(currentTestQuestion).getItem1String();
            item2Name = testPairs.get(currentTestQuestion).getItem2String();

            item1Button.setText(item1Name);
            item2Button.setText(item2Name);

            currentTestQuestion++;
        } else {
            newTest.finishTest();
            this.rootPanel.setVisible(false); // needs the extra step of closing the entire
                                              // JFrame, not just the current panel

        }
    }

    /**
     * Sets the user's choice for the given test question and sets the highlighted button
     * to that respective choice.
     * @param userChoice
     */

    private void setUserChoice(String userChoice) {

        switch (userChoice) {

            case "item1":
                choosingItem1 = true;
                highlightButtonChoice(userChoice);
                break;

            case "item2":
                choosingItem2 = true;
                highlightButtonChoice(userChoice);
                break;

            case "noChoice":
                choosingNone = true;
                highlightButtonChoice(userChoice);
                break;

            default:
                break;
        }
    }

    /**
     * Highlights the button corresponding to the user's current choice.
     * @param userChoice String representing the user's current choice.
     */
    private void highlightButtonChoice(String userChoice) {
        switch (userChoice) {
            case "item1":
                //set border on item1Button
                item1Button.setBorder(BorderFactory.createLineBorder(Color.getColor("GREEN"), 5));
                //remove borders from other buttons
                item2Button.setBorder(null);
                cantDecideButton.setBorder(null);
                break;
            case "item2":
                //set border on item2Button
                item2Button.setBorder(BorderFactory.createLineBorder(Color.getColor("GREEN"), 5));
                //remove borders from other buttons
                item1Button.setBorder(null);
                cantDecideButton.setBorder(null);
                break;
            case "noChoice":
                //set border on cantDecideButton
                cantDecideButton.setBorder(BorderFactory.createLineBorder(Color.getColor("GREEN"), 5));
                //remove borders from other buttons
                item1Button.setBorder(null);
                item2Button.setBorder(null);
                break;
            default:
                break;
        }
    }

    /**
     * Clears the user choice fields by setting all boolean fields to false.
     */
    private void clearUserChoice() {
        choosingItem1 = false;
        choosingItem2 = false;
        choosingNone = false;
    }

    /**
     * Gets the rootPanel of the this GUI
     * @return Jpanel rootPanel
     */
    public JPanel getRootPanel() {
        return rootPanel;
    }

    private void showNextItemPair() {
        item1Button.setText(testPairs.get(currentTestPair).getItem1String());
        item2Button.setText(testPairs.get(currentTestPair).getItem2String());
        currentTestPair++;
    }

    public String getCurrentChoice() {
        if (choosingItem1) {
            return "item1";
        }
        else if (choosingItem2) {
            return "item2";
        }
        else if (choosingNone) {
            return "noChoice";
        }
        else {
            return null;
        }
    }
}
