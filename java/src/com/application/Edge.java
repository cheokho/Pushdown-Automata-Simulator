package com.application;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

/**
 * Created by CheokHo on 25/01/2016.
 */
public class Edge {
    String edgeRule;
    Node fromNode;
    Node toNode;

    public Edge(Node fromNode, Node toNode, String edgeRule) {
        this.fromNode=fromNode;
        this.toNode=toNode;
        this.edgeRule=edgeRule;
    }

    public String toString() {
        return edgeRule;
    }
}
