package com.application;

import com.algorithms.PathFinder;
import com.algorithms.PathGenerator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by CheokHo on 23/02/2016.
 */
public class AlgorithmRunner {

    private RunSimGUI runSimGUI;
    private ArrayList<GraphNode> nodeArray;
    private ArrayList<Edge> edgeArray;
    private DefaultTableModel model;
    private JTextArea textArea;
    private SwingWorker<Void, Void> worker;

    private Set<PathGenerator> pathGenerators;

    public AlgorithmRunner(RunSimGUI runSimGUI, DefaultTableModel model, ArrayList<GraphNode> nodeArray, ArrayList<Edge> edgeArray, JTextArea textArea, SwingWorker<Void, Void> worker) {
        this.runSimGUI=runSimGUI;
        this.model=model;
        this.nodeArray=nodeArray;
        this.edgeArray=edgeArray;
        this.textArea=textArea;
        this.worker=worker;

        pathGenerators = new LinkedHashSet<PathGenerator>();
    }

    public void runAlgorithm(boolean isNdpda) {

        worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {

                StringBuilder totalPath = new StringBuilder();
                GraphNode node = null;
                for (GraphNode n : nodeArray) {
                    if (n.isInitial) {
                        node = n;
                        totalPath.append(n.toString());
                        break;
                    }
                }
                String inputElements = runSimGUI.getInput();
                Edge transitionEdge = null;
                boolean isStuck = false;

                PathFinder pathFinder = new PathFinder();
                pathFinder.setCurrentInput(inputElements);
                pathFinder.setToNode(node);
                pathFinder.setTableModel(model);

                ArrayList<PathFinder> pathFinders = new ArrayList<PathFinder>();
                pathFinders.add(pathFinder);


                while (inputElements != null && !inputElements.equals("") && node != null && !isStuck && !pathFinders.isEmpty()) {
                    ArrayList<String> stackArray = new ArrayList<String>();
                    for (int q = 0; q < model.getRowCount(); q++) {
                        stackArray.add(model.getValueAt(q, 0).toString());
                    }
                    String input;
//                                System.out.println("node outgoing combos: "+node.getOutGoingCombo());
                    if (!isNdpda) {
                        input = inputElements.substring(0, 1);
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
                        String transitionOperation = "";
                        if (transitionEdge != null) {
                            transitionOperation = transitionEdge.getTransitionOperation();
                        }
                        if (transitionOperation.contains("push")) {
                            String stackCharacter = transitionOperation.substring(transitionOperation.lastIndexOf("(") + 1, transitionOperation.lastIndexOf(")"));
                            model.insertRow(0, new String[]{stackCharacter});
                            model.fireTableDataChanged();

                        } else if (transitionOperation.contains("pop")) {
                            model.removeRow(0);
                            model.fireTableDataChanged();

                        } else if (transitionOperation.contains("e")) {
                            //do nothing lol
                        }
                        node = transitionEdge.getToNode();
                        inputElements = inputElements.substring(1);

                        Thread.sleep(1000);
                        textArea.setCaretPosition(textArea.getDocument().getLength());
                        textArea.append("Moving to node: '" + transitionEdge.getToNode() + "' from node: '" + transitionEdge.getFromNode() + "' through transition rule: " + transitionEdge.toString() + "\n");
                        totalPath.append(transitionEdge.getToNode());
                        if (inputElements.equals("")) {
                            textArea.append("Simulation finished at node: '" + transitionEdge.getToNode() + "'.\nPath taken: '" + totalPath.toString() + "'.\n");
                            if (transitionEdge.getToNode().isAccept()) {
                                textArea.append("RESULT: SUCCESS. '" + transitionEdge.getToNode() + "' is an accepting state.\n");
                            } else {
                                textArea.append("RESULT: FAILURE. '" + transitionEdge.getToNode() + "' is not an accepting state.\n");
                            }
                        } else {
                            textArea.append("Current input elements: " + inputElements + "\n");
                        }

                    }
                }
                return null;
            }
        };
        worker.execute();
    }


    public void ndpdaAlgorithm(String inputElements, GraphNode node, PathGenerator pathGenerator) {

        ArrayList<Edge> transitionEdges = new ArrayList<Edge>();
        Edge transitionEdge=null;


        if (inputElements!=null && !inputElements.equals("") && node!=null) {
            String input=inputElements.substring(0,1);
            inputElements = inputElements.substring(1);

            for (Edge edge : edgeArray) {
                //edgeTopInputAndStack = edge.getEdgeTopInput() + edge.getEdgeTopStack();
                if (node.toString().equals(edge.getFromNode().toString())) {
//                                        System.out.println("Outgoing edges from:" +node.toString());
//                                        System.out.println("Edges:"+edge.toString());
                    if (edge.getEdgeTopStack().equals(pathGenerator.getStackArray().get(0)) && edge.getEdgeTopInput() == Integer.parseInt(input)) {
                        transitionEdges.add(edge);
                    }
                }
            }

            if (transitionEdges.size() == 1) {
                System.out.println("Single outgoing edge");
                transitionEdge = transitionEdges.get(0);

            } else if (transitionEdges.size() > 1){
                System.out.println("Many outgoing edge");


                for (int i = 0; i < transitionEdges.size(); i++) {
                    transitionEdge = transitionEdges.get(i);
                    node = transitionEdge.getToNode();
                    //pathGenerators.add((path + node.toString()));

                    String currentPath = pathGenerator.getPath().toString();
                    ArrayList<String> currentStack = pathGenerator.getStackArray();
                    ArrayList<String> newStack = new ArrayList<String>();
                    newStack.addAll(currentStack);
                    PathGenerator newPath = new PathGenerator();
                    //System.out.println("test"+currentPath);
                    //newPath.getPath().append(node.toString());
                    newPath.setStringBuilder(new StringBuilder(currentPath+node.toString()));
                    newPath.setStackArray(newStack);


                    String transitionOperation="";
                    if (transitionEdge != null) {
                        transitionOperation = transitionEdge.getTransitionOperation();
                    }
                    if (transitionOperation.contains("push")) {
                        String stackCharacter = transitionOperation.substring(transitionOperation.lastIndexOf("(")+1, transitionOperation.lastIndexOf(")"));
                        newPath.getStackArray().add(0, stackCharacter);

                    } else if (transitionOperation.contains("pop")) {
                        newPath.getStackArray().remove(0);

                    } else if (transitionOperation.contains("e")) {
                        //do nothing lol
                    }
                    newPath.addToStackOperations(transitionOperation);

                    pathGenerators.add(newPath);

                    ndpdaAlgorithm(inputElements, node, newPath);
                }
            }

            String transitionOperation="";
            if (transitionEdge != null) {
                transitionOperation = transitionEdge.getTransitionOperation();
                pathGenerator.addToStackOperations(transitionOperation);

                if (transitionOperation.contains("push")) {
                    String stackCharacter = transitionOperation.substring(transitionOperation.lastIndexOf("(") + 1, transitionOperation.lastIndexOf(")"));
//                model.insertRow(0, new String[]{stackCharacter});
//                model.fireTableDataChanged();
                    pathGenerator.getStackArray().add(0, stackCharacter);

                } else if (transitionOperation.contains("pop")) {
//                model.removeRow(0);
//                model.fireTableDataChanged();
                    pathGenerator.getStackArray().remove(0);

                } else if (transitionOperation.contains("e")) {
                    //do nothing lol
                }

                node = transitionEdge.getToNode();
//                path.append(node.toString());
//                ndpdaAlgorithm(inputElements, node, path);
                pathGenerator.getPath().append(node.toString());
                ndpdaAlgorithm(inputElements, node, pathGenerator);

            }
        }
        if (pathGenerators.isEmpty() == false) {
            ArrayList<String> allPaths = new ArrayList<String>();
            for (PathGenerator p : pathGenerators) {
                allPaths.add(p.getPath().toString());

            }
            if (!allPaths.contains(pathGenerator.getPath().toString())) {
                pathGenerators.add(pathGenerator);
            }
        } else {
            pathGenerators.add(pathGenerator);
        }

    }

    public Set<PathGenerator> getPathGenerators(RunSimGUI runSimGUI) {
        Set<PathGenerator> toRemove = new LinkedHashSet<>();
        for (PathGenerator s: pathGenerators) {
                if ((s.getPath().toString().length()) > (runSimGUI.getInput().length() + 1)) {
                    toRemove.add(s);
                }
        }

        pathGenerators.removeAll(toRemove);

        return pathGenerators;
    }

    public void runNDPDAPath(PathGenerator selectedPath) {

        worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {

                String route = selectedPath.toString();
                String startNode=route.substring(0,1);
                route=route.substring(1);
                String inputElements = runSimGUI.getInput();

                for (GraphNode n : nodeArray) {
                    if (n.isInitial) {
                        startNode = n.toString();
                        break;
                    }
                }

                while (inputElements != null && !inputElements.equals("") && startNode != null && route != null) {

                    ArrayList<String> stackArray = new ArrayList<String>();
                    for (int q = 0; q < model.getRowCount(); q++) {
                        stackArray.add(model.getValueAt(q, 0).toString());
                    }

                    String input = inputElements.substring(0, 1);

                    String nextNode = route.substring(0,1);
                    route=route.substring(1);

                    System.out.println(startNode +"     "+ nextNode +"\n"+ input);

                    Edge transitionEdge = null;

                    for (Edge e : edgeArray) {
                        if (e.getFromNode().toString().equals(startNode) && e.getToNode().toString().equals(nextNode) && e.getEdgeTopStack().equals(stackArray.get(0)) && e.getEdgeTopInput() == Integer.parseInt(input)) {
                            transitionEdge = e;
                            break;
                        }
                    }

                    System.out.println(transitionEdge);

                    String transitionOperation = "";
                    if (transitionEdge != null) {
                        transitionOperation = transitionEdge.getTransitionOperation();
                    }
                    if (transitionOperation.contains("push")) {
                        String stackCharacter = transitionOperation.substring(transitionOperation.lastIndexOf("(") + 1, transitionOperation.lastIndexOf(")"));
                        model.insertRow(0, new String[]{stackCharacter});
                        model.fireTableDataChanged();

                    } else if (transitionOperation.contains("pop")) {
                        model.removeRow(0);
                        model.fireTableDataChanged();

                    } else if (transitionOperation.contains("e")) {
                        //do nothing lol
                    }

                    inputElements = inputElements.substring(1);
                    startNode=nextNode;

                    Thread.sleep(1000);
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                    textArea.append("Moving to node: '" + transitionEdge.getToNode() + "' from node: '" + transitionEdge.getFromNode() + "' through transition rule: " + transitionEdge.toString() + "\n");
                    if (inputElements.equals("")) {
                        textArea.append("Simulation finished at node: '" + transitionEdge.getToNode() + "'.\n");
                        if (transitionEdge.getToNode().isAccept()) {
                            textArea.append("RESULT: SUCCESS. '" + transitionEdge.getToNode() + "' is an accepting state.\n");
                        } else {
                            textArea.append("RESULT: FAILURE. '" + transitionEdge.getToNode() + "' is not an accepting state.\n");
                        }
                    } else {
                        textArea.append("Current input elements: " + inputElements + "\n");
                    }
                }

                return null;
            }
        };
        worker.execute();
    }

}
