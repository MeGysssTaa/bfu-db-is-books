package me.darksidecode.bfu.books.ui.award;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.database.entity.Award;
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

public class AwardsTab extends JPanel {

    private JPanel topToolbar;
    private JTextField tfSearch;
    private JComboBox<Ordering> cbOrdering;

    public AwardsTab() {
        setLayout(new MigLayout());
        setupTopToolbar();
        refresh();
    }

    private void setupTopToolbar() {
        topToolbar = new JPanel();
        topToolbar.setLayout(new FlowLayout());

        var btnAdd = new JButton("Add");
        btnAdd.setFont(UiOptions.genericFont);
        btnAdd.addActionListener(__ -> new AddAwardForm(this, this::refresh));
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

        var repo = App.INSTANCE.getRepo().awards();
        var awards = searchQuery.isBlank() ? repo.getAll() : repo.search(searchQuery);
        var ordering = Objects.requireNonNull((Ordering) cbOrdering.getSelectedItem());
        ordering.sort.accept(awards);

        for (var award : awards) {
            var lblAwardInfo = new JLabel(award.toString());
            lblAwardInfo.setFont(UiOptions.genericFont);
            add(lblAwardInfo, "w 700!");

            var btnEdit = new JButton("Edit");
            btnEdit.setFont(UiOptions.genericFont);
            add(btnEdit);
            btnEdit.addActionListener(__ ->
                    new EditAwardForm(this, award, this::refresh));

            var btnDelete = new JButton("Delete");
            btnDelete.setFont(UiOptions.genericFont);
            add(btnDelete, "wrap");
            btnDelete.addActionListener(__ ->
                    new DeleteAwardForm(this, award, this::refresh));
        }

        repaint();
    }

    @RequiredArgsConstructor
    private enum Ordering {
        Writer(entities -> entities.sort(Comparator.comparing(it ->
                it.getWriterObj().firstName() + " "
                        + it.getWriterObj().patronymic() + " "
                        + it.getWriterObj().secondName()))),

        Prize(entities -> entities.sort(Comparator.comparing(it -> it.getPrizeObj().name()))),

        Date(entities -> entities.sort(Comparator.comparing(it -> it.getDate().toLocalDate().toEpochDay()))),

        AmountUSD(entities -> entities.sort(Comparator.comparing(Award::getPrizeAmountDollars)))

        ;


        private final @NonNull Consumer<List<Award>> sort;
    }

}
