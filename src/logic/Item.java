package logic;

import data.Database;
import java.util.ArrayList;

/**
 * Item Class to store and transit items between Database and GUI.
 *
 * @author (Brady McAllister)
 * @version (11.02.2018)
 *
 */

public class Item {
    private int itemID;
    private int testID;
    private String itemName;

    /**
     * Item class constructor.
     *
     * @param itemId the item ID
     * @param testID the test ID
     * @param itemName the item name
     */
    public Item(int itemId, int testID, String itemName) {
        this.itemID = itemID;
        this.testID = testID;
        this.itemName = itemName;
    }

    /**
     * Public static method to return the list of current items
     * in the ITEM table.
     *
     * @return items in ITEM table
     */
    public static ArrayList<String> getItems() {
        return Database.getItems();
    }

    /**
     * Public String method to get the test ID.
     *
     * @return the test ID
     */
    public int getTestID() {
        return testID;
    }



    /**
     * Public String method to get the item name.
     *
     * @return the item name
     */

//    public String getItemName() {
//        return itemName;
//    }




    //private String itemName;
    private int itemId;

    public Item(int itemId, String itemName) {

        this.itemName = itemName;
        this.itemId = itemId;
    }

    public String getItemName() { return itemName; }

    //Not currently used.
    public int getItemId() { return itemId; }


    private Item testItem1;
    private Item testItem2;

    public Item(int item1Id, String item1Name, int item2Id, String item2Name) {

        testItem1 = new Item(item1Id, item1Name);
        testItem2 = new Item(item2Id, item2Name);
    }

    public String getItem1String() { return testItem1.getItemName(); }

    public String getItem2String() { return testItem2.getItemName(); }

}
