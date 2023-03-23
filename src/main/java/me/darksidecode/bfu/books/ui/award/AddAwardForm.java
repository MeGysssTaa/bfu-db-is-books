package me.darksidecode.bfu.books.ui.award;

import com.github.lgooddatepicker.components.DatePicker;
import lombok.NonNull;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.Utils;
import me.darksidecode.bfu.books.database.entity.Award;
import me.darksidecode.bfu.books.database.entity.Prize;
import me.darksidecode.bfu.books.database.entity.Writer;
import me.darksidecode.bfu.books.ui.UiOptions;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Objects;

public class AddAwardForm extends JFrame {

    private final Runnable successListener;
    private final JComboBox<Writer> cbWriter;
    private final JComboBox<Prize> cbPrize;
    private final DatePicker dpDate;
    private final JTextField tfPrizeAmountDollars;

    public AddAwardForm(Component parentForm, @NonNull Runnable successListener) {
        this.successListener = successListener;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new MigLayout());
        setTitle("Creating new award");

        var lblWriter = new JLabel("Writer");
        lblWriter.setFont(UiOptions.genericFont);
        getContentPane().add(lblWriter);
        var allWriters = App.INSTANCE.getRepo().writers().getAll().toArray(new Writer[0]);
        cbWriter = new JComboBox<>(allWriters);
        cbWriter.setFont(UiOptions.genericFont);
        cbWriter.setEditable(false);
        getContentPane().add(cbWriter, "wrap");

        var lblPrize = new JLabel("Prize");
        lblPrize.setFont(UiOptions.genericFont);
        getContentPane().add(lblPrize);
        var allPrizes = App.INSTANCE.getRepo().prizes().getAll().toArray(new Prize[0]);
        cbPrize = new JComboBox<>(allPrizes);
        cbPrize.setFont(UiOptions.genericFont);
        cbPrize.setEditable(false);
        getContentPane().add(cbPrize, "wrap");

        var lblDate = new JLabel("Date");
        lblDate.setFont(UiOptions.genericFont);
        getContentPane().add(lblDate);
        dpDate = new DatePicker(UiOptions.datePickerSettings(true));
        getContentPane().add(dpDate, "wrap");

        var lblPrizeAmountDollars = new JLabel("Amount (USD)");
        lblPrizeAmountDollars.setFont(UiOptions.genericFont);
        getContentPane().add(lblPrizeAmountDollars);
        tfPrizeAmountDollars = new JTextField(30);
        tfPrizeAmountDollars.setFont(UiOptions.genericFont);
        getContentPane().add(tfPrizeAmountDollars, "wrap");

        var btnSave = new JButton("Save");
        btnSave.setFont(UiOptions.genericFont);
        btnSave.addActionListener(__ -> save());
        getContentPane().add(btnSave);

        var btnCancel = new JButton("Cancel");
        btnCancel.setFont(UiOptions.genericFont);
        btnCancel.addActionListener(__ -> dispose());
        getContentPane().add(btnCancel);

        setSize(650, 250);
        setLocationRelativeTo(parentForm);
        setVisible(true);
    }

    private void save() {
        try {
            var award = new Award(
                    ((Writer) Objects.requireNonNull(cbWriter.getSelectedItem())).id(),
                    ((Prize) Objects.requireNonNull(cbPrize.getSelectedItem())).id(),
                    Objects.requireNonNull(Utils.extractSqlDate(dpDate)),
                    new BigDecimal(tfPrizeAmountDollars.getText().replace(',', '.'))
            );
            App.INSTANCE.getRepo().awards().create(award);
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
