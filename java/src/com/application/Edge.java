package com.application;

/**
 * Created by CheokHo on 25/01/2016.
 */
public class Edge {
    String edgeRule;
    GraphNode fromNode;
    GraphNode toNode;
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
    public void setFromNode(GraphNode fromNode) {
        this.fromNode=fromNode;
    }
    public GraphNode getFromNode() {
        return fromNode;
    }
    public void setToNode(GraphNode toNode) {
        this.toNode=toNode;
    }
    public GraphNode getToNode() {
        return toNode;
    }
}
