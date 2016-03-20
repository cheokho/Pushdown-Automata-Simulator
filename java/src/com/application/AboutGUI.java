package com.application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by CheokHo on 20/03/2016.
 */
public class AboutGUI extends JDialog {

    public AboutGUI() {
        setTitle("About");

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/com/icon/favicon.png"));
        JLabel imageLabel = new JLabel();
        imageLabel.setBorder(new EmptyBorder(10, 0, 5, 0));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setIcon(imageIcon);

        add(imageLabel, BorderLayout.CENTER);


        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        JLabel info = new JLabel("<html><center><b><font size=\"5\">Pushdown Automata Tool 1.0</font></b><br>Educational tool developed for simulation of deterministic and non-deterministic PDA.<br><br><font size=\"2\">Created by CheokHo Mai</font></center></html>", SwingConstants.CENTER);

        JButton ok = new JButton("Ok");
        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout());
        p3.add(ok);

        p2.add(info, BorderLayout.CENTER);
        p2.add(p3, BorderLayout.SOUTH);

        add(p2, BorderLayout.SOUTH);

        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        });

        setSize(350, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
