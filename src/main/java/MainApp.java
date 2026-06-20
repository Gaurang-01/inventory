import javax.swing.*;
import java.awt.event.*;

public class MainApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Stationary Management System");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Insert Button
        JButton insertBtn = new JButton("Insert Customer");
        insertBtn.setBounds(100, 50, 200, 40);
        frame.add(insertBtn);

        // View Button
        JButton viewBtn = new JButton("View Customers");
        viewBtn.setBounds(100, 100, 200, 40);
        frame.add(viewBtn);

        // Exit Button
        JButton exitBtn = new JButton("Exit");
        exitBtn.setBounds(100, 150, 200, 40);
        frame.add(exitBtn);

        // Exit action
        exitBtn.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }
}
