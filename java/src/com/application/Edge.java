package com.application;

import com.mxgraph.view.mxGraph;

/**
 * Created by CheokHo on 25/01/2016.
 */
public class Edge {
    mxGraph graph;
    Object parent;

    public Edge(mxGraph graph) {
        this.graph=graph;
        parent = graph.getDefaultParent();

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
}
