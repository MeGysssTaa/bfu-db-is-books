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

public class EditWriterForm extends JFrame {

    private final Writer writer;
    private final Runnable successListener;
    private final JTextField tfFirstName, tfSecondName, tfPatronymic;
    private final DatePicker dpBorn, dpDied;

    public EditWriterForm(Component parentForm, @NonNull Writer writer, @NonNull Runnable successListener) {
        this.writer = writer;
        this.successListener = successListener;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new MigLayout());
        setTitle("Editing writer " + writer.fullName());

//        var lblId = new JLabel("ID");
//        lblId.setFont(UiOptions.genericFont);
//        getContentPane().add(lblId);
//        var tfId = new JTextField(String.valueOf(writer.id()), 100);
//        tfId.setFont(UiOptions.genericFont);
//        tfId.setEnabled(false);
//        getContentPane().add(tfId, "wrap");

        var lblFirstName = new JLabel("First Name");
        lblFirstName.setFont(UiOptions.genericFont);
        getContentPane().add(lblFirstName);
        tfFirstName = new JTextField(writer.firstName(), 100);
        tfFirstName.setFont(UiOptions.genericFont);
        getContentPane().add(tfFirstName, "wrap");
        
        var lblSecondName = new JLabel("Second Name");
        lblSecondName.setFont(UiOptions.genericFont);
        getContentPane().add(lblSecondName);
        tfSecondName = new JTextField(writer.secondName(), 100);
        tfSecondName.setFont(UiOptions.genericFont);
        getContentPane().add(tfSecondName, "wrap");
        
        var lblPatronymic = new JLabel("Patronymic");
        lblPatronymic.setFont(UiOptions.genericFont);
        getContentPane().add(lblPatronymic);
        tfPatronymic = new JTextField(writer.patronymic(), 100);
        tfPatronymic.setFont(UiOptions.genericFont);
        getContentPane().add(tfPatronymic, "wrap");

        var lblBorn = new JLabel("Born");
        lblBorn.setFont(UiOptions.genericFont);
        getContentPane().add(lblBorn);
        dpBorn = new DatePicker(UiOptions.datePickerSettings(true));
        if (writer.born() != null) {
            dpBorn.setDate(writer.born().toLocalDate());
        }
        getContentPane().add(dpBorn, "wrap");
        
        var lblDied = new JLabel("Died");
        lblDied.setFont(UiOptions.genericFont);
        getContentPane().add(lblDied);
        dpDied = new DatePicker(UiOptions.datePickerSettings(true));
        if (writer.died() != null) {
            dpDied.setDate(writer.died().toLocalDate());
        }
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
        try {
            var updatedWriter = new Writer(
                    writer.id(),
                    Utils.nullIfBlank(tfFirstName.getText()),
                    Utils.nullIfBlank(tfSecondName.getText()),
                    Utils.nullIfBlank(tfPatronymic.getText()),
                    Utils.extractSqlDate(dpBorn),
                    Utils.extractSqlDate(dpDied)
            );
            App.INSTANCE.getRepo().writers().update(updatedWriter);
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
