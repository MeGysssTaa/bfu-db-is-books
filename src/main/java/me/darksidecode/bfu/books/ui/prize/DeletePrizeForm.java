package me.darksidecode.bfu.books.ui.prize;

import lombok.NonNull;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.database.entity.Prize;

import javax.swing.*;
import java.awt.*;

public class DeletePrizeForm {

    private final Component parentFrame;
    private final Prize prize;
    private final Runnable successListener;

    public DeletePrizeForm(Component parentFrame, @NonNull Prize prize, @NonNull Runnable successListener) {
        this.parentFrame = parentFrame;
        this.prize = prize;
        this.successListener = successListener;

        var prizeName = prize.name();
        var userResponse = JOptionPane.showConfirmDialog(
                parentFrame,
                "Do you really want to delete all information associated with prize " + prizeName + "? " +
                        "This action cannot be undone!",
                "Deleting prize " + prizeName,
                JOptionPane.YES_NO_OPTION
        );

        if (userResponse == JOptionPane.YES_OPTION) {
            confirmDelete();
        }
    }

    private void confirmDelete() {
        try {
            App.INSTANCE.getRepo().prizes().deleteById(prize.id());
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
