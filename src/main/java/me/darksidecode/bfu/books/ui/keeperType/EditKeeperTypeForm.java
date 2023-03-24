package me.darksidecode.bfu.books.ui.keeperType;

import lombok.NonNull;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.Utils;
import me.darksidecode.bfu.books.database.entity.KeeperType;
import me.darksidecode.bfu.books.ui.UiOptions;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class EditKeeperTypeForm extends JFrame {

    private final KeeperType keeperType;
    private final Runnable successListener;
    private final JTextField tfName;

    public EditKeeperTypeForm(Component parentForm, @NonNull KeeperType keeperType, @NonNull Runnable successListener) {
        this.keeperType = keeperType;
        this.successListener = successListener;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new MigLayout());
        setTitle("Editing keeper type " + keeperType.name());

//        var lblId = new JLabel("ID");
//        lblId.setFont(UiOptions.genericFont);
//        getContentPane().add(lblId);
//        var tfId = new JTextField(String.valueOf(keeperType.id()), 100);
//        tfId.setFont(UiOptions.genericFont);
//        tfId.setEnabled(false);
//        getContentPane().add(tfId, "wrap");

        var lblName = new JLabel("Name");
        lblName.setFont(UiOptions.genericFont);
        getContentPane().add(lblName);
        tfName = new JTextField(keeperType.name(), 100);
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

        try {
            var updatedKeeperType = new KeeperType(
                    keeperType.id(),
                    Utils.nullIfBlank(tfName.getText())
            );
            App.INSTANCE.getRepo().keeperTypes().update(updatedKeeperType);
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
