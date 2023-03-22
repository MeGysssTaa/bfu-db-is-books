package me.darksidecode.bfu.books.ui.country;

import lombok.NonNull;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.database.entity.Country;

import javax.swing.*;
import java.awt.*;

public class DeleteCountryForm {

    private final Component parentFrame;
    private final Country country;
    private final Runnable successListener;

    public DeleteCountryForm(Component parentFrame, @NonNull Country country, @NonNull Runnable successListener) {
        this.parentFrame = parentFrame;
        this.country = country;
        this.successListener = successListener;

        var countryName = country.name();
        var userResponse = JOptionPane.showConfirmDialog(
                parentFrame,
                "Do you really want to delete all information associated with country " + countryName + "? " +
                        "This action cannot be undone!",
                "Deleting country " + countryName,
                JOptionPane.YES_NO_OPTION
        );

        if (userResponse == JOptionPane.YES_OPTION) {
            confirmDelete();
        }
    }

    private void confirmDelete() {
        try {
            App.INSTANCE.getDatabase().countries().deleteById(country.id());
            successListener.run();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    parentFrame,
                    "Failed to delete the given entity. Please refresh and ensure they still exist.\n\n" + e,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

}
