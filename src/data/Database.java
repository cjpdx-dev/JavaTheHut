package data;

import logic.Result;
import logic.Session;
import logic.Result;
import logic.User;

import java.sql.*;
import java.util.ArrayList;
import java.lang.String;
import java.util.HashMap;


/**
 * Database class for the data layer. Note that there is only one database, so all the properties
 * and methods are static, and the single connection object is shared across all calls to the
 * database methods.
 */
public class Database {
    // Connection string for connecting to SQL Server at CISDBSS, using the 234a_JavaTheHut database.
    // Requires jtds.XXX.jar to be included in the project with the correct dependency set.

    public static final String CONNECTION_STRING = "jdbc:jtds:sqlserver://cisdbss.pcc.edu/234a_JavaTheHut";

    // some SQL queries using from ResultReporting
    private static final String FETCH_RESULTS_QUERY = "SELECT SessionID,ResultCode,Item1ID,I1.ItemName AS Item1Name,Item2ID,I2.ItemName" +
            " AS Item2Name FROM RESULT JOIN ITEM AS I1 ON I1.ItemID = RESULT.Item1ID JOIN ITEM AS I2 ON I2.ItemID = RESULT.Item2ID" +
            " WHERE SessionID = ?;";
    private static final String FETCH_SESSIONS_QUERY = "SELECT * " +
        "FROM   SESSION JOIN USERS ON USERS.UserID = SESSION.UserID JOIN TEST ON SESSION.TestID = TEST.TestID";


    // some SQL queries using from AdminSetup
    private static final String FETCH_ITEMS = "SELECT ItemName FROM ITEM WHERE TestID = 103 ORDER BY ItemID;";
    private static final String FETCH_ITEM_MAXID = "SELECT MAX(ItemID) FROM ITEM;";
    private static final String WRITE_ITEMS = "INSERT INTO ITEM (ItemID, TestID, ItemName) VALUES (?, ?, ?);";
    private static final String FETCH_SESSION = "SELECT DISTINCT TestID FROM SESSION;";
    private static final String FETCH_TESTS = "SELECT TestID, TestName FROM TEST;";

    // some SQL queries using from TestSession
    private static final String FETCH_TEST_ITEMS_QUERY = "SELECT ItemName, ItemID FROM ITEM WHERE TestID = ?";


    private static final String TEST_SESSION_INSERT  =
            "INSERT INTO SESSION VALUES ((SELECT coalesce(max(SessionID), 0) " +
                    "+ 1 FROM SESSION), ?, ?, GetDate());";

    private static final String FETCH_TEXT_SESSION_ID =
            "SELECT coalesce(max(SessionID), 0) + 1 FROM SESSION;";

    private static final String RESULT_INSERT =
            "INSERT INTO RESULT VALUES ((SELECT coalesce(max(ResultID), 0) " +
                    "+ 1 FROM RESULT), ?, ?, ?, ?);";

    //some sql queries using from UserLoginSignup

    private static final String GET_USER_INFO_QUERY = "SELECT UserID, UserName, Password, Email, Role FROM USERS WHERE Email = ?";
    private static final String REGISTER_USER = "INSERT INTO USERS (UserName, Email, Password, Role) VALUES (?, ?, ?, ?);" +
            " SELECT SCOPE_IDENTITY() AS ID;";

    // The one and only connection object
    private static Connection m_Connection = null;

    /**
     * Create a new connection object if there isn't one already.
     */
    private static void connect() {
        if (m_Connection != null)
            return;
        try {
            // Create a database connection with the given username and password.
            m_Connection = DriverManager.getConnection(CONNECTION_STRING, "234a_JavaTheHut", "!!!234a_JavaTheHut!!!");
        } catch (SQLException e) {
            System.err.println("Error! Couldn't connect to the database!");
        }
    }

