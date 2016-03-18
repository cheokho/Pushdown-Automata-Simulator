package com.application;

import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by Cheeeky on 13/02/2016.
 */
public class TransitionRuleGUI extends JDialog {

    private ArrayList<String> stackArray;
    private ArrayList<String> inputArray;
    private ArrayList<String> pushOperation;
    private ArrayList<Edge> edgeArray;
    private ArrayList<GraphNode> nodeArray;
    private TopLevelGUI topLevelGUI;
    private boolean isNdpda;

    private JPanel centerPanel;
    private JLabel inputLabel;
    private JComboBox<String> inputComboBox;
    private JLabel stackLabel;
    private JComboBox<String> stackComboBox;
    private JLabel transitionLabel;
    private String toNodeString;
    private String fromNodeString;

    private JPanel botPanel;

    private ButtonGroup group;
    private JRadioButton pushButton;
    private JRadioButton popButton;
    private JRadioButton nothingButton;

    private JButton okButton;

//    private Edge edge = null;


    public TransitionRuleGUI(TopLevelGUI topLevelGUI, String fromNodeString, String toNodeString, ArrayList<String> stackArray, ArrayList<String> inputArray, boolean isNdpda) {

        this.toNodeString=toNodeString;
        this.fromNodeString=fromNodeString;
        setTitle("Transition rule from '" + fromNodeString + "' to '" + toNodeString + "'.");
        setIconImage(new ImageIcon(getClass().getResource("/com/icon/favicon.png")).getImage());
        this.stackArray = stackArray;
        this.inputArray = inputArray;
        this.topLevelGUI = topLevelGUI;
        this.isNdpda = isNdpda;
        edgeArray = topLevelGUI.getEdgeArray();
        nodeArray = topLevelGUI.getNodeArray();
        createTransitionGUI();
        pack();
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void createTransitionGUI() {
        setLayout(new BorderLayout());
        centerPanel = new JPanel();
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        centerPanel.setLayout(new GridLayout(4, 2, 0, 10));
        inputLabel = new JLabel("If input symbol=");
        inputComboBox = new JComboBox<>();
        inputComboBox.setModel(new DefaultComboBoxModel(inputArray.toArray()));
        pushOperation = new ArrayList<String>();

        stackLabel = new JLabel("and top of stack symbol=");
        stackComboBox = new JComboBox<>();
        stackComboBox.setModel(new DefaultComboBoxModel(stackArray.toArray()));

        transitionLabel = new JLabel("then perform the following operation:");
        pushButton = new JRadioButton("push(__)");
        pushButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (String s : stackArray) {
                    if (!s.equals("$")) {
                        s = "push(" + s + ")";
                        pushOperation.add(s);
                    }
                }
                String s = (String) JOptionPane.showInputDialog(null, "", "Select push operation", JOptionPane.QUESTION_MESSAGE, null, pushOperation.toArray(), pushOperation.get(0));

                if ((s != null) && (s.length() > 0)) {
                    pushButton.setText(s);

                }
                pushOperation.clear();
            }
        });

        popButton = new JRadioButton("pop");
        nothingButton = new JRadioButton("e");
        group = new ButtonGroup();
        group.add(pushButton);
        group.add(popButton);
        group.add(nothingButton);
        nothingButton.setSelected(true);

        FlowLayout botLayout = new FlowLayout();
        botPanel = new JPanel();
        botPanel.setLayout(botLayout);
        botPanel.add(pushButton);
        botPanel.add(popButton);
        botPanel.add(nothingButton);

        centerPanel.add(inputLabel);
        centerPanel.add(inputComboBox);
        centerPanel.add(stackLabel);
        centerPanel.add(stackComboBox);
        centerPanel.add(transitionLabel);
        centerPanel.add(botPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        okButton = new JButton("OK");
        buttonPanel.add(okButton);

        //create Edge object here with transition rule defined.

        //TODO loop through Edge array (get from top level GUI) and match with the edge rule that is trying to be created for NPDA.
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCombo = inputComboBox.getSelectedItem().toString() + stackComboBox.getSelectedItem().toString();
                String edgeRule = "{" + inputComboBox.getSelectedItem().toString() + ", " + stackComboBox.getSelectedItem().toString() + ", " + getSelectedButtonText(group) + "}";

                System.out.println("Selected input-stack combo: " + selectedCombo);
                for (GraphNode n : topLevelGUI.getNodeArray()) {
                    if (n.toString().equals(topLevelGUI.getCellPressed().getValue().toString())) {
                        //System.out.println("Node outgoing combo: " + n.getOutGoingCombo());

                        System.out.println(n.getOutGoingEdgeRule());
                        System.out.println(edgeRule);
                        System.out.println((!n.getOutGoingEdgeRule().contains(edgeRule)));
                        System.out.println(n.getToFromCombo());
                        System.out.println(!n.getToFromCombo().contains(toNodeString+fromNodeString));

                        if (((!n.getOutGoingEdgeRule().contains(edgeRule)) || (!n.getToFromCombo().contains(toNodeString+fromNodeString))) && isNdpda) {
                            if (pushButton.getText().equals("push(__)") && pushButton.isSelected()) {
                                JOptionPane.showMessageDialog(getContentPane(), "Please select a valid push operation.", "Invalid operation", JOptionPane.ERROR_MESSAGE);
                            } else if (popButton.isSelected() && stackComboBox.getSelectedItem().equals("$")) {
                                JOptionPane.showMessageDialog(getContentPane(), "You cannot pop from empty stack $.", "Invalid operation", JOptionPane.ERROR_MESSAGE);
                            } else {
                                topLevelGUI.addEdge(topLevelGUI.getGraph(), edgeRule, topLevelGUI.getCellPressed(), topLevelGUI.getCellReleased(), Integer.parseInt(inputComboBox.getSelectedItem().toString()), stackComboBox.getSelectedItem().toString(), getSelectedButtonText(group));

                                for (GraphNode node : nodeArray) {
                                    if (node.toString().equals(fromNodeString)) {
                                        if (topLevelGUI.getEdge() != null) {
                                            node.addOutgoingInput(Integer.toString(topLevelGUI.getEdge().getEdgeTopInput()));
                                            node.addOutgoingTopStack(topLevelGUI.getEdge().getEdgeTopStack());
                                            node.addOutgoingCombo(Integer.toString(topLevelGUI.getEdge().getEdgeTopInput()) + topLevelGUI.getEdge().getEdgeTopStack());
                                            node.addOutgoingEdgeRule(topLevelGUI.getEdge().toString());
                                            node.addToFromCombo(toNodeString + fromNodeString);
                                        }
                                    }
                                }
                                dispose();
                            }
                        } else if (isNdpda) {
                            JOptionPane.showMessageDialog(getContentPane(), "You have already specified this rule!", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                        if (!n.getOutGoingCombo().contains(selectedCombo) && !isNdpda) {
                            if (pushButton.getText().equals("push(__)") && pushButton.isSelected()) {
                                JOptionPane.showMessageDialog(getContentPane(), "Please select a valid push operation.", "Invalid operation", JOptionPane.ERROR_MESSAGE);
                            } else if (popButton.isSelected() && stackComboBox.getSelectedItem().equals("$")) {
                                JOptionPane.showMessageDialog(getContentPane(), "You cannot pop from empty stack $.", "Invalid operation", JOptionPane.ERROR_MESSAGE);
                            } else {
                                topLevelGUI.addEdge(topLevelGUI.getGraph(), edgeRule, (Object) topLevelGUI.getCellPressed(), topLevelGUI.getCellReleased(), Integer.parseInt(inputComboBox.getSelectedItem().toString()), stackComboBox.getSelectedItem().toString(), getSelectedButtonText(group));
//                                GraphNode fromNode = null;
//                                GraphNode toNode = null;
//                                for (GraphNode node : topLevelGUI.getNodeArray()) {
//                                    if (node.toString().equals(topLevelGUI.getCellPressed().getValue().toString())) {
//                                        fromNode = node;
//                                    }
//                                    if (node.toString().equals(topLevelGUI.getCellReleased().getValue().toString())) {
//                                        toNode = node;
//                                    }
//
//                                }
//                                edge = new Edge(edgeRule);
//                                edge.setFromNode(fromNode);
//                                edge.setToNode(toNode);
//                                edge.setEdgeTopInput(Integer.parseInt(inputComboBox.getSelectedItem().toString()));
//                                edge.setEdgeTopStack(stackComboBox.getSelectedItem().toString());
//                                edge.setTransitionOperation(getSelectedButtonText(group));
//
//                                edgeArray.add(edge);
//                                System.out.println("Edge from: " + edge.getFromNode() + " Edge to: " + edge.getToNode());
//                                System.out.println("Updated edge array: " + edgeArray);
                                dispose();
                            }
                        } else if (!isNdpda) {
                            JOptionPane.showMessageDialog(getContentPane(), "For deterministic PDA, you must specify one rule for each input/stack combination only.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        add(centerPanel, BorderLayout.NORTH);
        add(botPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }

//    public Edge getEdge() {
//        return edge;
//    }

//    public ArrayList<Edge> getEdgeArray() {
//        return edgeArray;
//    }

}
