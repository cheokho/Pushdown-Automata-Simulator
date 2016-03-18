package com.application;



import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.UIManager.*;

/**
 * Created by CheokHo on 25/01/2016.
 */

public class Main
{

    public Main() {
    }

    public static void main(String[] args)
    {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // nimbus not available - use default.
        }
        TopLevelGUI frame = new TopLevelGUI();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(850, 500));
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

}

