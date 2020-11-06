package presentation;

import logic.User;
import main.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Kit Mitchell
 * @version 2018.11.09
 */
public class RegisterForm extends GUIForm{
    JPanel rootPanel;
    JTextField nameText;
    JTextField emailText;
    JPasswordField passwordText;
    JButton okButton;
    JButton backButton;
    private JPasswordField rePasswordText;

    public RegisterForm(User user) {
        String name = user.getUserName();
        String email = user.getEmail();
        char[] password = user.getPassword();

        nameText.setText(name);
        emailText.setText(email);
        passwordText.setText(String.valueOf(password));

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!User.validateBlankString(nameText.getText()) ||
                        !User.validateBlankString(emailText.getText()) ||
                        !User.validateBlankChar(passwordText.getPassword()) ||
                        !User.validateBlankChar(rePasswordText.getPassword())) {
                    JFrame frame = new JFrame("Login error");
                    JOptionPane.showMessageDialog(frame, "No lines can be left blank.");
                } else if (!User.validatePasswordForRegisterGUI(passwordText.getPassword(), rePasswordText.getPassword())){
                    JFrame frame = new JFrame("Login error");
                    JOptionPane.showMessageDialog(frame, "Passwords do not match.");
                } else {
                    User user = User.register(
                            nameText.getText(),
                            emailText.getText(),
                            passwordText.getPassword()
                    );
                    if(user !=null) {
                        Controller.setUser(user);
                        System.out.println("Logged in: ID: " + user.getUserID() +
                        ", Email: " + user.getEmail() + ", Name: " + user.getUserName() +
                        ", Role: " + user.getRole());
                        Controller.login();
                    } else {
                        JFrame frame = new JFrame("Login error");
                        JOptionPane.showMessageDialog(frame, "Email already in system.");
                    }
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Controller.setUser(new User(-1, nameText.getText(), emailText.getText(), passwordText.getPassword(), ""));
                Controller.showLogin();
            }
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
