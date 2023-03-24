package me.darksidecode.bfu.books.ui.text;

import com.github.lgooddatepicker.components.DatePicker;
import lombok.NonNull;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.Utils;
import me.darksidecode.bfu.books.database.entity.Genre;
import me.darksidecode.bfu.books.database.entity.Text;
import me.darksidecode.bfu.books.database.entity.Writer;
import me.darksidecode.bfu.books.ui.UiOptions;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class EditTextForm extends JFrame {

    private final Text text;
    private final Runnable successListener;
    private final JTextField tfName;
    private final JComboBox<Genre> cbGenre;
    private final JComboBox<Writer> cbWriter;
    private final DatePicker dpWritingBegun, dpWritingEnded, dpPublished;

    public EditTextForm(Component parentForm, @NonNull Text text, @NonNull Runnable successListener) {
        this.text = text;
        this.successListener = successListener;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new MigLayout());
        setTitle("Editing text " + text.getName());

        var lblGenre = new JLabel("Genre");
        lblGenre.setFont(UiOptions.genericFont);
        getContentPane().add(lblGenre);
        var allGenres = App.INSTANCE.getRepo().genres().getAll().toArray(new Genre[0]);
        var selectedGenreIndex = Utils.firstIndexOf(allGenres, it -> it.id() == text.getGenre());
        cbGenre = new JComboBox<>(allGenres);
        selectedGenreIndex.ifPresent(cbGenre::setSelectedIndex);
        cbGenre.setFont(UiOptions.genericFont);
        cbGenre.setEditable(false);
        getContentPane().add(cbGenre, "wrap");

        var lblName = new JLabel("Name");
        lblName.setFont(UiOptions.genericFont);
        getContentPane().add(lblName);
        tfName = new JTextField(text.getName(), 30);
        tfName.setFont(UiOptions.genericFont);
        getContentPane().add(tfName, "wrap");

        var lblWritingBegun = new JLabel("Writing Begun");
        lblWritingBegun.setFont(UiOptions.genericFont);
        getContentPane().add(lblWritingBegun);
        dpWritingBegun = new DatePicker(UiOptions.datePickerSettings(true));
        if (text.getWritingBegun() != null) {
            dpWritingBegun.setDate(text.getWritingBegun().toLocalDate());
        }
        getContentPane().add(dpWritingBegun, "wrap");

        var lblWritingEnded = new JLabel("Writing Ended");
        lblWritingEnded.setFont(UiOptions.genericFont);
        getContentPane().add(lblWritingEnded);
        dpWritingEnded = new DatePicker(UiOptions.datePickerSettings(true));
        if (text.getWritingEnded() != null) {
            dpWritingEnded.setDate(text.getWritingEnded().toLocalDate());
        }
        getContentPane().add(dpWritingEnded, "wrap");

        var lblPublished = new JLabel("Published");
        lblPublished.setFont(UiOptions.genericFont);
        getContentPane().add(lblPublished);
        dpPublished = new DatePicker(UiOptions.datePickerSettings(true));
        if (text.getPublished() != null) {
            dpPublished.setDate(text.getPublished().toLocalDate());
        }
        getContentPane().add(dpPublished, "wrap");

        var lblWriter = new JLabel("Writer");
        lblWriter.setFont(UiOptions.genericFont);
        getContentPane().add(lblWriter);
        var allWriters = App.INSTANCE.getRepo().writers().getAll().toArray(new Writer[0]);
        var selectedWriterIndex = Utils.firstIndexOf(allWriters, it -> it.id() == text.getWriter());
        cbWriter = new JComboBox<>(allWriters);
        selectedWriterIndex.ifPresent(cbWriter::setSelectedIndex);
        cbWriter.setFont(UiOptions.genericFont);
        cbWriter.setEditable(false);
        getContentPane().add(cbWriter, "wrap");

        var btnSave = new JButton("Save");
        btnSave.setFont(UiOptions.genericFont);
        btnSave.addActionListener(__ -> save());
        getContentPane().add(btnSave);

        var btnCancel = new JButton("Cancel");
        btnCancel.setFont(UiOptions.genericFont);
        btnCancel.addActionListener(__ -> dispose());
        getContentPane().add(btnCancel);

        setSize(700, 320);
        setLocationRelativeTo(parentForm);
        setVisible(true);
    }
    
    private void save() {
        if (tfName.getText().isBlank()) {
            Utils.error(this, "Field \"Name\" must not be empty.");
            return;
        }

        if (dpWritingBegun.getDate() != null
                && dpWritingEnded.getDate() != null
                && dpWritingBegun.getDate().isAfter(dpWritingEnded.getDate())) {
            Utils.error(this, "The date in field \"Writing Begun\" " +
                    "must be before the date in field \"Writing Ended\".");
            return;
        }

        if (dpWritingBegun.getDate() != null
                && dpPublished.getDate() != null
                && dpWritingBegun.getDate().isAfter(dpPublished.getDate())) {
            Utils.error(this, "The date in field \"Writing Begun\" " +
                    "must be before the date in field \"Published\".");
            return;
        }

        if (dpWritingEnded.getDate() != null
                && dpPublished.getDate() != null
                && dpWritingEnded.getDate().isAfter(dpPublished.getDate())) {
            Utils.error(this, "The date in field \"Writing Ended\" " +
                    "must be before the date in field \"Published\".");
            return;
        }

        try {
            var updatedText = new Text(
                      text.getId(),
                    ((Genre) Objects.requireNonNull(cbGenre.getSelectedItem())).id(),
                    Utils.nullIfBlank(tfName.getText()),
                    Utils.extractSqlDate(dpWritingBegun),
                    Utils.extractSqlDate(dpWritingEnded),
                    Utils.extractSqlDate(dpPublished),
                    3, // todo: user input
                    ((Writer) Objects.requireNonNull(cbWriter.getSelectedItem())).id()
            );
            App.INSTANCE.getRepo().texts().update(updatedText);
            successListener.run();
            dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to save changes. Make sure all fields are filled with valid values.\n\n" + e,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

}
