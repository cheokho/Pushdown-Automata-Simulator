package com.application;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.view.mxGraph;
import javafx.animation.Transition;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

/**
 * Created by CheokHo on 25/01/2016.
 */
public class TopLevelGUI extends JFrame{

    private Object parent;
    private mxGraph graph;
    private ArrayList<Node> nodeArray;
    private ArrayList<Edge> edgeArray;
    private JTextArea consoleArea;
    //private ArrayList<String> stackArray;
    private PDAVersionGUI pdaTypeGUI;
    private TransitionRuleGUI transRule;
    private DefaultTableModel model;

    //private mxCell nodePressed;
    private mxCell cellReleased;
    private mxCell cellPressed;

    public TopLevelGUI(){
        super("Pushdown Automata Tool");
        setLayout(new BorderLayout());
        graph = new mxGraph();
        nodeArray = new ArrayList<Node>();
        edgeArray = new ArrayList<Edge>();
//        stackArray = new ArrayList<String>();
//        stackArray.add("$");
        createGraphPane();
        createStackGUI();
        createConsoleGUI();
        createMenuBar();
    }


    public void createMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu file = new JMenu("File");
        JMenuItem menuNew = new JMenuItem("New");
        JMenuItem menuRun = new JMenuItem("Run Simulation");
        JMenuItem menuOpen = new JMenuItem("Open");
        JMenuItem menuSave = new JMenuItem("Save");
        JMenuItem menuClose = new JMenuItem("Close");
        menuClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        file.add(menuNew); file.add(menuRun); file.add(menuOpen); file.add(menuSave); file.add(menuClose);

        menuNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (graph.getChildVertices(graph.getDefaultParent()) != null) {
                    int reply = JOptionPane.showConfirmDialog(null, "Creating a new PDA will clear the existing.", "Warning", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
                        nodeArray.clear();
                        edgeArray.clear();
                        pdaTypeGUI = new PDAVersionGUI(getTopLevelGUI());
                    }
                }
            }
        });

        menuRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AllComboArray allComboArray = new AllComboArray();
                boolean testRunnable = false;
                boolean containsInitial = false;
                boolean containsAccept = false;
                if (pdaTypeGUI == null) {
                    JOptionPane.showMessageDialog(getFocusOwner(), "No PDA is specified. No simulation can be run. \nPlease create a new PDA first.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    ArrayList<String> allInputStackCombo = allComboArray.getAllCombinations(pdaTypeGUI.getPdaInputGUI().getInputArray(), pdaTypeGUI.getPdaInputGUI().getStackArray());
                    ArrayList<String> missingCombos = new ArrayList<String>();
                    System.out.println("All input stack combo: " + allInputStackCombo);
                    for (Node n : nodeArray) {
                        System.out.println("Current input-stack combo: " + n.getOutGoingCombo());
                        if (n.isInitial) {
                            containsInitial = true;
                        }
                        if (n.isAccept) {
                            containsAccept = true;
                        }
                        if (n.getOutGoingCombo().containsAll(allInputStackCombo) && allInputStackCombo.containsAll(n.getOutGoingCombo())) {
                            testRunnable = true;
                        } else {
                            testRunnable = false;
                        }
                    }
                    if (PDAVersionGUI.isNdpda) {
                        testRunnable = true;
                    }
                    if (testRunnable == true && containsAccept == true && containsInitial == true) {
                        model.setRowCount(0);
                        model.addRow(new String[]{"$"});
                        model.fireTableDataChanged();
                        RunSimGUI runSimGUI = new RunSimGUI(getFocusOwner(), pdaTypeGUI.getPdaInputGUI().getInputArray());
                        runSimGUI.showRunSimGUI();
                        getTextArea().append("\nRunning simulation on: " + runSimGUI.getInput()+"\n");
                        if (PDAVersionGUI.isNdpda) {
                            getTextArea().append("Preparing to run simulation. Specified graph is: Non Deterministic\n");
                        } else {
                            getTextArea().append("Preparing to run simulation. Specified graph is: Deterministic\n");
                        }

                        SwingWorker<Boolean, Void> worker = null;

                        AlgorithmRunner algorithmRunner = new AlgorithmRunner(runSimGUI, getModel(), nodeArray, edgeArray, getTextArea(), worker);
                        if (PDAVersionGUI.isNdpda) {
                            Node node=null;
                            for (Node n: nodeArray) {
                                if (n.isInitial) {
                                    node=n;
                                    break;
                                }
                            }

                            //create $ stack array here.

                            PathGenerator pathGenerator = new PathGenerator();
                            ArrayList<String> stackArray = new ArrayList<String>();
                            stackArray.add(0, "$");
                            pathGenerator.setStackArray(stackArray);
                            StringBuilder path=new StringBuilder();
                            path.append(node.toString());
                            pathGenerator.setStringBuilder(path);

                            algorithmRunner.ndpdaAlgorithm(runSimGUI.getInput(),node, pathGenerator);

                            //System.out.println("PATH GENERATOR: "+algorithmRunner.getPathGenerators(runSimGUI));
                            for (PathGenerator p: algorithmRunner.getPathGenerators(runSimGUI)) {
                                System.out.println("path: "+p.getPath().toString());
                                System.out.println("stackarray at end: "+p.getStackArray()+" | transition operations:"+p.getStackOperations());
                            }
                        } else {
                            algorithmRunner.runAlgorithm(PDAVersionGUI.isNdpda);
                        }

                        /**TODO - Get initial state for starting point.
                         - Get top stack element AND leftmost input element; loop through all edges and find a match then call break.
                         - When matched, get 'toNode' value of specified edge and specify that as node to move to.
                         - Delete leftmost input string now it is consumed and update stack according to edge transition rule.
                         - When input string is empty algorithm has finished, determine if the state we are on is accepting.
                         **/

                    } else if (containsAccept == false || containsInitial == false) {
                        JOptionPane.showMessageDialog(getFocusOwner(), "There is no initial and/or accepting state in your PDA.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(getFocusOwner(), "You have not specified all the transition rules required to run a deterministic PDA simulation.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JMenu edit = new JMenu("Edit");
        JMenuItem menuUndo = new JMenuItem("Undo");
        JMenuItem menuRedo = new JMenuItem("Redo");
        JMenuItem menuDelete = new JMenuItem("Delete");
        edit.add(menuUndo); edit.add(menuRedo); edit.add(menuDelete);

        menuBar.add(file); menuBar.add(edit);

    }

    public void createStackGUI() {
        JPanel stackPanel = new JPanel();
        stackPanel.setLayout(new FlowLayout());
        Object columnName[] = {"Stack"};
        model = new DefaultTableModel(columnName, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //model.addColumn("Stack", stackArray.toArray());
        model.addRow(new String[] {"$"});

        JTable stackTable = new JTable(model);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        stackTable.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        //stackTable.setTableHeader(null);
        JScrollPane scrollPane= new JScrollPane(stackTable);
        scrollPane.setPreferredSize(new Dimension(150, 530));
        stackPanel.add(scrollPane);
        add(stackPanel, BorderLayout.EAST);
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public void createConsoleGUI() {
        JPanel consolePanel = new JPanel();
        JLabel title = new JLabel("Console:");
        consoleArea = new JTextArea(8, 70);
        consoleArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(consoleArea);
        consolePanel.add(title, BorderLayout.NORTH);
        consolePanel.add(scrollPane, BorderLayout.CENTER);
        add(consolePanel, BorderLayout.SOUTH);
//        PrintStream printStream = new PrintStream(new CustomOutput(consoleArea));
//        PrintStream standardOut = System.out;
//        System.setOut(printStream);
//        System.setErr(printStream);
    }

    public JTextArea getTextArea() {
        return consoleArea;
    }


    public void createGraphPane(){
//        Edge edge = new Edge(graph);
//        try
//        {
//            Object v1 = graph.insertVertex(parent, null, "a", 20, 20, 80,
//                    60);
//            Object v2 = graph.insertVertex(parent, null, "b", 240, 150,
//                    80, 60);
//            graph.insertEdge(parent, null, "Edge", v1, v2);
//        }
//        finally
//        {
//            graph.getModel().endUpdate();
//        }
//        Node nodeA = new Node(graph, "a", false);
//        nodeA.createNode(20,20);
//        Node nodeB = new Node(graph, "b", true);
//        nodeB.createNode(240,150);
//        try {
//            edge.addEdge(nodeA.getNode(), nodeB.getNode(), "transition rule temp");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        parent = graph.getDefaultParent();
//        mxIGraphLayout layout = new mxParallelEdgeLayout(graph, 30);
//        layout.execute(parent);

        //Object a=createNode(20,20, "a", false);
        //Object b=createNode(240,150, "b", true);
        //addEdge(a, b, "temp trans rule");

        mxGraphComponent graphComponent = new mxGraphComponent(graph) {
            @Override
            protected mxConnectionHandler createConnectionHandler() {
                return new mxConnectionHandler(this) {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        super.mouseReleased(e);
                    }
                };
            }
        };



        //new mxKeyboardHandler(graphComponent); needs to be fixed for backend (keyboard deleting)

        //This handles edge creation handlers.
        graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxEventSource.mxIEventListener() {
            @Override
            public void invoke(Object sender, mxEventObject evt) {
                //System.out.println("edge: " + evt.getProperty("cell"));
                graph.getModel().remove(evt.getProperty("cell"));
            }
        });


        //This handles node creation handlers.
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                //nodePressed = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                //Left click (maybe not needed)
                if (SwingUtilities.isLeftMouseButton(e)) {
                    cellPressed = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                    if (cellPressed != null) {
                        //System.out.println("Left Click Cell Value: " + cellPressed.getValue().toString());
                    }
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    cellPressed = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                    if (cellPressed != null) {
                        //System.out.println("Right Click Cell Value: " + cellPressed.getValue().toString());
                        JPopupMenu popup = new JPopupMenu();
                        JMenuItem delete = new JMenuItem("Delete");
                        popup.add(delete);
                        popup.show(e.getComponent(), e.getX(), e.getY());
                        delete.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // TODO Auto-generated method stub
                                graph.removeCells(new Object[]{cellPressed});
                                for (int i = 0; i < nodeArray.size(); i++) {
                                    if (nodeArray.get(i).toString().equals(cellPressed.getValue().toString())) {
                                        for (int x=0; x < edgeArray.size(); x++) {
                                            if (nodeArray.get(i).equals(edgeArray.get(x).getFromNode()) || nodeArray.get(i).equals(edgeArray.get(x).getToNode()))  {
                                                edgeArray.remove(x);
                                            }
                                        }
                                        nodeArray.remove(i);
                                    }
                                }
                                for (int i = 0; i < edgeArray.size(); i++) {
                                    if (edgeArray.get(i).toString().equals(cellPressed.getValue().toString())) {
                                        for (Node n: nodeArray) {
                                                for (String s: n.getOutGoingTopStacks()) {
                                                    if (s.equals(edgeArray.get(i).getEdgeTopStack())) {
                                                        n.getOutGoingTopStacks().remove(s);
                                                        break;
                                                    }
                                                }
                                                for (String s1: n.getOutgoingInputs()) {
                                                    int value=Integer.parseInt(s1);
                                                    if (value == edgeArray.get(i).getEdgeTopInput()) {
                                                        n.getOutgoingInputs().remove(s1);
                                                        break;
                                                    }
                                                }
                                                for (String s2: n.getOutGoingCombo()) {
                                                    if(s2.equals(edgeArray.get(i).getEdgeTopInput()+edgeArray.get(i).getEdgeTopStack())) {
                                                        n.getOutGoingCombo().remove(s2);
                                                        break;
                                                    }
                                                }
                                                for (String s3: n.getOutGoingEdgeRule()) {
                                                    if (s3.equals(edgeArray.get(i).toString())) {
                                                        n.getOutGoingEdgeRule().remove(s3);
                                                        break;
                                                    }
                                                }
                                                for (String s4: n.getToFromCombo()) {
                                                    if (s4.equals(edgeArray.get(i).getToNode().toString()+edgeArray.get(i).getFromNode().toString())) {
                                                        n.getToFromCombo().remove(s4);
                                                        break;
                                                    }
                                                }
                                                //System.out.println("COMBO   :"+n.getOutGoingCombo());
                                        }
                                        edgeArray.remove(i);
                                    }
                                }
                                System.out.println("Updated Edge Array: " + edgeArray.toString());
                                System.out.println("Updated Node Array: " + nodeArray.toString());
                                repaint();
                            }
                        });
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                cellReleased = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                if (SwingUtilities.isLeftMouseButton(e) && cellPressed != null) {
                    if (cellReleased != null && cellReleased.isVertex() && e.getClickCount() == 2) {
                        //if (!pdaTypeGUI.isNdpda()) { //this is a deterministic PDA so each node must only have 1 rule for each input.
                            transRule = new TransitionRuleGUI(getTopLevelGUI(), cellPressed.getValue().toString(), cellReleased.getValue().toString(), pdaTypeGUI.getPdaInputGUI().getStackArray(), pdaTypeGUI.getPdaInputGUI().getInputArray(), PDAVersionGUI.isNdpda);
                            System.out.println("STACK ARRAY ON TRANS RULE RELEASE " + pdaTypeGUI.getPdaInputGUI().getStackArray());
                            System.out.println("INPUT ARRAY ON TRANS RULE RELEASE " + pdaTypeGUI.getPdaInputGUI().getInputArray());
                            for (Node n: nodeArray) {
                                if (n.toString().equals(cellPressed.getValue().toString())) {
                                    if (transRule.getEdge() != null) {
                                        n.addOutgoingInput(Integer.toString(transRule.getEdge().getEdgeTopInput()));
                                        n.addOutgoingTopStack(transRule.getEdge().getEdgeTopStack());
                                        n.addOutgoingCombo(Integer.toString(transRule.getEdge().getEdgeTopInput()) + transRule.getEdge().getEdgeTopStack());
                                        n.addOutgoingEdgeRule(transRule.getEdge().toString());
                                        n.addToFromCombo(cellReleased.getValue().toString()+cellPressed.getValue().toString());
                                    }
                                }
                            }

                        //graph.insertEdge(parent, null, "self loop", nodePressed, (Object) cellReleased);
                    } else if (cellReleased != null && cellReleased.isVertex() && !cellPressed.getValue().equals(cellReleased.getValue())) {
                        //if (!pdaTypeGUI.isNdpda()) { //this is a deterministic PDA so each node must only have 1 rule for each input.
                            transRule = new TransitionRuleGUI(getTopLevelGUI(), cellPressed.getValue().toString(), cellReleased.getValue().toString(), pdaTypeGUI.getPdaInputGUI().getStackArray(), pdaTypeGUI.getPdaInputGUI().getInputArray(), PDAVersionGUI.isNdpda);
                            System.out.println("STACK ARRAY ON TRANS RULE RELEASE " + pdaTypeGUI.getPdaInputGUI().getStackArray());
                            System.out.println("INPUT ARRAY ON TRANS RULE RELEASE " + pdaTypeGUI.getPdaInputGUI().getInputArray());
                            for (Node n: nodeArray) {
                                if (n.toString().equals(cellPressed.getValue().toString())) {
                                    if (transRule.getEdge() != null) {
                                        n.addOutgoingInput(Integer.toString(transRule.getEdge().getEdgeTopInput()));
                                        n.addOutgoingTopStack(transRule.getEdge().getEdgeTopStack());
                                        n.addOutgoingCombo(Integer.toString(transRule.getEdge().getEdgeTopInput()) + transRule.getEdge().getEdgeTopStack());
                                        n.addOutgoingEdgeRule(transRule.getEdge().toString());
                                        n.addToFromCombo(cellReleased.getValue().toString()+cellPressed.getValue().toString());
                                    }
                                }
//                            System.out.println("LOL"+n.getOutgoingInputs());
//                            System.out.println("LOL1"+n.getOutGoingTopStacks());
                            }
//                        graph.insertEdge(parent, null, "test for now", nodePressed, (Object) cellReleased);
                    }
                    //System.out.println(nodeArray.get(0).toString() +"   "+nodeArray.get(0).getOutgoingInputs()+"    "+nodeArray.get(0).getOutGoingTopStacks());
                }
            }
        });
        add(getContentPane().add(graphComponent), BorderLayout.CENTER);
    }

    public Object createNode(int x,int y, String state, boolean isAccepting, boolean isInitial) {
        graph.getModel().beginUpdate();
        Object node=null;
        Node newNode;
        try
        {
            if (isInitial && !isAccepting) {
                //System.out.println("init - not accept");
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=ellipse;perimeter=ellipsePerimeter;fillColor=green");
            }
            else if (isAccepting && !isInitial) {
                //System.out.println("not init - accept");
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=doubleEllipse; perimeter=ellipsePerimeter");
            }
            else if (isAccepting && isInitial) {
                //System.out.println("init - accept");
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=doubleEllipse;perimeter=ellipsePerimeter;fillColor=green");
            }
            else if (!isInitial && !isAccepting) {
                //System.out.println("not init - not accept");
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=ellipse; perimeter=ellipsePerimeter");
            }
            newNode = new Node(node, state, isInitial, isAccepting);
            nodeArray.add(newNode);

            graph.setVertexLabelsMovable(false);
            graph.setCellsEditable(false);
        }
        finally
        {
            graph.getModel().endUpdate();
        }
        return node;
    }

//    //Instead of taking in an Object for nodes, pass in Node object instead.
//    public void addEdge(Node nodeFrom, Node nodeTo, String transRule){
//        Object edge;
//        Edge newEdge;
//        //Node newNodeFrom = new Node(nodeFrom, ); //need to get the cell name to create this.
//        Node newNodeTo;
//        try
//        {
//            edge = graph.insertEdge(parent, null, transRule, nodeFrom.getNode(), nodeTo.getNode());
//            newEdge = new Edge(edge);
//            //newEdge.setEdgeRule();
//            graph.setAllowDanglingEdges(false);
//            graph.setEdgeLabelsMovable(false);
//            graph.setCellsEditable(false);
//        }
//        finally
//        {
//            graph.getModel().endUpdate();
//        }
//    }

    public ArrayList<Node> getNodeArray() {
        return nodeArray;
    }

    public ArrayList<Edge> getEdgeArray() { return edgeArray; }

    public mxCell getCellPressed() {
        return cellPressed;
    }

    public mxCell getCellReleased() {
        return cellReleased;
    }

    public mxGraph getGraph() {
        return graph;
    }
    public TopLevelGUI getTopLevelGUI() {
        return this;
    }

}
