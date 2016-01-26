package com.application;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;

/**
 * Created by CheokHo on 25/01/2016.
 */

public class Main
{

    public Main() {
    }

    public static void main(String[] args)
    {
        TopLevelGUI frame = new TopLevelGUI();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

}

