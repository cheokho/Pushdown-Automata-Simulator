package com.application;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by CheokHo on 25/01/2016.
 */
public class TopLevelGUI extends JFrame{

    private Object parent;
    private mxGraph graph;
    private ArrayList<Node> nodeArray;
    private ArrayList<Edge> edgeArray;
    private ArrayList<String> stackArray;
    private PDAVersionGUI pdaTypeGUI;
    private TransitionRuleGUI transRule;

    //private mxCell nodePressed;
    private mxCell cellReleased;
    private mxCell cellPressed;

    public TopLevelGUI(){
        super("Pushdown Automata Tool");
        setLayout(new BorderLayout());
        graph = new mxGraph();
        nodeArray = new ArrayList<Node>();
        edgeArray = new ArrayList<Edge>();
        stackArray = new ArrayList<String>();
        stackArray.add("$");
        createGraphPane();
        createStackGUI(stackArray);
        createMenuBar();
    }


    public void createMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu file = new JMenu("File");
        JMenuItem menuNew = new JMenuItem("New");
        JMenuItem menuOpen = new JMenuItem("Open");
        JMenuItem menuSave = new JMenuItem("Save");
        JMenuItem menuClose = new JMenuItem("Close");
        menuClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        file.add(menuNew); file.add(menuOpen); file.add(menuSave); file.add(menuClose);

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

        JMenu edit = new JMenu("Edit");
        JMenuItem menuUndo = new JMenuItem("Undo");
        JMenuItem menuRedo = new JMenuItem("Redo");
        JMenuItem menuDelete = new JMenuItem("Delete");
        edit.add(menuUndo); edit.add(menuRedo); edit.add(menuDelete);

        menuBar.add(file); menuBar.add(edit);

    }

    public void createStackGUI(ArrayList<String> stackArray) {
        JPanel stackPanel = new JPanel();
        stackPanel.setLayout(new FlowLayout());
        DefaultTableModel model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Stack", stackArray.toArray());

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
                System.out.println("edge: " + evt.getProperty("cell"));
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
                        System.out.println("Left Click Cell Value: " + cellPressed.getValue().toString());
                    }
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    cellPressed = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                    if (cellPressed != null) {
                        System.out.println("Right Click Cell Value: " + cellPressed.getValue().toString());
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
                                        nodeArray.remove(i);
                                    }
                                }
                                for (int i = 0; i < edgeArray.size(); i++) {
                                    if (edgeArray.get(i).toString().equals(cellPressed.getValue().toString())) {
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
                        transRule = new TransitionRuleGUI(getTopLevelGUI(), cellPressed.getValue().toString(), cellReleased.getValue().toString(), pdaTypeGUI.getPdaInputGUI().getStackArray(), pdaTypeGUI.getPdaInputGUI().getInputArray());
                        System.out.println("STACK ARRAY ON TRANS RULE RELEASE " + pdaTypeGUI.getPdaInputGUI().getStackArray());
                        System.out.println("INPUT ARRAY ON TRANS RULE RELEASE " + pdaTypeGUI.getPdaInputGUI().getInputArray());
                        //graph.insertEdge(parent, null, "self loop", nodePressed, (Object) cellReleased);
                    } else if (cellReleased != null && cellReleased.isVertex() && !cellPressed.getValue().equals(cellReleased.getValue())) {
                        transRule = new TransitionRuleGUI(getTopLevelGUI(), cellPressed.getValue().toString(), cellReleased.getValue().toString(), pdaTypeGUI.getPdaInputGUI().getStackArray(), pdaTypeGUI.getPdaInputGUI().getInputArray());
                        System.out.println("STACK ARRAY ON TRANS RULE RELEASE " + pdaTypeGUI.getPdaInputGUI().getStackArray());
                        System.out.println("INPUT ARRAY ON TRANS RULE RELEASE " + pdaTypeGUI.getPdaInputGUI().getInputArray());
//                        graph.insertEdge(parent, null, "test for now", nodePressed, (Object) cellReleased);
                    }
                }
            }
        });
        add(getContentPane().add(graphComponent), BorderLayout.CENTER);
    }

    public Object createNode(int x,int y, String state, boolean isAccepting, boolean isInitial) {
        graph.getModel().beginUpdate();
        Object node;
        Node newNode;
        try
        {
            if (isInitial) {
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=ellipse; perimeter=ellipsePerimeter; fillColor=green");
            }
            if (isAccepting) {
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=doubleEllipse; perimeter=ellipsePerimeter");
            }
            else {
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=ellipse; perimeter=ellipsePerimeter");
            }
            newNode = new Node(node, state);
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
