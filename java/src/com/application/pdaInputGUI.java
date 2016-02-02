package com.application;

import javax.smartcardio.Card;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Created by CheokHo on 26/01/2016.
 */
public class pdaInputGUI extends JFrame {

    private JPanel inputPDA; //card 1
    private JPanel defPDA; //card 2
    private JPanel container; //container for card

    //==input PDA components==//
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

    //--def PDA components==//
    private JPanel centerPanel1;
    private JPanel southPanel1;
    private JLabel info3;
    private JLabel stackAlp;
    private JTextField stackField;
    private JLabel inputAlp;
    private JTextField inputField;
    private JButton done;
    private JButton previous;

    public pdaInputGUI(boolean isNdpda) {
        super("Input PDA");
        createpdaInput();
        setSize(500, 250);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);

    }

    public void createpdaInput(){
        //setLayout(new BorderLayout());
        container = new JPanel();
        CardLayout cl = new CardLayout();
        container.setLayout(cl);
        inputPDA = new JPanel();
        defPDA = new JPanel();


        inputPDA.setLayout(new BorderLayout());
        centerPanel = new JPanel();
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        centerPanel.setLayout(new GridLayout(4, 2, 0, 10));
        info = new JLabel("Separate a list of states using a space between each state.");
        info.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
        states = new JLabel("States:");
        statesField = new JTextField();
//        statesField.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                Document doc = (Document) e.getDocument();
//                String stateString = doc.toString();
//                String[] states = stateString.split("\\s+");
//
//                for (String s : states) {
//                    System.out.println("STATELOOP:" + s);
//                }
//            }
//        });
        statesField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                String stateString = statesField.getText();
                String[] statesArray = stateString.split("\\s+");

                for (String s: statesArray){
                    System.out.println("STATELOOP:"+s);
                }
            }
        });

        acceptStates = new JLabel("Accepting state(s):");
        acceptStatesField = new JTextField();

        initState = new JLabel("Initial state:");
        initStateField = new JTextField();

        info2 = new JLabel("Specify transition rules through:");
        graph = new JRadioButton("Graph");
        transTable = new JRadioButton("Transition table");
        group = new ButtonGroup();
        group.add(graph);
        group.add(transTable);
        BorderLayout centerBotLayout = new BorderLayout();
        centerBotPanel = new JPanel();
        centerBotPanel.setLayout(centerBotLayout);
        centerBotPanel.add(info2, BorderLayout.NORTH); centerBotPanel.add(graph, BorderLayout.CENTER); centerBotPanel.add(transTable, BorderLayout.SOUTH);

        centerPanel.add(states); centerPanel.add(statesField);
        centerPanel.add(acceptStates); centerPanel.add(acceptStatesField);
        centerPanel.add(initState);
        centerPanel.add(initStateField);
        centerPanel.add(centerBotPanel);


        next = new JButton("Next");
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (statesField.getText().equals("")) {
                    JOptionPane.showMessageDialog(new JPanel(), "States field empty or invalid format.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                if (acceptStatesField.getText().equals("")) {
                    JOptionPane.showMessageDialog(new JPanel(), "Accepting state(s) field empty or invalid format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                if (initStateField.getText().equals("")) {
                    JOptionPane.showMessageDialog(new JPanel(), "Initial state field empty or invalid format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    cl.show(container, "2");
                }
            }
        });
        southPanel= new JPanel();
        southPanel.setLayout(new FlowLayout());
        southPanel.add(next);

        inputPDA.add(info, BorderLayout.NORTH);
        inputPDA.add(centerPanel, BorderLayout.CENTER);
        inputPDA.add(southPanel, BorderLayout.SOUTH);

        //==========DEF PDA STARTS HERE===============//
        defPDA.setLayout(new BorderLayout());
        centerPanel1 = new JPanel();
        centerPanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        centerPanel1.setLayout(new GridLayout(2, 2, 0, 10));
        info3 = new JLabel("Separate characters with a space. There must be no clashes between the stack and the input alphabets.");
        info3.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));

        stackAlp = new JLabel("Stack Alphabet:");
        stackField = new JTextField();

        inputAlp = new JLabel("Input Alphabet:");
        inputField = new JTextField();

        centerPanel1.add(stackAlp); centerPanel1.add(stackField); centerPanel1.add(inputAlp); centerPanel1.add(inputField);

        done = new JButton("Done");
        previous = new JButton("Previous");

        southPanel1= new JPanel();
        southPanel1.setLayout(new FlowLayout());
        southPanel1.add(previous);
        southPanel1.add(done);
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(container, "1");
            }
        });
        //defPDA.add(done);
        defPDA.add(info3, BorderLayout.NORTH);
        defPDA.add(centerPanel1, BorderLayout.CENTER);
        defPDA.add(southPanel1, BorderLayout.SOUTH);

        //==========CARD LAYOUT======================//
        add(container);
        container.add(inputPDA, "1");
        container.add(defPDA, "2");
        cl.show(container, "1");

    }
}
