package com.application;

import javax.swing.*;
import java.awt.*;

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
        frame.setMinimumSize(new Dimension(850, 500));
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

}

