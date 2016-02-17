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

    public Edge(String edgeRule) {
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
    public void setFromNode(Node fromNode) {
        this.fromNode=fromNode;
    }
    public Node getFromNode() {
        return fromNode;
    }
    public void setToNode(Node toNode) {
        this.toNode=toNode;
    }
    public Node getToNode() {
        return toNode;
    }
}
