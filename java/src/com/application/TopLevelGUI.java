package com.application;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
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

    public TopLevelGUI(){
        super("Pushdown Automata Tool");
        graph = new mxGraph();
        nodeArray = new ArrayList<Node>();
        createGraphPane();
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
                PDAVersionGUI pdaTypeGUI = new PDAVersionGUI(getTopLevelGUI());
            }
        });

        JMenu edit = new JMenu("Edit");
        JMenuItem menuUndo = new JMenuItem("Undo");
        JMenuItem menuRedo = new JMenuItem("Redo");
        JMenuItem menuDelete = new JMenuItem("Delete");
        edit.add(menuUndo); edit.add(menuRedo); edit.add(menuDelete);

        menuBar.add(file); menuBar.add(edit);

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
        //Object a=createNode(20,20, "a", false);
        //Object b=createNode(240,150, "b", true);
        //addEdge(a, b, "temp trans rule");

        mxGraphComponent graphComponent = new mxGraphComponent(graph) {
            @Override
            protected mxConnectionHandler createConnectionHandler() {
                return new mxConnectionHandler(this) {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        //add code for check here.
                        mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                        if (cell != null) {
                            graph.insertEdge(parent, null, "test for now",  );
                            System.out.println("mouse released.");
                        }
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
                System.out.println("edge: "+evt.getProperty("cell"));
                graph.getModel().remove(evt.getProperty("cell"));
            }
        });


        //This handles node creation handlers.
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                //Left click (maybe not needed)
                if (SwingUtilities.isLeftMouseButton(e)) {
                    mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                    if (cell!=null) {
                        System.out.println("Left Click Cell Value: "+cell.getValue().toString());
                    }
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                    if (cell != null) {
                        System.out.println("Right Click Cell Value: "+cell.getValue().toString());
                        JPopupMenu popup = new JPopupMenu();
                        JMenuItem delete = new JMenuItem("Delete");
                        popup.add(delete);
                        popup.show(e.getComponent(), e.getX(), e.getY());
                        delete.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // TODO Auto-generated method stub
                                graph.removeCells(new Object[]{cell});
                                for (int i=0; i<nodeArray.size(); i++) {
                                    if (nodeArray.get(i).getNodeName().equals(cell.getValue().toString())) {
                                        nodeArray.remove(i);
                                    }
                                }
                                System.out.println("Updated Node Array: "+nodeArray.toString());
                                repaint();
                            }
                        });
                    }
                }
            }
        });

        getContentPane().add(graphComponent);
    }

    public Object createNode(int x,int y, String state, boolean isAccepting) {
        graph.getModel().beginUpdate();
        Object node;
        Node newNode;
        try
        {
            if (isAccepting) {
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=doubleEllipse");
            }
            else {
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=ellipse");
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

    //Instead of taking in an Object for nodes, pass in Node object instead.
    public void addEdge(Node nodeFrom, Node nodeTo, String transRule){
        Object edge;
        Edge newEdge;
        //Node newNodeFrom = new Node(nodeFrom, ); //need to get the cell name to create this.
        Node newNodeTo;
        try
        {
            edge = graph.insertEdge(parent, null, transRule, nodeFrom.getNode(), nodeTo.getNode());
            newEdge = new Edge(edge);
            //newEdge.setEdgeRule();
            graph.setAllowDanglingEdges(false);
            graph.setEdgeLabelsMovable(false);
            graph.setCellsEditable(false);
        }
        finally
        {
            graph.getModel().endUpdate();
        }
    }

    public ArrayList<Node> getNodeArray() {
        return nodeArray;
    }

    public TopLevelGUI getTopLevelGUI() {
        return this;
    }
}
