package com.application;

import com.mxgraph.view.mxGraph;

import java.util.ArrayList;

/**
 * Created by CheokHo on 25/01/2016.
 */
public class GraphNode {

    Object node;
    String name;
    boolean isInitial;
    boolean isAccept;
    int x; int y;
    ArrayList<String> outGoingInputs;
    ArrayList<String> outGoingTopStacks;
    ArrayList<String> outGoingCombo;
    ArrayList<String> outGoingEdgeRule;
    ArrayList<String> toFromCombo;

    public GraphNode(Object node, String name, boolean isInitial, boolean isAccept) {
        this.node=node;
        this.name=name;
        this.isInitial=isInitial;
        this.isAccept=isAccept;
        outGoingInputs = new ArrayList<String>();
        outGoingTopStacks = new ArrayList<String>();
        outGoingCombo = new ArrayList<String>();
        outGoingEdgeRule = new ArrayList<String>();
        toFromCombo = new ArrayList<String>();
    }

    public String toString() {
        return name;
    }

    public Object getNode() {
        return node;
    }

    public void addOutgoingInput(String input) {
        outGoingInputs.add(input);
    }

    public ArrayList<String> getOutgoingInputs() {
        return outGoingInputs;
    }
    public void addOutgoingTopStack(String stack) {
        outGoingTopStacks.add(stack);
    }
    public ArrayList<String> getOutGoingTopStacks() {
        return outGoingTopStacks;
    }
    public void addOutgoingCombo(String combo) {outGoingCombo.add(combo); }
    public ArrayList<String> getOutGoingCombo() {
        return outGoingCombo;
    }
    public void addOutgoingEdgeRule(String edgeRule) { outGoingEdgeRule.add(edgeRule); }
    public ArrayList<String> getOutGoingEdgeRule() {return outGoingEdgeRule; }
    public void addToFromCombo(String toFrom) {
        toFromCombo.add(toFrom);
    }
    public ArrayList<String> getToFromCombo() {
        return toFromCombo;
    }
    public boolean isInitial() {
        return isInitial;
    }
    public boolean isAccept() {
        return isAccept;
    }
    public void setXPosition(int x) {
        this.x=x;
    }
    public void setYPosition(int y) {
        this.y=y;
    }
    public int getXPosition() {
        return x;
    }
    public int getYPosition() {
        return y;
    }
}

