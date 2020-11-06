package presentation;

import logic.User;
import main.Controller;

import javax.swing.*;

/**
 * @author Kit Mitchell
 * @version 2018.11.09
 */
public class LoginForm extends GUIForm {
    JPanel rootPanel;
    JTextField emailText;
    JPasswordField passwordText;
    JButton okButton;
    JButton createNewAccountButton;

    public LoginForm(User user) {
        String email = user.getEmail();
        char[] password = user.getPassword();

        emailText.setText(email);
        passwordText.setText(String.valueOf(password));

        okButton.addActionListener(actionEvent -> {
            User m_User = User.login(emailText.getText(), passwordText.getPassword());
            if (m_User!=null) {
                Controller.setUser(m_User);
                System.out.println("Logged in: ID: " + m_User.getUserID() +
                        ", Email: " + m_User.getEmail() + ", Name: " + m_User.getUserName() +
                        ", Role: " + m_User.getRole());
                Controller.login();
            } else {
                JFrame frame = new JFrame("Login error");
                JOptionPane.showMessageDialog(frame, "Username or password incorrect.");
            }
        });

        createNewAccountButton.addActionListener(actionEvent -> {
            Controller.setUser(new User(-1, "", emailText.getText(), passwordText.getPassword(), null));
            Controller.showRegister();
        });
    }



    public JPanel getRootPanel() {
        return rootPanel;
    }
}