    /**
     * Fetch a list of results with the given session.
     *
     * @param sessionID  The sessionID for results that we want to fetch
     * @return      The list of results matching that sessionID.
     */
    public static ArrayList<Result> fetchResults(Integer sessionID) {
        ResultSet rs = null;
        ArrayList<Result> results = new ArrayList<>();

        try {
            // Create a connection if there isn't one already
            connect();

            // Prepare a SQL statement
            PreparedStatement stmt = m_Connection.prepareStatement(FETCH_RESULTS_QUERY);

            // This one has a single parameter for the sessionID, so we bind the value of sessionID to the parameter
            stmt.setInt(1, sessionID);

            // Execute the query returning a result set
            rs = stmt.executeQuery();

            // For each row in the result set, create a new Result object with the specified values
            // and add it to the list of results.
            while (rs.next()) {
                results.add(new Result(
                        rs.getInt("SessionID"),
                        rs.getString("Item1Name"),
                        rs.getString("Item2Name"),
                        rs.getInt("Item1ID"),
                        rs.getInt("Item2ID"),
                        rs.getInt("ResultCode")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Return the list of results. Will be an empty list if there was an error.
        return results;
    }

    /**
     * Return all the Email-TestName-SessionDate that are in use in the SESSION, USERS, AND TEST join table.
     *
     * @return  The list of sessions(Email,TestName, and SessionDate) as Strings.
     */
    public static ArrayList<Session> fetchSessions() {
        ResultSet rs = null;
        ArrayList<Session> sessions = new ArrayList<>();

        try {
            // Create a connection if there isn't one already
            connect();

            // Prepare the SQL statement
            PreparedStatement stmt = m_Connection.prepareStatement(FETCH_SESSIONS_QUERY);

            // This statement doesn't currently have any parameters, so we don't need to bind anything.
            // Execute the query returning a result set.
            rs = stmt.executeQuery();

            // For each result in the result set, add the SessionDate,Email, and TestName value to the list of sessions.
            while (rs.next()) {

                sessions.add(new Session(
                        rs.getInt("SessionID"),
                        rs.getString("SessionDate"),
                        rs.getString("Email"),
                        rs.getString("TestName")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Return the list of results. Will return an empty list if there was an error.
        return sessions;
    }



    // from AdminSetup

    /**
     * Public static method that returns all item names in the ITEM table.
     *
     * @return A list of item names
     */
    public static ArrayList<String> getItems() {
        ArrayList<String> testItems = new ArrayList<>();
        connect();
        try {
            PreparedStatement stmt = m_Connection.prepareStatement(FETCH_ITEMS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                testItems.add(rs.getString("ItemName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error");
        }
        return testItems;
    }

    /**
     * Public static method that returns all item names in the ITEM table.
     *
     * @return A list of item names
     */
    public static ArrayList<String> getTests() {
        ArrayList<String> tests = new ArrayList<>();
        connect();
        try {
            PreparedStatement stmt = m_Connection.prepareStatement(FETCH_TESTS);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tests.add(rs.getString("TestID"));
                tests.add(rs.getString("TestName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error");
        }
        return tests;
    }

    /**
     * Public static method that returns session data from the SESSION table.
     *
     * @return the session testID
     */
    public static ArrayList<Integer> getSession() {
        ArrayList<Integer> session = new ArrayList<>();
        connect();
        try {
            PreparedStatement stmt = m_Connection.prepareStatement(FETCH_SESSION);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                session.add(rs.getInt("TestID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error");
        }
        return session;
    }

    /**
     * Public static method to write test items and associating testID
     * to the ITEM table.
     *
     * @param itemName the item name
     */
    public static void writeItems(int itemID, int testID, String itemName) {
        connect();
        try {
            PreparedStatement stmt = m_Connection.prepareStatement(WRITE_ITEMS);
            stmt.setInt(1, itemID);
            stmt.setInt(2, testID);
            stmt.setString(3, itemName);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }

    /**
     * Public Boolean method to remove specified item from the ITEM table.
     *
     * @param id the selected item
     * @return true if connection and sql statement successful
     */
    public Boolean removeItem(String id) {
        connect();
        String DELETE_ITEM = "DELETE FROM ITEM WHERE ItemName = '" + id + "'";
        try {
            PreparedStatement stmt = m_Connection.prepareStatement(DELETE_ITEM);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error");
            return false;
        }
    }

    /**
     * Public static method to get the highest item ID currently in the ITEM table.
     *
     * @return the highest item ID
     */
    public static int fetchItemMaxID() {
        int itemMaxID = 0;
        connect();
        try (
                PreparedStatement stmt = m_Connection.prepareStatement(FETCH_ITEM_MAXID);
                ResultSet rs = stmt.executeQuery()
        ) {
            rs.next();
            itemMaxID = rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemMaxID;
    }



    //from TestSession






        /**
         * A connection is established and a result set is fetched consisting of ItemIds
         * and ItemNames that correspond to the testID parameter.
         *
         * The result set is stored in a HashMap consisting of an itemId as a key and an
         * itemName as a value for that key. This HashMap object is then returned to the
         * TestSession.loadTestData() method.
         *
         * @param testID : A testId that corresponds to a set of items in the ITEM table
         * @return testItems : a HashMap consisting of itemIds and itemNames
         */
        public static HashMap<Integer, String> fetchTestData(int testID) {

            HashMap<Integer, String> testItems = new HashMap<>();

            try {
                connect();
                PreparedStatement stmt = m_Connection.prepareStatement(FETCH_TEST_ITEMS_QUERY);
                stmt.setInt(1, testID);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    testItems.put(rs.getInt("ItemID"), rs.getString("ItemName"));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return testItems;
        }



        //from TestResultsDatabase.java
        /**
         * Inserts the TestSession data into the SESSION table.
         *
         * @param userId - an integer corresponding to the user taking the test.
         * @param testId - a testId corresponding to the test the user is taking.
         * @return
         */
        public static int insertTestSession(int userId, int testId) {

            int nextTestSessionId = 0;

            try {
                // Attempt the connection.
                connect();

                //Create the statement for fetching the next session id
                PreparedStatement fetchNextSessionIdStatement = m_Connection.prepareStatement(FETCH_TEXT_SESSION_ID);
                ResultSet rs = fetchNextSessionIdStatement.executeQuery();

                //If the ResultSet has an entry, store that entry.
                if(rs.next()) {
                    nextTestSessionId = rs.getInt(1);
                }

                //Insert the TestSession data into the SESSION table
                PreparedStatement writeSessionStatement = m_Connection.prepareStatement(TEST_SESSION_INSERT);
                writeSessionStatement.setInt(1, testId);
                writeSessionStatement.setInt(2, userId);
                writeSessionStatement.execute();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Return the next available TestSessionId that was found and subsequently
            // used in this method.
            return nextTestSessionId;
        }

        /**
         * Inserts an ArrayList of TestResult objects into the RESULT table, using
         * the SessionID that was previously used to insert the TestSession into
         * the SESSION table.
         *
         * @param nextTestSessionId - the sessionId that we just used when writing to
         *                          the SESSION table
         * @param testResults - an ArrayList of TestResult objects.
         */
        public static void insertTestResults(int nextTestSessionId, ArrayList<Result> testResults) {

            try {
                //Attempt the connection
                connect();

                //Iterate over the ArrayList of TestResult objects, creating a new connection
                //statement for each testResult. Assign the nextTestSessionId and the testResults
                //fields to each statement and execute the statement.
                for(Result testResult : testResults) {
                    PreparedStatement insertResultsStatement = m_Connection.prepareStatement(RESULT_INSERT);

                    insertResultsStatement.setInt(1, nextTestSessionId);
                    insertResultsStatement.setInt(2, testResult.getItem1Id());
                    insertResultsStatement.setInt(3, testResult.getItem2Id());
                    insertResultsStatement.setInt(4, testResult.getResultCode());

                    insertResultsStatement.execute();
                }

            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

    /**
     * This class connects to the 234a_JavaTheHut Database.
     *
     * @author Kit Mitchell
     * @version 2018.11.09
     */






        /**
         * This function verifies the username is in the database
         *
         * @param email
         * @return a User object
         */
        public static User readUsers(String email) {
            connect();
            try {
                PreparedStatement stmt = m_Connection.prepareStatement(GET_USER_INFO_QUERY);

                stmt.setString(1, email);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new User(rs.getInt("UserID"), rs.getString("UserName"), rs.getString("Email"), rs.getString("Password").toCharArray(), rs.getString("Role"));
                }
                return null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Register a new user
         * Takes the name, email, password, and role and enters them into a new User entry in the Database
         *
         * @param name
         * @param email
         * @param password
         * @param role
         * @return new User object
         */
        public static User registerUser(String name, String email, char[] password, String role) {
            connect();
            try {
                PreparedStatement stmt = m_Connection.prepareStatement(REGISTER_USER);

                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, String.valueOf(password));
                stmt.setString(4, role);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new User(rs.getInt("ID"), name, email, password, role);
                }
                return null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }



