package com.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by CheokHo on 26/01/2016.
 */
public class PDAInGUI extends JDialog {

    private JPanel inputPDA; //card 1
    private JPanel defPDA; //card 2
    private JPanel container; //container for card

    private String stateString;
    private String acceptString;
    private String stackString;
    private String inputString;

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

    private ArrayList<String> statesArray;
    private ArrayList<String> acceptStatesArray;
    private ArrayList<String> stackArray;
    private ArrayList<String> inputArray;
    private String initStateStr;
    private boolean isGraph;
    private boolean isNdpda;
    private TopLevelGUI topLevelGUI;


    public PDAInGUI(boolean isNdpda, TopLevelGUI topLevelGUI) {
        setTitle("Input PDA");
        this.isNdpda=isNdpda;
        this.topLevelGUI=topLevelGUI;
        createpdaInput();
        pack();
        setModal(true);
        setMinimumSize(new Dimension(500, 500));
        setLocationRelativeTo(null);
        setVisible(true);

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
        statesField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                stateString = statesField.getText();
                statesArray= new ArrayList(Arrays.asList(stateString.split("\\s+")));
                //String[] statesArray = stateString.split("\\s+");
//                for(int i=0;i<statesArray.size();i++)
//                {
//                    System.out.println(" -->"+statesArray.get(i));
//                }
                System.out.println("States Array: "+statesArray);
                Set<String> set = new HashSet<String>(statesArray);
                if(set.size()<statesArray.size()){ //duplicates found
                    JOptionPane.showMessageDialog(new JPanel(), "You have duplicate states specified.", "Error", JOptionPane.ERROR_MESSAGE);
                    next.setEnabled(false);
                } else {
                    next.setEnabled(true);
                }
            }
        });
        acceptStates = new JLabel("Accepting state(s):");
        acceptStatesField = new JTextField();

        acceptStatesField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                acceptString=acceptStatesField.getText();
                acceptStatesArray=new ArrayList(Arrays.asList(acceptString.split("\\s+")));
                if(statesArray.containsAll(acceptStatesArray)) { //checks if subset
                    next.setEnabled(true);
                } else {
                    //JOptionPane.showMessageDialog(new JPanel(), "Accepting state(s) is not a subset of your defined states.", "Error", JOptionPane.ERROR_MESSAGE);
                    next.setEnabled(false);
                }
            }
        });

        initState = new JLabel("Initial state:");
        initStateField = new JTextField();

        initStateField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                initStateField.getText().trim();
                initStateStr = initStateField.getText().replaceAll("\\s+", "");
                initStateField.setText(initStateStr);
                if (statesArray.contains(initStateStr)) {
                    next.setEnabled(true);
                } else {
                    //JOptionPane.showMessageDialog(new JPanel(), "Initial state is not one of your defined states.", "Error", JOptionPane.ERROR_MESSAGE);
                    next.setEnabled(false);
                }
            }
        });



        info2 = new JLabel("Specify transition rules through:");
        graph = new JRadioButton("Graph");
        transTable = new JRadioButton("Transition table");
        group = new ButtonGroup();
        group.add(graph);
        group.add(transTable);
        graph.setSelected(true);
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
                    if (graph.isSelected()) {
                        isGraph = true;
                    } else if (transTable.isSelected()) {
                        isGraph = false;
                    }
                    if(!statesArray.contains(initStateStr)){
                    } else {
                        cl.show(container,"2");
                    }
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
        info3 = new JLabel("Separate characters with a space. Use alphabetical letters for the stack and numbers for the input.");
        info3.setBorder(BorderFactory.createEmptyBorder(10,10,0,10));

        stackAlp = new JLabel("Stack Alphabet:");
        stackField = new JTextField();
        stackField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                stackString = stackField.getText();
                stackArray= new ArrayList(Arrays.asList(stackString.split("\\s+")));
                stackArray.add("$");
                System.out.println("Stack Array: "+stackArray);
            }
        });

        inputAlp = new JLabel("Input Alphabet:");
        inputField = new JTextField();
        inputField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                inputString = inputField.getText();
                inputArray= new ArrayList(Arrays.asList(inputString.split("\\s+")));
                System.out.println("Input Array: "+inputArray);
            }
        });

        centerPanel1.add(stackAlp); centerPanel1.add(stackField); centerPanel1.add(inputAlp); centerPanel1.add(inputField);

        done = new JButton("Done");
        previous = new JButton("Previous");

        southPanel1= new JPanel();
        southPanel1.setLayout(new FlowLayout());
        southPanel1.add(previous);
        southPanel1.add(done);

        done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //topLevelGUI.createNode(20,20, "a", false);
                Set<String> stackSet = new HashSet<String>(stackArray);
                Set<String> inputSet = new HashSet<String>(inputArray);
                if(stackField.getText().equals("") || inputField.getText().equals("")) {
                    JOptionPane.showMessageDialog(new JPanel(), "You haven't filled all the information!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(stackSet.size()<stackArray.size()) {
                    JOptionPane.showMessageDialog(new JPanel(), "You have duplicate stack characters specified.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(inputSet.size()<inputArray.size()) {
                    JOptionPane.showMessageDialog(new JPanel(), "You have duplicate input characters specified.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(!stackString.matches("^[a-zA-Z ]+$")) {
                    JOptionPane.showMessageDialog(new JPanel(), "Please use alphabetical letters in your stack only.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(!inputString.matches("^[0-9 ]+$")) {
                    JOptionPane.showMessageDialog(new JPanel(), "Please use numbers in your input only.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    int x=20;
                    int y=20;
                    for (int i=0; i<statesArray.size(); i++) {
                        if(!topLevelGUI.getNodeArray().contains(statesArray.get(i))) {
                            if (statesArray.get(i).equals(initStateStr) && !acceptStatesArray.contains(statesArray.get(i))) {
                                topLevelGUI.createNode(x, y, statesArray.get(i), false, true);
                            }
                            else if (statesArray.get(i).equals(initStateStr) && acceptStatesArray.contains(statesArray.get(i))) {
                                topLevelGUI.createNode(x, y, statesArray.get(i), true, true);
                            }
                            else if (acceptStatesArray.contains(statesArray.get(i)) && !statesArray.get(i).equals(initStateStr)) {
                                topLevelGUI.createNode(x, y, statesArray.get(i), true, false);
                            } else if (!acceptStatesArray.contains(statesArray.get(i)) && !statesArray.get(i).equals(initStateStr)){
                                topLevelGUI.createNode(x, y, statesArray.get(i), false, false);
                            }
                        }
                        x=x+100;
                        y=y+100;
                    }
                    System.out.println("Current node array(non delete): "+topLevelGUI.getNodeArray().toString());
                    dispose();
                }
            }
        });
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

    public boolean isGraph() {
        return isGraph;
    }

    public ArrayList<String> getStatesArray() {
        return statesArray;
    }

    public ArrayList<String> getAcceptStatesArray() {
        return acceptStatesArray;
    }

    public String getInitStateStr() {
        return initStateStr;
    }

    public ArrayList<String> getStackArray() {
        return stackArray;
    }

    public ArrayList<String> getInputArray() {
        return inputArray;
    }
}
