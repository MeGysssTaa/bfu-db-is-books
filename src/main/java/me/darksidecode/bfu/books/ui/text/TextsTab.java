//package me.darksidecode.bfu.books.ui.text;
//
//import me.darksidecode.bfu.books.App;
//import me.darksidecode.bfu.books.ui.UiOptions;
//import me.darksidecode.bfu.books.ui.writer.AddWriterForm;
//import me.darksidecode.bfu.books.ui.writer.DeleteWriterForm;
//import me.darksidecode.bfu.books.ui.writer.EditWriterForm;
//import net.miginfocom.swing.MigLayout;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//
//public class TextsTab extends JPanel {
//
//    private final JPanel topToolbar;
//
//    private String currentFilter;
//
//    public TextsTab() {
//        setLayout(new MigLayout());
//
//        topToolbar = new JPanel();
//        setupTopToolbar();
//
//        refresh();
//    }
//
//    private void setupTopToolbar() {
//        topToolbar.setLayout(new FlowLayout());
//
//        var btnAdd = new JButton("Add");
//        btnAdd.setFont(UiOptions.genericFont);
//        btnAdd.addActionListener(__ -> new AddWriterForm(this, this::refresh));
//        topToolbar.add(btnAdd);
//
//        var btnRefresh = new JButton("Refresh");
//        btnRefresh.setFont(UiOptions.genericFont);
//        btnRefresh.addActionListener(__ -> refresh());
//        topToolbar.add(btnRefresh);
//
//        var tfSearch = new JTextField("Search", 40);
//        tfSearch.setFont(UiOptions.genericFont);
//        tfSearch.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() != KeyEvent.VK_ENTER)
//                    return;
//
//                var searchQuery = tfSearch.getText();
//
//                if (tfSearch.getText().isBlank()) {
//                    currentFilter = null;
//                } else {
//                    currentFilter = searchQuery;
//                }
//
//                refresh();
//            }
//        });
//        topToolbar.add(tfSearch);
//    }
//
//    private void refresh() {
//        removeAll();
//        add(topToolbar, "span 3, wrap");
//
//        var repo = App.INSTANCE.getDatabase().writers();
//        var writers = currentFilter == null
//                ? repo.getAllWriters()
//                : repo.searchWriters(currentFilter);
//
//        for (var writer : writers) {
//            var lblWriterInfo = new JLabel(writer.toString());
//            lblWriterInfo.setFont(UiOptions.genericFont);
//            add(lblWriterInfo, "w 700!");
//
//            var btnEdit = new JButton("Edit");
//            btnEdit.setFont(UiOptions.genericFont);
//            add(btnEdit);
//            btnEdit.addActionListener(__ ->
//                    new EditWriterForm(this, writer, this::refresh));
//
//            var btnDelete = new JButton("Delete");
//            btnDelete.setFont(UiOptions.genericFont);
//            add(btnDelete, "wrap");
//            btnDelete.addActionListener(__ ->
//                    new DeleteWriterForm(this, writer, this::refresh));
//        }
//
//        repaint();
//    }
//
//}
