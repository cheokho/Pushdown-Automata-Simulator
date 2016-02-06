package com.application;

import com.mxgraph.view.mxGraph;

/**
 * Created by CheokHo on 25/01/2016.
 */
public class Edge {
    Object edge;
    String edgeRule;
    Node fromNode;
    Node toNode;

    public Edge(Object edge) {
        this.edge=edge;
    }

    public void setEdgeRule(String edgeRule, Node fromNode, Node toNode) {
        this.edgeRule=edgeRule;
        this.fromNode=fromNode;
        this.toNode=toNode;
    }
}
