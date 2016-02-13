package com.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Cheeeky on 13/02/2016.
 */
public class TransitionRuleGUI extends JFrame {

    private ArrayList<String> stackArray;
    private ArrayList<String> inputArray;

    private JPanel centerPanel;
    private JLabel inputLabel;
    private JComboBox<String> inputComboBox;
    private JLabel stackLabel;
    private JComboBox<String> stackComboBox;
    private JLabel transitionLabel;

    private JPanel botPanel;

    private ButtonGroup group;
    private JRadioButton pushButton;
    private JRadioButton popButton;
    private JRadioButton nothingButton;

    private JButton okButton;

    public TransitionRuleGUI(String fromNode, String toNode, ArrayList<String> stackArray, ArrayList<String> inputArray) {
        super("Transition rule from '"+fromNode+"' to '"+toNode+"'.");
        this.stackArray=stackArray;
        this.inputArray=inputArray;
        createTransitionGUI();
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    public void createTransitionGUI() {
        setLayout(new BorderLayout());
        centerPanel=new JPanel();
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        centerPanel.setLayout(new GridLayout(4, 2, 0, 10));
        inputLabel = new JLabel("If input symbol=");
        inputComboBox = new JComboBox<>();

        stackLabel=new JLabel("and top of stack symbol=");
        stackComboBox = new JComboBox<>();

        transitionLabel=new JLabel("then perform the following operation:");
        pushButton = new JRadioButton("push(__)");
        popButton = new JRadioButton("pop");
        nothingButton = new JRadioButton("ε (do nothing)");
        group = new ButtonGroup();
        group.add(pushButton); group.add(popButton); group.add(nothingButton);
        nothingButton.setSelected(true);

        FlowLayout botLayout = new FlowLayout();
        botPanel = new JPanel();
        botPanel.setLayout(botLayout);
        botPanel.add(pushButton); botPanel.add(popButton); botPanel.add(nothingButton);

        centerPanel.add(inputLabel); centerPanel.add(inputComboBox);
        centerPanel.add(stackLabel); centerPanel.add(stackComboBox);
        centerPanel.add(transitionLabel);
        centerPanel.add(botPanel);

        okButton = new JButton("OK");
        //create Edge object here with transition rule defined.
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        add(centerPanel, BorderLayout.CENTER);
        add(botPanel, BorderLayout.SOUTH);
    }
}
