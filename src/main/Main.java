package main;

import javax.swing.*;

/**
 * Main class to launch the application.
 *
 * @author (Brady McAllister)
 * @version (11.02.2018)
 *
 */
public class Main {

    /**
     * Runs the application.
     *
     * @param args application arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
           // UserLoginSignup.main.Controller controller = new UserLoginSignup.main.Controller();
            public void run() {
                Controller.start();
            }
        });
    }



}
