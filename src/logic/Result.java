package logic;

import data.Database;

import java.util.ArrayList;

/**
 * Business ResultsReporting.logic class for holding Result ResultsReporting.data and functionality.
 */

public class Result {


    private int m_SessionID;
    private String m_Item1Name;
    private String m_Item2Name;
    private int m_Item1ID;
    private int m_Item2ID;
    private int m_ResultCode;


    /**
     * Static method that returns the list of result with the given sessionID
     * in use in the  RESULT JOIN ITEM AND ITEM table of the JavaTheHutDB.
     * @param sessionID
     * @return  The list of result .
     */
    public static ArrayList<Result> fetchResults(int sessionID) {
       return Database.fetchResults(sessionID);
    }

    /**
     *
     * Construct a Result object with the given sessionID
     * @param sessionID
     * @param item1Name
     * @param item2Name
     * @param item1ID
     * @param item2ID
     * @param resultCode
     *
     */
    public Result(int sessionID,String item1Name, String item2Name, int item1ID, int item2ID, int resultCode) {
        m_Item1Name = item1Name;
        m_Item2Name = item2Name;
        m_Item1ID = item1ID;
        m_Item2ID = item2ID;
        m_ResultCode = resultCode;
        m_SessionID = sessionID;

    }



    public int getItem1ID (){ return m_Item1ID; }
    public int getItem2ID(){return m_Item2ID;}
    public int getResultCode(){return m_ResultCode;}
    public int getSessionID2(){ return m_SessionID;}
    public String getItem1Name(){return m_Item1Name; }
    public String getItem2Name(){return m_Item2Name;}



    //from TestResult
    private String item1Name;
    private String item2Name;
    private int item1Id;
    private int item2Id;
    private int resultCode;

    public Result(String item1Name, String item2Name, Boolean choosingItem1,
                      Boolean choosingItem2, Boolean noChoice) {

        this.item1Name = item1Name;
        this.item2Name = item2Name;
        setUserChoice(choosingItem1, choosingItem2, noChoice);
    }

    /**
     * Setter method that sets the users choice with respect to which itemName
     * they prefer.
     * @param choosingItem1 - boolean value representing a choice of item1
     * @param choosingItem2 - boolean value representing a choice of item2
     * @param noChoice - boolean value representing no user preference
     */
    private void setUserChoice(Boolean choosingItem1, Boolean choosingItem2,
                               Boolean noChoice) {
        if (choosingItem1) {
            resultCode = 1;
        }
        else if (choosingItem2) {
            resultCode = 2;
        }
        else if (noChoice) {
            resultCode = 0;
        }
        else {
            System.out.println("Error: all fields of GuiTestResult.setUserChoice" +
                    " were false");
        }
    }

    /**
     * Setter method for this TestResult's item1Id field.
     * @param item1Id - item1's ID value
     */
    public void setItem1Id(int item1Id) {
        this.item1Id = item1Id;
    }

    /**
     * Setter method for this TestResult's item2Id field.
     * @param item2Id - item2's ID value
     */
    public void setItem2Id(int item2Id) {
        this.item2Id = item2Id;
    }

    //Accessor methods

   // public String getItem1Name() { return item1Name; }

    //public String getItem2Name() { return item2Name; }

    //public int getResultCode() { return resultCode; }

    public int getItem1Id() { return item1Id; }

    public int getItem2Id() { return item2Id; }




}
