package me.darksidecode.bfu.books.ui.text;

import lombok.NonNull;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.database.entity.Text;

import javax.swing.*;
import java.awt.*;

public class DeleteTextForm {

    private final Component parentFrame;
    private final Text text;
    private final Runnable successListener;

    public DeleteTextForm(Component parentFrame, @NonNull Text text, @NonNull Runnable successListener) {
        this.parentFrame = parentFrame;
        this.text = text;
        this.successListener = successListener;

        var textName = text.getName();
        var userResponse = JOptionPane.showConfirmDialog(
                parentFrame,
                "Do you really want to delete all information associated with text " + textName + "? " +
                        "This action cannot be undone!",
                "Deleting text " + textName,
                JOptionPane.YES_NO_OPTION
        );

        if (userResponse == JOptionPane.YES_OPTION) {
            confirmDelete();
        }
    }

    private void confirmDelete() {
        try {
            App.INSTANCE.getRepo().texts().deleteById(text.getId());
            successListener.run();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    parentFrame,
                    "Failed to delete the given entity. Please refresh and ensure it still exists.\n\n" + e,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

}
