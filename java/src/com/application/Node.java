package com.application;

import com.mxgraph.view.mxGraph;

import java.util.ArrayList;

/**
 * Created by CheokHo on 25/01/2016.
 */
public class Node {

    Object node;
    String name;
    ArrayList<String> outGoingInputs;
    ArrayList<String> outGoingTopStacks;

    public Node(Object node, String name) {
        this.node=node;
        this.name=name;
        outGoingInputs = new ArrayList<String>();
        outGoingTopStacks = new ArrayList<String>();
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
}

