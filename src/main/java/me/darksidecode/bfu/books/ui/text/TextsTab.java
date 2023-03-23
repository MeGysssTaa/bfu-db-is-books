package me.darksidecode.bfu.books.ui.text;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.database.entity.Text;
import me.darksidecode.bfu.books.ui.UiOptions;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class TextsTab extends JPanel {

    private JPanel topToolbar;
    private JTextField tfSearch;
    private JComboBox<Ordering> cbOrdering;

    public TextsTab() {
        setLayout(new MigLayout());
        setupTopToolbar();
        refresh();
    }

    private void setupTopToolbar() {
        topToolbar = new JPanel();
        topToolbar.setLayout(new FlowLayout());

        var btnAdd = new JButton("Add");
        btnAdd.setFont(UiOptions.genericFont);
        btnAdd.addActionListener(__ -> new AddTextForm(this, this::refresh));
        topToolbar.add(btnAdd);

        var btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(UiOptions.genericFont);
        btnRefresh.addActionListener(__ -> refresh());
        topToolbar.add(btnRefresh);

        cbOrdering = new JComboBox<>(Ordering.values());
        cbOrdering.setFont(UiOptions.genericFont);
        cbOrdering.setEditable(false);
        cbOrdering.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                refresh();
            }
        });
        topToolbar.add(cbOrdering);

        tfSearch = new JTextField(20);
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
        add(topToolbar, "span 3, wrap");

        var repo = App.INSTANCE.getRepo().texts();
        var texts = searchQuery.isBlank() ? repo.getAll() : repo.search(searchQuery);
        var ordering = Objects.requireNonNull((Ordering) cbOrdering.getSelectedItem());
        ordering.sort.accept(texts);

        for (var text : texts) {
            var lblTextInfo = new JLabel(text.toString());
            lblTextInfo.setFont(UiOptions.genericFont);
            add(lblTextInfo, "w 700!");

            var btnEdit = new JButton("Edit");
            btnEdit.setFont(UiOptions.genericFont);
            add(btnEdit);
            btnEdit.addActionListener(__ ->
                    new EditTextForm(this, text, this::refresh));

            var btnDelete = new JButton("Delete");
            btnDelete.setFont(UiOptions.genericFont);
            add(btnDelete, "wrap");
            btnDelete.addActionListener(__ ->
                    new DeleteTextForm(this, text, this::refresh));
        }

        repaint();
    }

    @RequiredArgsConstructor
    private enum Ordering {
        TextName(entities -> entities.sort(Comparator.comparing(Text::getName))),

        AuthorName(entities -> entities.sort(Comparator.comparing(it ->
                it.getWriterObj() == null ? "" :
                        it.getWriterObj().firstName() + " "
                                + it.getWriterObj().patronymic() + " "
                                + it.getWriterObj().secondName()))),

        Published(entities -> entities.sort(Comparator.comparing(it ->
                it.getPublished() == null ? Long.MAX_VALUE : it.getPublished().toLocalDate().toEpochDay()))),

        ;


        private final @NonNull Consumer<List<Text>> sort;
    }

}
