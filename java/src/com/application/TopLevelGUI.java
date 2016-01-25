package com.application;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by CheokHo on 25/01/2016.
 */
public class TopLevelGUI extends JFrame{

    public TopLevelGUI(){
        super("Pushdown Automata Tool");
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

        JMenu edit = new JMenu("Edit");
        JMenuItem menuUndo = new JMenuItem("Undo");
        JMenuItem menuRedo = new JMenuItem("Redo");
        JMenuItem menuDelete = new JMenuItem("Delete");
        edit.add(menuUndo); edit.add(menuRedo); edit.add(menuDelete);

        menuBar.add(file); menuBar.add(edit);

    }

    public void createGraphPane(){
        mxGraph graph = new mxGraph();
        Edge edge = new Edge(graph);
//        Object parent = graph.getDefaultParent();
//
//        graph.getModel().beginUpdate();
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
        Node nodeA = new Node(graph, "a");
        nodeA.createNode(20,20);
        Node nodeB = new Node(graph, "b");
        nodeB.createNode(240,150);
        try {
            edge.addEdge(nodeA.getNode(), nodeB.getNode(), "transition rule temp");
        } catch (Exception e) {
            e.printStackTrace();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);
    }
}
