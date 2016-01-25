package com.application;

import com.mxgraph.view.mxGraph;

/**
 * Created by CheokHo on 25/01/2016.
 */
public class Node {
    mxGraph graph;
    Object parent;
    Object node;
    String state;
    boolean isFinal;

    public Node(mxGraph graph, String state, boolean isFinal){
        this.graph=graph;
        this.state=state;
        this.isFinal=isFinal;
        parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
    }

    public void createNode(int x,int y) {
        try
        {
            if (isFinal == true) {
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
    }

    public Object getNode() throws Exception{
        return node;
    }
}
