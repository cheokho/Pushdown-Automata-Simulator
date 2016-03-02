package com.application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by CheokHo on 23/02/2016.
 */
public class AlgorithmRunner {

    private RunSimGUI runSimGUI;
    private ArrayList<Node> nodeArray;
    private ArrayList<Edge> edgeArray;
    private DefaultTableModel model;
    private JTextArea textArea;
    private SwingWorker<Boolean, Void> worker;
    private boolean choicePointFound;

    ArrayList<String> pathGenerator;

    public AlgorithmRunner(RunSimGUI runSimGUI, DefaultTableModel model, ArrayList<Node> nodeArray, ArrayList<Edge> edgeArray, JTextArea textArea, SwingWorker<Boolean, Void> worker) {
        this.runSimGUI=runSimGUI;
        this.model=model;
        this.nodeArray=nodeArray;
        this.edgeArray=edgeArray;
        this.textArea=textArea;
        this.worker=worker;

        pathGenerator = new ArrayList<String>();
    }

    //same rule but different TONODE needs fixing too.
    //basically I need a check which can detect that there is a choice point in the algorithm. If there is, the runalgorithm method needs to be recalled using the 2nd choice.
    public void runAlgorithm(boolean isNdpda) {

        worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {

                StringBuilder totalPath = new StringBuilder();
                Node node = null;
                for (Node n: nodeArray) {
                    if (n.isInitial) {
                        node=n;
                        totalPath.append(n.toString());
                        break;
                    }
                }
                String inputElements = runSimGUI.getInput();
                Edge transitionEdge=null;
                boolean isStuck=false;

                PathFinder pathFinder = new PathFinder();
                pathFinder.setCurrentInput(inputElements);
                pathFinder.setToNode(node);
                pathFinder.setTableModel(model);

                ArrayList<PathFinder> pathFinders = new ArrayList<PathFinder>();
                pathFinders.add(pathFinder);


                while (inputElements != null && !inputElements.equals("") && node!=null && !isStuck && !pathFinders.isEmpty()) {
                    ArrayList<String> stackArray = new ArrayList<String>();
                    for (int q=0; q<model.getRowCount(); q++) {
                        stackArray.add(model.getValueAt(q, 0).toString());
                    }
                    String input = pathFinders.get(0).getCurrentInput().substring(0, 1);
//                                System.out.println("node outgoing combos: "+node.getOutGoingCombo());
                    if (!isNdpda) {
                        input=inputElements.substring(0,1);
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
                        totalPath.append(transitionEdge.getToNode());
                        if (inputElements.equals("")) {
                            textArea.append("Simulation finished at node: '"+transitionEdge.getToNode()+"'.\nPath taken: '"+totalPath.toString()+"'.\n");
                            if(transitionEdge.getToNode().isAccept()) {
                                textArea.append("RESULT: SUCCESS. '"+transitionEdge.getToNode()+"' is an accepting state.\n");
                            } else {
                                textArea.append("RESULT: FAILURE. '"+transitionEdge.getToNode()+"' is not an accepting state.\n");
                            }
                        }
                        else {
                            textArea.append("Current input elements: " + inputElements + "\n");
                        }

                        // TODO
                        // PathFinder object stores a full path into an ArrayList until input elements =""
                        // PathFinder realises when there is choice point. Creates a new PathFinder for each choice point.
                        // Search all paths first and get a list of them. Let user choose which ones to run??
                        // Run transitionEdge calculator on all transitionEdges it finds. (transitionEdge.get(i)
                        // update this for all PathFinder objects.


                    } else if (isNdpda) {
                        //System.out.println("Entered NDPDA if statement.");

                    }
                }

                return choicePointFound;
            }

            @Override

            protected void done() {

                //recursion for swingworker.
                try {
                    Boolean status = get();
                    if(status) {
                        //runAlgorithm(PDAVersionGUI.isNdpda);
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        };
        worker.execute();
    }

    public void ndpdaAlgorithm(String inputElements, Node node, StringBuilder path) {

        ArrayList<Edge> transitionEdges = new ArrayList<Edge>();
        ArrayList<String> stackArray = new ArrayList<String>();
        Edge transitionEdge=null;


        for (int q=0; q<model.getRowCount(); q++) {
            stackArray.add(model.getValueAt(q, 0).toString());
        }

        if (inputElements!=null && !inputElements.equals("") && node!=null) {
            String input=inputElements.substring(0,1);
            inputElements = inputElements.substring(1);

            for (Edge edge : edgeArray) {
                //edgeTopInputAndStack = edge.getEdgeTopInput() + edge.getEdgeTopStack();
                if (node.toString().equals(edge.getFromNode().toString())) {
//                                        System.out.println("Outgoing edges from:" +node.toString());
//                                        System.out.println("Edges:"+edge.toString());
                    if (edge.getEdgeTopStack().equals(stackArray.get(0)) && edge.getEdgeTopInput() == Integer.parseInt(input)) {
                        transitionEdges.add(edge);
                    }
                }
            }

            if (transitionEdges.size() == 1) {
                System.out.println("Single outgoing edge");
                transitionEdge = transitionEdges.get(0);

            } else {
                System.out.println("Many outgoing edge");

                for (int i = 0; i < transitionEdges.size(); i++) {
                    transitionEdge = transitionEdges.get(i);
                    node = transitionEdge.getToNode();

                    pathGenerator.add((path + node.toString()));

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
                    ndpdaAlgorithm(inputElements, node, path);
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

            if (transitionEdge!=null) {
                node = transitionEdge.getToNode();
                path.append(node.toString());
                ndpdaAlgorithm(inputElements, node, path);
            }

        }
        if (!pathGenerator.contains(path.toString())) {
            pathGenerator.add(path.toString());
        }


//        Thread.sleep(1000);
//        textArea.setCaretPosition(textArea.getDocument().getLength());
//        textArea.append("Moving to node: '"+transitionEdge.getToNode()+"' from node: '"+transitionEdge.getFromNode()+"' through transition rule: "+transitionEdge.toString()+"\n");
//        totalPath.append(transitionEdge.getToNode());
//        if (inputElements.equals("")) {
//            textArea.append("Simulation finished at node: '"+transitionEdge.getToNode()+"'.\nPath taken: '"+totalPath.toString()+"'.\n");
//            if(transitionEdge.getToNode().isAccept()) {
//                textArea.append("RESULT: SUCCESS. '"+transitionEdge.getToNode()+"' is an accepting state.\n");
//            } else {
//                textArea.append("RESULT: FAILURE. '"+transitionEdge.getToNode()+"' is not an accepting state.\n");
//            }
//        }
//        else {
//            textArea.append("Current input elements: " + inputElements + "\n");
//        }
    }

    public ArrayList<String> getPathGenerator() {
        return pathGenerator;
    }

    public boolean choicePointFound() {
        return choicePointFound;
    }

}
