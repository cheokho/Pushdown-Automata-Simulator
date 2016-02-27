package com.application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * Created by CheokHo on 23/02/2016.
 */
public class AlgorithmRunner {

    private RunSimGUI runSimGUI;
    private ArrayList<Node> nodeArray;
    private ArrayList<Edge> edgeArray;
    private DefaultTableModel model;
    private JTextArea textArea;
    private SwingWorker<Void, Void> worker;
    private boolean choicePointFound;

    public AlgorithmRunner(RunSimGUI runSimGUI, DefaultTableModel model, ArrayList<Node> nodeArray, ArrayList<Edge> edgeArray, JTextArea textArea, SwingWorker<Void, Void> worker) {
        this.runSimGUI=runSimGUI;
        this.model=model;
        this.nodeArray=nodeArray;
        this.edgeArray=edgeArray;
        this.textArea=textArea;
        this.worker=worker;
        choicePointFound=false;
    }

    //basically I need a check which can detect that there is a choice point in the algorithm. If there is, the runalgorithm method needs to be recalled using the 2nd choice.
    public void runAlgorithm(boolean isNdpda) {

        worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {

                Node node = null;
                for (Node n: nodeArray) {
                    if (n.isInitial) {
                        node=n;
                        break;
                    }
                }
                String inputElements = runSimGUI.getInput();
                Edge transitionEdge=null;
                boolean isStuck=false;
                while (inputElements != null && !inputElements.equals("") && node!=null && !isStuck) {
                    ArrayList<String> stackArray = new ArrayList<String>();
                    for (int q=0; q<model.getRowCount(); q++) {
                        stackArray.add(model.getValueAt(q, 0).toString());
                    }
                    String input = inputElements.substring(0,1);
//                                System.out.println("node outgoing combos: "+node.getOutGoingCombo());
                    if (!isNdpda) {
                        for (Edge edge : edgeArray) {
                            //edgeTopInputAndStack = edge.getEdgeTopInput() + edge.getEdgeTopStack();
                            if (node.toString().equals(edge.getFromNode().toString())) {
//                                        System.out.println("Outgoing edges from:" +node.toString());
//                                        System.out.println("Edges:"+edge.toString());
                                if (edge.getEdgeTopStack().equals(stackArray.get(0)) && edge.getEdgeTopInput() == Integer.parseInt(input)) {
                                    transitionEdge = edge;
                                    break;
                                }
                            }
                        }
                        // TODO make NDPDA work.
                    } else if (isNdpda) {
                        System.out.println("Entered NDPDA if statement.");
                        ArrayList<Edge> transitionEdges=new ArrayList<Edge>(); //used for NDPDA
                            for (Edge edge : edgeArray) {
                                if (node.toString().equals(edge.getFromNode().toString())) {
                                    if (edge.getEdgeTopStack().equals(stackArray.get(0)) && edge.getEdgeTopInput() == Integer.parseInt(input)) {
                                        transitionEdges.add(edge);
                                    }
                                }
                            }
                            if (transitionEdges.size() >= 1) {
                                if (transitionEdges.size() > 1) { //choice point.
                                    textArea.append("Choice point found. Selecting first path found.\n");
                                    choicePointFound=true;
                                }
                                transitionEdge = transitionEdges.remove(0);
                            } else if (transitionEdges.isEmpty() ) {
                                textArea.append("RESULT: FAILURE. Simulation stuck at node: '"+node.toString()+"', no transition path found.\n");
                                isStuck = true;
                                break;
                            }

                    }
                    String transitionOperation="";
                    if (transitionEdge != null) {
                        transitionOperation = transitionEdge.getTransitionOperation();
                    }
                    if (transitionOperation.contains("push")) {
                        String stackCharacter = transitionOperation.substring(transitionOperation.lastIndexOf("(")+1, transitionOperation.lastIndexOf(")"));
                        model.insertRow(0, new String[]{stackCharacter});
                        model.fireTableDataChanged();

                    } else if (transitionOperation.contains("pop")) {
                        model.removeRow(0);
                        model.fireTableDataChanged();

                    } else if (transitionOperation.contains("do nothing")) {
                        //do nothing lol
                    }
                    node = transitionEdge.getToNode();
                    inputElements = inputElements.substring(1);
                    Thread.sleep(1000);
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                    textArea.append("Moving to node: '"+transitionEdge.getToNode()+"' from node: '"+transitionEdge.getFromNode()+"' through transition rule: "+transitionEdge.toString()+"\n");
                    if (inputElements.equals("")) {
                        textArea.append("Simulation finished at node: '"+transitionEdge.getToNode()+"'.\n");
                        if(transitionEdge.getToNode().isAccept()) {
                            textArea.append("RESULT: SUCCESS. '"+transitionEdge.getToNode()+"' is an accepting state.\n");
                        } else {
                            textArea.append("RESULT: FAILURE. '"+transitionEdge.getToNode()+"' is not an accepting state.\n");
                        }
                    }
                    else {
                        textArea.append("Current input elements: " + inputElements + "\n");
                    }
                }

                return null;
            }
        };
        worker.execute();
    }

    public boolean choicePointFound() {
        return choicePointFound;
    }
}
