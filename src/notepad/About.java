package notepad;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author manish
 */
public class About extends JFrame implements ActionListener {

    About() {
        setTitle("About Notepad");
        setBounds(500, 100, 600, 500);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // Windows Icon
        ImageIcon il = new ImageIcon(ClassLoader.getSystemResource("notepad/icons/windows.png"));
        Image img = il.getImage().getScaledInstance(300, 70, Image.SCALE_SMOOTH);
        JLabel headerIcon = new JLabel(new ImageIcon(img));
        headerIcon.setBounds(150, 20, 300, 70);
        add(headerIcon);

        // Notepad Icon
        ImageIcon il1 = new ImageIcon(ClassLoader.getSystemResource("notepad/icons/notepad.png"));
        Image img2 = il1.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        JLabel icon = new JLabel(new ImageIcon(img2));
        icon.setBounds(260, 100, 70, 70);
        add(icon);

        // About Text using JTextPane for proper wrapping
        JTextPane textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setText("<html>" +
                "<h2 style='color: #2e86c1;'>Notepad Clone</h2>" +
                "<p style='font-size: 14px;'>This is a simple Notepad application built in Java using Swing. " +
                "It supports basic text editing features such as Cut, Copy, Paste, Undo, Redo, Zoom, and Dark Mode.</p>" +
                "</html>");
        textPane.setBounds(100, 180, 400, 150);
        textPane.setFont(new Font("Arial", Font.PLAIN, 14));
        textPane.setEditable(false);
        textPane.setOpaque(false);
        add(textPane);

        // OK Button
        JButton button = new JButton("OK");
        button.setBounds(240, 400, 120, 30);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(30, 144, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(this);
        add(button);

        setResizable(false);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        this.dispose();
    }

    public static void main(String... args) {
        new About();
    }
}
