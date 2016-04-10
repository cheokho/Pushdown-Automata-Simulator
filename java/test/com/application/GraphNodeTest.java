package com.application;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

/**
 * Created by Cheeeky on 09/04/2016.
 */
public class GraphNodeTest {

    GraphNode testNode = new GraphNode(new Object(), "a", true, false);

    @Test
    public void getOutgoingInputs() throws Exception {
        testNode.addOutgoingInput("X0");
        testNode.addOutgoingInput("Y1");

        List<String> test = asList("X0", "Y1");

        assertTrue(testNode.getOutgoingInputs().equals(test));
    }

    @Test
    public void getOutGoingTopStacks() throws Exception {
        testNode.addOutgoingTopStack("X");
        testNode.addOutgoingTopStack("Y");
        testNode.addOutgoingTopStack("Z");

        List<String> test1 = asList("X", "Y", "Z");

        assertTrue(testNode.getOutGoingTopStacks().equals(test1));
    }

    @Test
    public void getToFromCombo() throws Exception {
        testNode.addToFromCombo("ab");
        testNode.addToFromCombo("ac");
        testNode.addToFromCombo("aa");

        List<String> test2 = asList("ab", "ac", "aa");

        assertTrue(testNode.getToFromCombo().equals(test2));
    }

    @Test
    public void isInitial() throws Exception {
        assertTrue(testNode.isInitial());
    }

    @Test
    public void isAccept() throws Exception {
        assertTrue(!testNode.isAccept());
    }
}