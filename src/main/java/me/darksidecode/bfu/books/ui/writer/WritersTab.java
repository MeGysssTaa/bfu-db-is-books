package me.darksidecode.bfu.books.ui.writer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.OrderingDirection;
import me.darksidecode.bfu.books.database.entity.Writer;
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

public class WritersTab extends JPanel {

    private JPanel topToolbar;
    private JTextField tfSearch;
    private JComboBox<Ordering> cbOrdering;
    private JComboBox<OrderingDirection> cbOrderingDirection;

    public WritersTab() {
        setLayout(new MigLayout());
        setupTopToolbar();
        refresh();
    }

    private void setupTopToolbar() {
        topToolbar = new JPanel();
        topToolbar.setLayout(new FlowLayout());

        var btnAdd = new JButton("Add");
        btnAdd.setFont(UiOptions.genericFont);
        btnAdd.addActionListener(__ -> new AddWriterForm(this, this::refresh));
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
        
        cbOrderingDirection = new JComboBox<>(OrderingDirection.values());
        cbOrderingDirection.setFont(UiOptions.genericFont);
        cbOrderingDirection.setEditable(false);
        cbOrderingDirection.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                refresh();
            }
        });
        topToolbar.add(cbOrderingDirection);

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

        var repo = App.INSTANCE.getRepo().writers();
        var writers = searchQuery.isBlank() ? repo.getAll() : repo.search(searchQuery);
        var ordering = Objects.requireNonNull((Ordering) cbOrdering.getSelectedItem());
        var ordDir = Objects.requireNonNull((OrderingDirection) cbOrderingDirection.getSelectedItem());
        ordering.sort.accept(writers);
        ordDir.sort.accept(writers);

        for (var writer : writers) {
            var lblWriterInfo = new JLabel(writer.toString());
            lblWriterInfo.setFont(UiOptions.genericFont);
            add(lblWriterInfo, "w 700!");

            var btnEdit = new JButton("Edit");
            btnEdit.setFont(UiOptions.genericFont);
            add(btnEdit);
            btnEdit.addActionListener(__ ->
                    new EditWriterForm(this, writer, this::refresh));

            var btnDelete = new JButton("Delete");
            btnDelete.setFont(UiOptions.genericFont);
            add(btnDelete, "wrap");
            btnDelete.addActionListener(__ ->
                    new DeleteWriterForm(this, writer, this::refresh));
        }

        repaint();
    }

    @RequiredArgsConstructor
    private enum Ordering {
        Name(entities -> entities.sort(Comparator.comparing(Writer::fullName))),

        Born(entities -> entities.sort(Comparator.comparing(it ->
                it.born() == null ? Long.MAX_VALUE : it.born().toLocalDate().toEpochDay()))),

        Died(entities -> entities.sort(Comparator.comparing(it ->
                it.died() == null ? Long.MAX_VALUE : it.died().toLocalDate().toEpochDay()))),

        YearsLived(entities -> entities.sort(Comparator.comparing(it ->
                (it.born() == null || it.died() == null) ? Long.MAX_VALUE :
                        it.died().toLocalDate().toEpochDay() - it.born().toLocalDate().toEpochDay()))),

        ;


        private final @NonNull Consumer<List<Writer>> sort;
    }

}
