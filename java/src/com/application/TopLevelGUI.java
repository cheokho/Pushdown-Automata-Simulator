package com.application;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by CheokHo on 25/01/2016.
 */
public class TopLevelGUI extends JFrame{

    private Object parent;
    private mxGraph graph;

    public TopLevelGUI(){
        super("Pushdown Automata Tool");
        graph = new mxGraph();
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
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        new mxKeyboardHandler(graphComponent);
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                    if (cell != null) {
                        System.out.println(cell.getValue().toString());
                        JPopupMenu popup = new JPopupMenu();
                        JMenuItem delete = new JMenuItem("Delete");
                        popup.add(delete);
                        popup.show(e.getComponent(), e.getX(), e.getY());
                        delete.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // TODO Auto-generated method stub
                                graph.removeCells(new Object[]{cell});
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
        try
        {
            if (isAccepting) {
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=doubleEllipse");
            }
            else {
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=ellipse");
            }
            graph.setVertexLabelsMovable(false);
            graph.setCellsEditable(false);
        }
        finally
        {
            graph.getModel().endUpdate();
        }
        return node;
    }

    public void addEdge(Object nodeFrom, Object nodeTo, String transRule){
        try
        {
            graph.insertEdge(parent, null, transRule, nodeFrom, nodeTo);
            graph.setAllowDanglingEdges(false);
            graph.setEdgeLabelsMovable(false);
            graph.setCellsEditable(false);
        }
        finally
        {
            graph.getModel().endUpdate();
        }
    }

    public TopLevelGUI getTopLevelGUI() {
        return this;
    }
}
