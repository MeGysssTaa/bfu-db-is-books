package me.darksidecode.bfu.books.ui;

import me.darksidecode.bfu.books.ui.country.CountriesTab;
import me.darksidecode.bfu.books.ui.genre.GenresTab;
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
        tabs.addTab("Genres", new JScrollPane(new GenresTab()));
        tabs.addTab("Countries", new JScrollPane(new CountriesTab()));

        getContentPane().setLayout(new GridLayout());
        getContentPane().add(tabs);

        setSize(910, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
