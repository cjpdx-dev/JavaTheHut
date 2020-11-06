package logic;

import java.util.Arrays;

/**
 * @author Kit Mitchell
 * @version 2018.11.09
 */
public class User {
    private int m_UserID;
    private String m_UserName;
    private String m_Email;
    private char[] m_Password;
    private String m_Role;
    //private BufferedImage m_Image;

    public static final String ADMIN_ROLE = "admin";
    public static final String THERAPIST_ROLE = "therapist";
    public static final String USER_ROLE = "user";

    /**
     * Create a new User object with all values specified.
     *
     * @param userID
     * @param userName
     * @param email
     * @param password
     * @param role
     * future param = image
     */
    public User(int userID, String userName, String email, char[] password, String role/*, BufferedImage image*/) {
        m_UserID = userID;
        m_UserName = userName;
        m_Email = email;
        m_Password = password;
        m_Role = role;
        //m_Image = image;
    }

    /**
     * Logic for logging in
     * First checks that user is in the system
     * If so, it checks that password matches
     *
     * @param email
     * @param password
     * @return User that matches the email if the password matches
     */
    public static User login(String email, char[] password) {
        User user = data.Database.readUsers(email);
        if (user == null) {
            System.out.println("User not found");
            return null;
        }
        if (Arrays.equals(user.getPassword(), password)) {
            System.out.println("User " + user.getUserName() + " logged in successfully.");
            return user;
        }
        else {
            System.out.println("Password incorrect");
            return null;
        }
    }

    /**
     * Register a user to class.
     * First, confirms user's email is not already in system
     * Then, registers user into database
     * @param name
     * @param email
     * @param password
     * @return new User object
     */
    public static User register(String name, String email, char[] password/*, BufferedImage image*/) {
        User user = data.Database.readUsers(email);
        if (user != null) {
            System.out.println("User already exists in system. Alert goes here!");
            return null;
        }
        user = data.Database.registerUser(name, email, password/*, image*/, USER_ROLE);
        return user;
    }

    /**
     * Verifies that a string is blank or not.
     *
     * @param string
     * @return false if empty, true if not
     */
    public static boolean validateBlankString(String string) {
        if(string.equals("")) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Verifies if a char[] has any items in it
     *
     * @param password
     * @return false if the length is 0
     */
    public static boolean validateBlankChar(char password[]) {
        if (password.length == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Verifies if the password and the re-entered password match
     *
     * @param password
     * @param reEnterPassword
     * @return true if they match
     */
    public static boolean validatePasswordForRegisterGUI(char password[], char reEnterPassword[]) {
        if (Arrays.equals(password, reEnterPassword)) {
            return true;
        } else {
            return false;
        }
    }

    public int getUserID() {return m_UserID;}

    public String getUserName() {return m_UserName;}

    public String getEmail() {return m_Email;}

    public char[] getPassword() {return m_Password;}

    public String getRole() {return m_Role;}

//    public BufferedImage getImage() {return m_Image;}
}