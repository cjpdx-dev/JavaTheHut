package logic;

import java.util.*;

/**
 * @author Chris Jacobs
 * @version 10.24.2018
 * ---------------------
 * The Test class represents the unique test that the user is taking.
 * Its fields consist of the rawTestData retrieved from TestSession.loadTestData,
 * and ArrayList of TestPair objects that will be compared by the user, a List
 * of testItemIds that are used to help populate the testItemPairs, and an
 * ArrayList of TestResult objects that will be uploaded to the RESULT database
 * table.
 */

public class Test {

    private int userId;
    private int testId;

    private HashMap<Integer, String> rawTestData;
    private ArrayList<logic.Item> testItemPairs;
    private List<Integer> testItemIdList;
    private ArrayList<Result> testResults;

    public Test(int userId, int testId) {

        this.userId = userId;
        this.testId = testId;

        testResults = new ArrayList<>();
        rawTestData = Session.loadTestData(testId);
        populateTestPairs();
    }

    /**
     * Populates the testItemPairs that will be compared by the user during the test.
     */
    private void populateTestPairs() {

        System.out.println("Populating Test Pairs");

        //Create and populate an ArrayList of test itemIds.
        Set<Integer> itemIdSet = new HashSet<Integer>(rawTestData.keySet());

        testItemIdList = new ArrayList<>(itemIdSet);

        //Create and populate an ArrayList of TestPair objects
        testItemPairs = new ArrayList<>();
        for (int rootIndex = 0; rootIndex < testItemIdList.size(); rootIndex++) {

            for(int pairIndex = rootIndex + 1; pairIndex < rawTestData.size(); pairIndex++) {

                Item newTestPair = new Item(testItemIdList.get(rootIndex),
                        rawTestData.get(testItemIdList.get(rootIndex)),
                        testItemIdList.get(pairIndex), rawTestData.get(testItemIdList.get(pairIndex)));

                testItemPairs.add(newTestPair);
            }
        }

        //Validate testItemPairs
        System.out.println("Validating Test Pairings: ");
        for (Item testPair : testItemPairs) {
            System.out.println(testPair.getItem1String() + " : " + testPair.getItem2String());
        }
    }

    /**
     * This method retrieves an incomplete TestResult object passed to it by the
     * TestSessionGui. It calls getItemId() for the two itemNames stored in the
     * TestResult object, and searches the original raw data for the key value
     * associated with the item name.
     *
     * This eventually needs to be changed, because our data set is not guaranteed
     * to have a 1:1 key-value relationship. If a matching key-value is not found,
     * the TestResult object is not saved in the ArrayList and is not uploaded to the
     * RESULT table.
     *
     * @param newResult - the incomplete TestResult object being stored in the local
     *                  ArrayList of testResults
     */
    public void storeResult(Result newResult) {
        String item1Name = newResult.getItem1Name();
        String item2Name = newResult.getItem2Name();

        int item1Id = getItemId(item1Name);
        int item2Id = getItemId(item2Name);

        if (item1Id >= 0) {
            newResult.setItem1Id(item1Id);
        } else {
            System.out.println("Error: " + item1Name + " does not have a corresponding itemId. " +
                    "Result will not be stored.");
        }

        if (item1Id >= 0) {
            newResult.setItem2Id(item2Id);
            testResults.add(newResult);
        } else {
            System.out.println("Error: " + item2Name + " does not have a corresponding itemId. " +
                    "Result will not be stored.");
        }
    }

    /**
     * Finds the itemId key corresponding to that itemId's itemName value.
     *
     * @param itemName - the itemName string that we are attempting to match
     *                 to a key value.
     *
     * @return - the itemId key found that matches the value. If no key is found, return
     * a value of 0
     */
    private int getItemId(String itemName) {
        Iterator rawDataEntries = rawTestData.entrySet().iterator();
        int itemId = -1;


        while (rawDataEntries.hasNext()) {

            HashMap.Entry entry = (HashMap.Entry)rawDataEntries.next();
            Integer entryKey = (Integer)entry.getKey();
            String entryValue = entry.getValue().toString();

            if (itemName.equals(entryValue)) {
                itemId = entryKey;
            }
        }
        return itemId;
    }

    /**
     * Finishes the test by passing the local ArrayList of TestResult objects
     * to TestSession.storeTestSession.
     */
    public void finishTest() {
        Session.storeTestSession(testId, userId, testResults);
    }

    /**
     * Returns
     * @return - an ArrayList of TestPair objects.
     */
    public ArrayList<logic.Item> getTestItemPairs () {
        return testItemPairs;
    }

}

