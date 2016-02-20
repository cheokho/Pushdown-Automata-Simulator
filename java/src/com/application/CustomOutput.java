package com.application;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Cheeeky on 20/02/2016.
 */
public class CustomOutput extends OutputStream{
    private JTextArea textArea;

    public CustomOutput(JTextArea textArea) {
        this.textArea=textArea;
    }

    @Override
    public void write(int b) throws IOException {
        textArea.append(String.valueOf((char)b)); // redirects data to the text area
        textArea.setCaretPosition(textArea.getDocument().getLength()); // scrolls the text area to the end of data
    }
}

