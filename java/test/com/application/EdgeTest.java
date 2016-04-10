package com.application;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Cheeeky on 09/04/2016.
 */
public class EdgeTest {

    Edge edgeTest = new Edge("{0, $, push(Y)}");

    @Test
    public void getEdgeTopInput() throws Exception {
        edgeTest.setEdgeTopInput(0);

        assertEquals(0, edgeTest.getEdgeTopInput());
    }

    @Test
    public void getEdgeTopStack() throws Exception {
        edgeTest.setEdgeTopStack("$");

        assertEquals("$", edgeTest.getEdgeTopStack());

    }

    @Test
    public void getTransitionOperation() throws Exception {
        edgeTest.setTransitionOperation("push(Y)");

        assertEquals("push(Y)", edgeTest.getTransitionOperation());
    }
}