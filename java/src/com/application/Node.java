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


    public Node(mxGraph graph, String state){
        this.graph=graph;
        this.state=state;
        parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
    }

    public void createNode(int x,int y) {
        try
        {
            node = graph.insertVertex(parent, null, state, x, y, 80, 60);
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
