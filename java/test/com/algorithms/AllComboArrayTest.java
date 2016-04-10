package com.algorithms;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static java.util.Arrays.asList;

import static org.junit.Assert.*;

/**
 * Created by Cheeeky on 08/04/2016.
 */
public class AllComboArrayTest {

    @Test
    public void getAllCombinations() throws Exception {
        ArrayList<String> first = new ArrayList<>();
        first.add("X");
        first.add("Y");

        ArrayList<String> second = new ArrayList<>();
        second.add("0");
        second.add("1");

        List<String> test = asList("X0", "X1", "Y0", "Y1");

        ArrayList<String> result = new AllComboArray().getAllCombinations(first, second);

        assertTrue(result.equals(test));

    }
}