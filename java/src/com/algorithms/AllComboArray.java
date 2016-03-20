package com.algorithms;


import java.util.ArrayList;

/**
 * Created by CheokHo on 21/02/2016.
 */

public class AllComboArray {

    public AllComboArray() {
    }

    /**
     * Returns all possible combinations of an ArrayList.
     * @param arrayList1 first arraylist.
     * @param arrayList2 second arraylist.
     * @return result all combinations.
     */
    public ArrayList<String> getAllCombinations(ArrayList<String> arrayList1, ArrayList<String> arrayList2) {
        ArrayList<String> result = new ArrayList<String>();
        for (String s1: arrayList1) {
            for(String s2: arrayList2) {
                result.add(s1 + s2);
            }
        }
        return result;
    }
}
