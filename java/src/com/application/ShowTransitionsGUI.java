package com.application;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Cheeeky on 27/03/2016.
 */
public class ShowTransitionsGUI extends JFrame {
    private ArrayList<Edge> edgeArray;
    private DefaultTableModel model;

    public ShowTransitionsGUI(ArrayList<Edge> edgeArray) {
        setTitle("Transition Edges");
        setIconImage(new ImageIcon(getClass().getResource("/com/icon/favicon.png")).getImage());
        this.edgeArray=edgeArray;

        createTransitionGUI();
        setMinimumSize(new Dimension(400, 400));
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void createTransitionGUI() {
        model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Starting state");
        model.addColumn("Destination state");
        model.addColumn("Transition rule");
        JTable table = new JTable(model);

        for (Edge e: edgeArray) {
            model.addRow(new Object[]{e.getFromNode(), e.getToNode(), e.toString()});
        }


        JScrollPane scrollPane = new JScrollPane(table);
        JButton button = new JButton("Refresh table");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.getRowCount() > 0) {
                    for (int i = model.getRowCount() - 1; i > -1; i--) {
                        model.removeRow(i);
                    }
                }
                for (Edge e1: edgeArray) {
                    model.addRow(new Object[]{e1.getFromNode(), e1.getToNode(), e1.toString()});
                }
                model.fireTableDataChanged();
            }
        });
        add(scrollPane, BorderLayout.CENTER);
        add(button, BorderLayout.SOUTH);
        getRootPane().setBorder(new EmptyBorder(5,5,5,5));


    }


}
