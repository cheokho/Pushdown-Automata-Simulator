package com.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by CheokHo on 26/01/2016.
 */
public class PDAVersionGUI extends JFrame {

    private JLabel label;
    private JRadioButton ndpda;
    private JRadioButton dpda;
    private ButtonGroup group;
    private GridLayout buttonLayout;
    private JPanel buttonPanel;
    private JPanel continuePanel;
    private JButton continueButton;
    private boolean isNdpda;
    private PDAInGUI pdaInputGUI;
    private TopLevelGUI topLevelGUI;

    public PDAVersionGUI(TopLevelGUI topLevelGUI) {
        super("Create new PDA");
        this.topLevelGUI=topLevelGUI;
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
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dpda.isSelected()) {
                    isNdpda = false;
                    pdaInputGUI = new PDAInGUI(isNdpda, topLevelGUI);
                    dispose();
                } else if (ndpda.isSelected()) {
                    isNdpda = true;
                    pdaInputGUI = new PDAInGUI(isNdpda, topLevelGUI);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(new JPanel(), "Please select a PDA type before continuing.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel = new JPanel();
        continuePanel = new JPanel();
        continuePanel.setLayout(new FlowLayout());
        continuePanel.add(continueButton);
        buttonLayout = new GridLayout(2,1);
        buttonPanel.setLayout(buttonLayout);
        buttonPanel.add(dpda); buttonPanel.add(ndpda);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0,5,10,5));
        group = new ButtonGroup();
        group.add(ndpda); group.add(dpda);
        add(label, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(continuePanel, BorderLayout.SOUTH);

    }

    public boolean isNdpda() {
        return isNdpda;
    }

    public PDAInGUI getPdaInputGUI() {
        return pdaInputGUI;
    }
}
