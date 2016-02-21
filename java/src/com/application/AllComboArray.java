package com.application;


import java.util.ArrayList;

/**
 * Created by CheokHo on 21/02/2016.
 */
public class AllComboArray {

    public AllComboArray() {
    }

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
