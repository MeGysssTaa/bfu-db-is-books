package me.darksidecode.bfu.books.ui.keeperType;

import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.ui.UiOptions;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeeperTypesTab extends JPanel {

    private JPanel topToolbar;
    private JTextField tfSearch;

    private String currentFilter;

    public KeeperTypesTab() {
        setLayout(new MigLayout());
        setupTopToolbar();
        refresh();
    }

    private void setupTopToolbar() {
        topToolbar = new JPanel();
        topToolbar.setLayout(new FlowLayout());

        var btnAdd = new JButton("Add");
        btnAdd.setFont(UiOptions.genericFont);
        btnAdd.addActionListener(__ -> new AddKeeperTypeForm(this, this::refresh));
        topToolbar.add(btnAdd);

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

        if (tfSearch.getText().isBlank()) {
            currentFilter = null;
        } else {
            currentFilter = searchQuery;
        }

        removeAll();
        add(topToolbar, "span 3, wrap");

        var repo = App.INSTANCE.getRepo().keeperTypes();
        var keeperTypes = currentFilter == null
                ? repo.getAll()
                : repo.search(currentFilter);

        for (var keeperType : keeperTypes) {
            var lblWriterInfo = new JLabel(keeperType.toString());
            lblWriterInfo.setFont(UiOptions.genericFont);
            add(lblWriterInfo, "w 700!");

            var btnEdit = new JButton("Edit");
            btnEdit.setFont(UiOptions.genericFont);
            add(btnEdit);
            btnEdit.addActionListener(__ ->
                    new EditKeeperTypeForm(this, keeperType, this::refresh));

            var btnDelete = new JButton("Delete");
            btnDelete.setFont(UiOptions.genericFont);
            add(btnDelete, "wrap");
            btnDelete.addActionListener(__ ->
                    new DeleteKeeperTypeForm(this, keeperType, this::refresh));
        }

        repaint();
    }

}
