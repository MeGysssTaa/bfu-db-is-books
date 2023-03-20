package me.darksidecode.bfu.books.ui;

import me.darksidecode.bfu.books.ui.writer.WritersTab;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame {

    public MainForm() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("BFU DB IS - Books");

        var tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        tabs.setFont(UiOptions.genericFont);
        tabs.addTab("Writers", new JScrollPane(new WritersTab()));
        tabs.addTab("Texts", createTextsTab());

        getContentPane().setLayout(new GridLayout());
        getContentPane().add(tabs);

        setSize(910, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private Component createTextsTab() {
        var tab = new JPanel();
        tab.setBackground(Color.BLACK);
        return tab;
    }

}
