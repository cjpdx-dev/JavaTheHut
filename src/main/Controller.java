package main;

import presentation.GUITable;
import presentation.TestSessionGui;
import presentation.GUIForm;
import presentation.LoginForm;
import presentation.RegisterForm;
import logic.User;
import presentation.AdminSetupUI;

import javax.swing.*;

import static logic.User.ADMIN_ROLE;
import static logic.User.THERAPIST_ROLE;
import static logic.User.USER_ROLE;

/**
 * Controller class to control the flow of application execution.
 *
 * @author (Brady McAllister)
 * @version (11.02.2018)
 *
 */
public class Controller {

    //from UserLoginsignup presentation Controller
    private static JFrame m_Frame;
    private static char[] password = new char[0];
    private static User m_User = new User(-1, "", "", password, null);


    /**
     * Initiates the administrator setup GUI.
     */
    public static void start() {
        createAndShowGui();
    }

    /**
     * Creates the administrator setup GUI.
     */
    public static void createAndShowGui() {
        JFrame frame = new JFrame("Administrator Setup");
        m_Frame = frame;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // frame.getContentPane().add(new GUITable().getRootPanel());
        //frame.getContentPane().add(new AdminSetupUI().getRootPanel());
       frame.getContentPane().add(new LoginForm(m_User).getRootPanel());

      // frame.getContentPane().add(new TestSessionGui().getRootPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    /**
     * Opens new GUIForm
     *
     * @param form
     */
    public static void showForm(GUIForm form) {
        JPanel root = form.getRootPanel();

        m_Frame.getContentPane().removeAll();
        m_Frame.getContentPane().add(root);
        m_Frame.pack();
        m_Frame.setLocationRelativeTo(null);
        m_Frame.setVisible(true);
    }
    /**
     * Opens LoginForm
     */
    public static void showLogin() {
        showForm(new LoginForm(m_User));
    }

    /**
     * Opens RegisterForm
     */
    public static void showRegister() {
        showForm(new RegisterForm(m_User));
    }

    public static void setUser(User user) {m_User = user;}

    /**
     * After confirming login, directs user appropriately based on the role
     */
    public static void login() {
        if(m_User.getRole().toLowerCase().equals(ADMIN_ROLE))
            showForm(new AdminSetupUI());
        else if (m_User.getRole().toLowerCase().equals(THERAPIST_ROLE))
            showForm(new GUITable());
        else if (m_User.getRole().toLowerCase().equals(USER_ROLE))
            showForm(new TestSessionGui());
    }

}
