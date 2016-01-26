package com.application;

import javax.swing.*;
import java.awt.*;

/**
 * Created by CheokHo on 26/01/2016.
 */
public class pdaInputGUI extends JFrame {

    private JPanel centerPanel;
    private JPanel southPanel;
    private JPanel centerBotPanel;
    private JLabel info;
    private JLabel info2;
    private JLabel states;
    private JTextField statesField;
    private JLabel acceptStates;
    private JTextField acceptStatesField;
    private JLabel initState;
    private JTextField initStateField;
    private JRadioButton graph;
    private JRadioButton transTable;
    private ButtonGroup group;
    private JButton next;


    public pdaInputGUI(boolean isNdpda) {
        super("Input PDA");
        createpdaInput();
        setSize(500, 250);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);

    }

    public void createpdaInput(){
        setLayout(new BorderLayout());
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 2, 0, 10));
        info = new JLabel("Separate a list of states using a space between each state.");
        states = new JLabel("States:");
        statesField = new JTextField();

        acceptStates = new JLabel("Accepting state(s):");
        acceptStatesField = new JTextField();

        initState = new JLabel("Initial state:");
        initStateField = new JTextField();

        info2 = new JLabel("Specify transition rules through:");
        graph = new JRadioButton("Graph");
        transTable = new JRadioButton("Transition table");
        group = new ButtonGroup();
        group.add(graph); group.add(transTable);
        BorderLayout centerBotLayout = new BorderLayout();
        centerBotPanel = new JPanel();
        centerBotPanel.setLayout(centerBotLayout);
        centerBotPanel.add(info2, BorderLayout.NORTH); centerBotPanel.add(graph, BorderLayout.CENTER); centerBotPanel.add(transTable, BorderLayout.SOUTH);

        centerPanel.add(states); centerPanel.add(statesField);
        centerPanel.add(acceptStates); centerPanel.add(acceptStatesField);
        centerPanel.add(initState); centerPanel.add(initStateField);
        centerPanel.add(centerBotPanel);

        next = new JButton("Next");
        southPanel= new JPanel();
        southPanel.setLayout(new FlowLayout());
        southPanel.add(next);

        add(info, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }
}
