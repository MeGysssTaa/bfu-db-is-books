package me.darksidecode.bfu.books.ui.prize;

import lombok.NonNull;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.Utils;
import me.darksidecode.bfu.books.database.entity.Prize;
import me.darksidecode.bfu.books.ui.UiOptions;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class AddPrizeForm extends JFrame {

    private final Runnable successListener;
    private final JTextField tfName;

    public AddPrizeForm(Component parentForm, @NonNull Runnable successListener) {
        this.successListener = successListener;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new MigLayout());
        setTitle("Creating new prize");

        var lblFirstName = new JLabel("Name");
        lblFirstName.setFont(UiOptions.genericFont);
        getContentPane().add(lblFirstName);
        tfName = new JTextField(100);
        tfName.setFont(UiOptions.genericFont);
        getContentPane().add(tfName, "wrap");

        var btnSave = new JButton("Save");
        btnSave.setFont(UiOptions.genericFont);
        btnSave.addActionListener(__ -> save());
        getContentPane().add(btnSave);

        var btnCancel = new JButton("Cancel");
        btnCancel.setFont(UiOptions.genericFont);
        btnCancel.addActionListener(__ -> dispose());
        getContentPane().add(btnCancel);

        setSize(500, 125);
        setLocationRelativeTo(parentForm);
        setVisible(true);
    }

    private void save() {
        if (tfName.getText().isBlank()) {
            Utils.error(this, "Field \"Name\" must not be empty.");
            return;
        }

        if (tfName.getText().length() > 50) {
            Utils.error(this, "Length of \"Name\" must be at most 50 characters.");
            return;
        }

        try {
            var prize = new Prize(
                    -1L,
                    Utils.nullIfBlank(tfName.getText())
            );
            App.INSTANCE.getRepo().prizes().create(prize);
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
