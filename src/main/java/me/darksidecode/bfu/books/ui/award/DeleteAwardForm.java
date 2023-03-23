package me.darksidecode.bfu.books.ui.award;

import lombok.NonNull;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.database.entity.Award;

import javax.swing.*;
import java.awt.*;

public class DeleteAwardForm {

    private final Component parentFrame;
    private final Award award;
    private final Runnable successListener;

    public DeleteAwardForm(Component parentFrame, @NonNull Award award, @NonNull Runnable successListener) {
        this.parentFrame = parentFrame;
        this.award = award;
        this.successListener = successListener;

        var awardName = award.toString();
        var userResponse = JOptionPane.showConfirmDialog(
                parentFrame,
                "Do you really want to delete all information associated with award " + awardName + "? " +
                        "This action cannot be undone!",
                "Deleting award " + awardName,
                JOptionPane.YES_NO_OPTION
        );

        if (userResponse == JOptionPane.YES_OPTION) {
            confirmDelete();
        }
    }

    private void confirmDelete() {
        try {
            App.INSTANCE.getRepo().awards().delete(award);
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
