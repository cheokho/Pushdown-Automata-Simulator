package com.application;

import com.mxgraph.view.mxGraph;

/**
 * Created by CheokHo on 25/01/2016.
 */
public class Node {

    Object node;
    String name;
    String input;

    public Node(Object node, String name) {
        this.node=node;
        this.name=name;
    }

    public String toString() {
        return name;
    }

    public Object getNode() {
        return node;
    }

    public void setOutgoingInput(String input) {
        this.input=input;
    }

    public String getOutgoingInput() {
        return input;
    }
}

