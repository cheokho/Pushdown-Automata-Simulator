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
    int edgeTopInput;
    String edgeTopStack;
    String transitionOperation;

    public Edge(Node fromNode, Node toNode, String edgeRule) {
        this.fromNode=fromNode;
        this.toNode=toNode;
        this.edgeRule=edgeRule;
    }

    public String toString() {
        return edgeRule;
    }
    public void setEdgeTopInput(int edgeTopInput) {
        this.edgeTopInput=edgeTopInput;
    }
    public int getEdgeTopInput() {
        return edgeTopInput;
    }
    public void setEdgeTopStack(String edgeTopStack) {
        this.edgeTopStack=edgeTopStack;
    }
    public String getEdgeTopStack() {
        return edgeTopStack;
    }
    public void setTransitionOperation(String transitionOperation) {
        this.transitionOperation=transitionOperation;
    }
    public String getTransitionOperation() {
        return transitionOperation;
    }
}
