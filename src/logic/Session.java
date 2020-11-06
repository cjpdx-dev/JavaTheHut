package logic;

import data.Database;

import java.util.ArrayList;
import java.lang.String;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Business ResultsReporting.logic class for holding user ResultsReporting.data and functionality.
 */
public class Session {
    private int m_SessionID;

    private String m_Email;
    private String m_TestName;
    private String m_SessionDate;




    /**
     * Static method that returns the list of session information(Eamil, testName, sessionID, userID, sessionDate)
     * in use in the SESSION JOIN RESULT AND USERS table of the database.
     *
     * @return  The list of session information .
     */
    public static ArrayList<Session> fetchSessions() {
        return Database.fetchSessions();
    }

    public  Session(){

    }
    /**
     *
     * Construct the Session objects with the given sessionID
     * @param sessionID
     *
     * @param email
     * @param testName
     * @param sessionDate
     */
    public Session( int sessionID, String sessionDate, String email, String testName) {
        m_SessionID = sessionID;
        m_SessionDate = sessionDate;
        m_Email = email;
        m_TestName = testName;
    }

    // note that only getters are provided, User objects are essentially immutable because
    // the properties are all private and no setters are provided.
    public int getSessionID() {
      return m_SessionID;
    }



    public String getSessionDate() {
        return m_SessionDate;
    }

    public String getEmail() {
        return m_Email;
    }

    public String getTestName() {
        return m_TestName;
    }




        /**
         *
         * Method to return the list of current testIDs in SESSION table.
         *
         * @return testIDs in SESSION table
         */
        public static ArrayList<Integer> getSession() {
            return Database.getSession();
        }



//    public TestSession() {
//
//    }

    /**
     * Loads the HashMap of testItems from the TestItemsDatabase and
     * verifies the contents of the HashMap by printing to the console.
     *
     * @param testId - the testId corresponding to the test the user is taking.
     * @return - the HashMap of item values.
     */
    public static HashMap<Integer, String> loadTestData(int testId) {

        HashMap<Integer, String> testItemsMap = Database.fetchTestData(testId);

        //Verify the items returned from the database
        System.out.println("TestItemsDatabase items loaded...");
        Iterator iter = testItemsMap.entrySet().iterator();

        while (iter.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)iter.next();
            System.out.println("Item ID: " + pair.getKey() + " Item Name: " + pair.getValue());
        }

        return testItemsMap;
    }

    /**
     * Stores a completed test session in the database. This method first
     * validates the test results that will be stored by writing to the console.
     * The method then calls the TestResultsDatabase.insertTestSession which returns
     * a value corresponding to the sessionID being used, which is then passed
     * to TestResultsDatabase.insertTestResults to store the results.
     *
     * @param testId -  the testId corresponding to the test the user is taking.
     * @param userId - the userId corresponding to the specific user taking the test.
     * @param testResults - the ArrayList of TestResult of objects from the completed
     *                    test session
     */
    public static void storeTestSession(int testId, int userId,
                                        ArrayList<Result> testResults) {

        int nextTestSessionId;

        //Validate resultData before upload
        System.out.println("Storing Results...");
        System.out.println("Test ID: " + testId);
        System.out.println("User ID: " + userId);

        int i = 1;
        for(Result testResult : testResults) {
            System.out.println("Test Pair " + i + ":");
            System.out.println(testResult.getItem1Id() + " : "  + testResult.getItem1Name());
            System.out.println(testResult.getItem2Id() + " : " + testResult.getItem2Name());
            System.out.println("Result Code: " + testResult.getResultCode() + "\n");
            i++;
        }

        System.out.println("Uploading Test Results...");
        nextTestSessionId = Database.insertTestSession(testId, userId);
        Database.insertTestResults(nextTestSessionId, testResults);
    }



    }


