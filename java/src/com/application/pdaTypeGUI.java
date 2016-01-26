package com.application;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by CheokHo on 26/01/2016.
 */
public class pdaTypeGUI extends JFrame {

    private JLabel label;
    private JRadioButton ndpda;
    private JRadioButton dpda;
    private ButtonGroup group;
    private GridLayout buttonLayout;
    private JPanel buttonPanel;
    private JButton continueButton;

    public pdaTypeGUI() {
        super("Create new PDA");
        setLayout(new BorderLayout());
        createpdaType();
        setSize(300, 150);
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    public void createpdaType() {
        label = new JLabel("Select PDA you wish to create:");
        label.setBorder(BorderFactory.createEmptyBorder(10,5,10,5));
        ndpda = new JRadioButton("Nondeterministic");
        dpda = new JRadioButton("Deterministic");
        continueButton = new JButton("Continue");
        buttonPanel = new JPanel();
        buttonLayout = new GridLayout(2,1);
        buttonPanel.setLayout(buttonLayout);
        buttonPanel.add(dpda); buttonPanel.add(ndpda);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0,5,10,5));
        group = new ButtonGroup();
        group.add(ndpda); group.add(dpda);
        add(label, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(continueButton, BorderLayout.SOUTH);

    }
}
