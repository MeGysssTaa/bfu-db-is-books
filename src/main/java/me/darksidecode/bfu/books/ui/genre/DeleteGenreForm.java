package me.darksidecode.bfu.books.ui.genre;

import lombok.NonNull;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.database.entity.Genre;

import javax.swing.*;
import java.awt.*;

public class DeleteGenreForm {

    private final Component parentFrame;
    private final Genre genre;
    private final Runnable successListener;

    public DeleteGenreForm(Component parentFrame, @NonNull Genre genre, @NonNull Runnable successListener) {
        this.parentFrame = parentFrame;
        this.genre = genre;
        this.successListener = successListener;

        var genreName = genre.name();
        var userResponse = JOptionPane.showConfirmDialog(
                parentFrame,
                "Do you really want to delete all information associated with genre " + genreName + "? " +
                        "This action cannot be undone!",
                "Deleting genre " + genreName,
                JOptionPane.YES_NO_OPTION
        );

        if (userResponse == JOptionPane.YES_OPTION) {
            confirmDelete();
        }
    }

    private void confirmDelete() {
        try {
            App.INSTANCE.getDatabase().genres().deleteById(genre.id());
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
