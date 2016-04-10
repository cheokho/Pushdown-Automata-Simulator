package com.application;

import com.algorithms.PathFinder;
import com.algorithms.PathGenerator;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

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
    private int executionTime;
    private String inputElements;
    private mxGraph graph;
    private mxGraphComponent graphComponent;
    private ArrayList<mxCell> frontEndStates;
    private Set<PathGenerator> pathGenerators;

    public AlgorithmRunner(int executionTime, RunSimGUI runSimGUI, DefaultTableModel model, mxGraph graph, ArrayList<GraphNode> nodeArray, ArrayList<Edge> edgeArray, JTextArea textArea, SwingWorker<Void, Void> worker) {
        this.executionTime=executionTime;
        this.runSimGUI=runSimGUI;
        this.model=model;
        this.graph=graph;
        this.nodeArray=nodeArray;
        this.edgeArray=edgeArray;
        this.textArea=textArea;
        this.worker=worker;

        pathGenerators = new LinkedHashSet<PathGenerator>();

        graphComponent = new mxGraphComponent(graph);

        graph.clearSelection();
        graph.selectAll();
        Object[] cells = graph.getSelectionCells();
        frontEndStates = new ArrayList<mxCell>();

        for (Object c : cells) {
            mxCell cell = (mxCell) c;
            if (cell.isVertex()) {
                frontEndStates.add(cell);
                //System.out.println("test        "+ cell.getValue().toString());
            }
        }
        graph.clearSelection();

        //refreshes state colours back to orig.
        GraphNode initial = null;
        Object initialCell = null;
        for (GraphNode n: nodeArray) {
            if (n.isInitial) {
                initial=n;
            }
        }
        for (mxCell m: frontEndStates) {
            if (m.getValue().toString().equals(initial.toString())) {
                initialCell = m;
            }
            graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#3090C7", new Object[]{m});
            graphComponent.refresh();
        }
        graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#4CC417", new Object[]{initialCell});
        graphComponent.refresh();
    }

    public void dpdaAlgorithm(boolean isNdpda) {

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
                inputElements = runSimGUI.getInput();
                Edge transitionEdge = null;
                boolean isStuck = false;

                PathFinder pathFinder = new PathFinder();
                pathFinder.setCurrentInput(inputElements);
                pathFinder.setToNode(node);
                pathFinder.setTableModel(model);

                ArrayList<PathFinder> pathFinders = new ArrayList<PathFinder>();
                pathFinders.add(pathFinder);

//                mxGraphComponent graphComponent = new mxGraphComponent(graph);
//
//                graph.clearSelection();
//                graph.selectAll();
//                Object[] cells = graph.getSelectionCells();
//                ArrayList<mxCell> frontEndStates = new ArrayList<mxCell>();
//
//                for (Object c : cells) {
//                    mxCell cell = (mxCell) c;
//                    if (cell.isVertex()) {
//                        frontEndStates.add(cell);
//                        //System.out.println("test        "+ cell.getValue().toString());
//                    }
//                }
//                graph.clearSelection();


                while (inputElements != null && !inputElements.equals("") && node != null && !pathFinders.isEmpty()) {
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

                        Object colorState = null;
                        GraphNode colorNode = null;
                        for (mxCell state: frontEndStates) {
                            if (state.getValue().toString().equals(node.toString())) {
                                colorState = state;
                                colorNode = node;
                                //System.out.println("test    "+colorState);
                            }
                        }
                        graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "red", new Object[]{colorState});
                        graphComponent.refresh();

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

                        Thread.sleep(executionTime);

                        if (colorNode.isInitial) {
                            graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#4CC417", new Object[]{colorState});
                            graphComponent.refresh();
                        } else if (!colorNode.isInitial) {
                            graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#3090C7", new Object[]{colorState});
                            graphComponent.refresh();
                        }

                        textArea.setCaretPosition(textArea.getDocument().getLength());
                        textArea.append("Moving to node: '" + transitionEdge.getToNode() + "' from node: '" + transitionEdge.getFromNode() + "' through transition rule: " + transitionEdge.toString() + "\n");
                        totalPath.append(transitionEdge.getToNode());
                        if (inputElements.equals("")) {

                            Object endState = null;
                            GraphNode endNode = null;
                            for (mxCell state: frontEndStates) {
                                if (state.getValue().toString().equals(node.toString())) {
                                    endState = state;
                                    endNode = node;
                                }
                            }

                            graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "red", new Object[]{endState});
                            graphComponent.refresh();
                            try {
                                Thread.sleep(executionTime);
                            } catch(InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            }

                            if (endNode.isInitial) {
                                graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#4CC417", new Object[]{endState});
                                graphComponent.refresh();
                            } else if (!endNode.isInitial) {
                                graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#3090C7", new Object[]{endState});
                                graphComponent.refresh();
                            }

                            textArea.append("Simulation finished at node: '" + transitionEdge.getToNode() + "'.\nPath taken: '" + totalPath.toString() + "'.\n");
                            if (transitionEdge.getToNode().isAccept()) {
                                textArea.append("RESULT: SUCCESS. '" + transitionEdge.getToNode() + "' is an accepting state.\n");
                            } else {
                                textArea.append("RESULT: FAILURE. '" + transitionEdge.getToNode() + "' is not an accepting state.\n");
                            }
                        }

                        else {
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
                inputElements = runSimGUI.getInput();

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

                    Object colorState = null;
                    GraphNode colorNode = null;
                    for (mxCell state: frontEndStates) {
                        if (state.getValue().toString().equals(startNode)) {
                            colorState = state;
                            //System.out.println("test    "+colorState);
                        }
                    }
                    for (GraphNode n: nodeArray) {
                        if (n.toString().equals(startNode)) {
                            colorNode = n;
                        }
                    }
                    graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "red", new Object[]{colorState});
                    graphComponent.refresh();

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

                    Thread.sleep(executionTime);

                    assert colorNode != null;
                    if (colorState != null) {
                        if (colorNode.isInitial) {
                            graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#4CC417", new Object[]{colorState});
                            graphComponent.refresh();
                        } else if (!colorNode.isInitial) {
                            graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#3090C7", new Object[]{colorState});
                            graphComponent.refresh();
                        }
                    }

                    textArea.setCaretPosition(textArea.getDocument().getLength());
                    textArea.append("Moving to node: '" + transitionEdge.getToNode() + "' from node: '" + transitionEdge.getFromNode() + "' through transition rule: " + transitionEdge.toString() + "\n");
                    if (inputElements.equals("")) {

                        Object endState = null;
                        GraphNode endNode = null;
                        for (mxCell state: frontEndStates) {
                            if (state.getValue().toString().equals(startNode)) {
                                endState = state;
                            }
                        }
                        for (GraphNode n: nodeArray) {
                            if (n.toString().equals(startNode)) {
                                endNode = n;
                            }
                        }

                        graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "red", new Object[]{endState});
                        graphComponent.refresh();
                        try {
                            Thread.sleep(executionTime);
                        } catch(InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }

                        if (endNode.isInitial) {
                            graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#4CC417", new Object[]{endState});
                            graphComponent.refresh();
                        } else if (!endNode.isInitial) {
                            graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "#3090C7", new Object[]{endState});
                            graphComponent.refresh();
                        }

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

            @Override
            protected void done() {
                if (!inputElements.equals("")) {
                    textArea.append("RESULT: STUCK.");
                }
            }
        };
        worker.execute();
    }
}
