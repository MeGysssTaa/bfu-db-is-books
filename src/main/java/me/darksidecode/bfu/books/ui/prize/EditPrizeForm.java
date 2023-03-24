package me.darksidecode.bfu.books.ui.prize;

import lombok.NonNull;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.Utils;
import me.darksidecode.bfu.books.database.entity.Prize;
import me.darksidecode.bfu.books.ui.UiOptions;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class EditPrizeForm extends JFrame {

    private final Prize prize;
    private final Runnable successListener;
    private final JTextField tfName;

    public EditPrizeForm(Component parentForm, @NonNull Prize prize, @NonNull Runnable successListener) {
        this.prize = prize;
        this.successListener = successListener;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new MigLayout());
        setTitle("Editing prize " + prize.name());

//        var lblId = new JLabel("ID");
//        lblId.setFont(UiOptions.genericFont);
//        getContentPane().add(lblId);
//        var tfId = new JTextField(String.valueOf(prize.id()), 100);
//        tfId.setFont(UiOptions.genericFont);
//        tfId.setEnabled(false);
//        getContentPane().add(tfId, "wrap");

        var lblName = new JLabel("Name");
        lblName.setFont(UiOptions.genericFont);
        getContentPane().add(lblName);
        tfName = new JTextField(prize.name(), 100);
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

        setSize(500, 130);
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
            var updatedPrize = new Prize(
                    prize.id(),
                    Utils.nullIfBlank(tfName.getText())
            );
            App.INSTANCE.getRepo().prizes().update(updatedPrize);
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
