package me.darksidecode.bfu.books.ui.writer;

import com.github.lgooddatepicker.components.DatePicker;
import lombok.NonNull;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.Utils;
import me.darksidecode.bfu.books.database.entity.Writer;
import me.darksidecode.bfu.books.ui.UiOptions;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class AddWriterForm extends JFrame {

    private final Runnable successListener;
    private final JTextField tfFirstName, tfSecondName, tfPatronymic;
    private final DatePicker dpBorn, dpDied;

    public AddWriterForm(Component parentForm, @NonNull Runnable successListener) {
        this.successListener = successListener;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new MigLayout());
        setTitle("Creating new writer");

        var lblFirstName = new JLabel("First Name");
        lblFirstName.setFont(UiOptions.genericFont);
        getContentPane().add(lblFirstName);
        tfFirstName = new JTextField(100);
        tfFirstName.setFont(UiOptions.genericFont);
        getContentPane().add(tfFirstName, "wrap");

        var lblSecondName = new JLabel("Second Name");
        lblSecondName.setFont(UiOptions.genericFont);
        getContentPane().add(lblSecondName);
        tfSecondName = new JTextField(100);
        tfSecondName.setFont(UiOptions.genericFont);
        getContentPane().add(tfSecondName, "wrap");

        var lblPatronymic = new JLabel("Patronymic");
        lblPatronymic.setFont(UiOptions.genericFont);
        getContentPane().add(lblPatronymic);
        tfPatronymic = new JTextField(100);
        tfPatronymic.setFont(UiOptions.genericFont);
        getContentPane().add(tfPatronymic, "wrap");

        var lblBorn = new JLabel("Born");
        lblBorn.setFont(UiOptions.genericFont);
        getContentPane().add(lblBorn);
        dpBorn = new DatePicker(UiOptions.datePickerSettings(true));
        getContentPane().add(dpBorn, "wrap");

        var lblDied = new JLabel("Died");
        lblDied.setFont(UiOptions.genericFont);
        getContentPane().add(lblDied);
        dpDied = new DatePicker(UiOptions.datePickerSettings(true));
        getContentPane().add(dpDied, "wrap");

        var btnSave = new JButton("Save");
        btnSave.setFont(UiOptions.genericFont);
        btnSave.addActionListener(__ -> save());
        getContentPane().add(btnSave);

        var btnCancel = new JButton("Cancel");
        btnCancel.setFont(UiOptions.genericFont);
        btnCancel.addActionListener(__ -> dispose());
        getContentPane().add(btnCancel);

        setSize(500, 280);
        setLocationRelativeTo(parentForm);
        setVisible(true);
    }

    private void save() {
        if (tfFirstName.getText().isBlank()) {
            Utils.error(this, "Field \"First Name\" must not be empty.");
            return;
        }

        if (tfFirstName.getText().length() > 50) {
            Utils.error(this, "Length of \"First Name\" must be at most 50 characters.");
            return;
        }

        if (tfSecondName.getText().isBlank()) {
            Utils.error(this, "Field \"Second Name\" must not be empty.");
            return;
        }

        if (tfSecondName.getText().length() > 50) {
            Utils.error(this, "Length of \"Second Name\" must be at most 50 characters.");
            return;
        }

        if (tfPatronymic.getText().length() > 50) {
            Utils.error(this, "Length of \"Patronymic\" must be at most 50 characters.");
            return;
        }

        if (dpBorn.getDate() != null
                && dpDied.getDate() != null
                && dpBorn.getDate().isAfter(dpDied.getDate())) {
            Utils.error(this, "The date in field \"Born\" " +
                    "must be before the date in field \"Died\".");
            return;
        }

        try {
            var writer = new Writer(
                    -1L,
                    Utils.nullIfBlank(tfFirstName.getText()),
                    Utils.nullIfBlank(tfSecondName.getText()),
                    Utils.nullIfBlank(tfPatronymic.getText()),
                    Utils.extractSqlDate(dpBorn),
                    Utils.extractSqlDate(dpDied)
            );
            App.INSTANCE.getRepo().writers().create(writer);
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
