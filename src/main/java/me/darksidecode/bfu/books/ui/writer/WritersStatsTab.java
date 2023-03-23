package me.darksidecode.bfu.books.ui.writer;

import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.ui.UiOptions;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class WritersStatsTab extends JPanel {

    private JPanel topToolbar;
    private JTextField tfSearch;

    public WritersStatsTab() {
        setLayout(new MigLayout());
        setupTopToolbar();
        refresh();
    }

    private void setupTopToolbar() {
        topToolbar = new JPanel();
        topToolbar.setLayout(new FlowLayout());

        var btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(UiOptions.genericFont);
        btnRefresh.addActionListener(__ -> refresh());
        topToolbar.add(btnRefresh);

        tfSearch = new JTextField(40);
        tfSearch.setFont(UiOptions.genericFont);
        tfSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    refresh();
                }
            }
        });
        topToolbar.add(tfSearch);
    }

    private void refresh() {
        var searchQuery = tfSearch.getText();

        removeAll();
        add(topToolbar, "wrap");

        var writersRepo = App.INSTANCE.getRepo().writers();
        var writers = searchQuery.isBlank() ? writersRepo.getAll() : writersRepo.search(searchQuery);

        String[] columns = { "Writer", "Texts", "Awards" };
        int[] colWidths = { 600, 100, 100 };
        Object[][] data = new Object[writers.size()][columns.length];

        for (int i = 0; i < writers.size(); i++) {
            var writer = writers.get(i);
            data[i][0] = writer.fullName();
            data[i][1] = App.INSTANCE.getRepo().texts().countByWriter(writer);
            data[i][2] = App.INSTANCE.getRepo().awards().countByWriter(writer);
        }

        var table = new JTable(data, columns);
        table.setFont(UiOptions.genericFont);
        table.setDefaultEditor(Object.class, null);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(35);
        for (int i = 0; i < colWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(colWidths[i]);
        }
        var tableHeader = table.getTableHeader();
        tableHeader.setFont(UiOptions.genericFont);
        add(table.getTableHeader(), "w 800!, wrap");
        add(table, "w 800!");

        repaint();
    }

}
