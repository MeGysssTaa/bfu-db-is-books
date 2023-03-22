package me.darksidecode.bfu.books.ui.keeperType;

import lombok.NonNull;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.database.entity.KeeperType;

import javax.swing.*;
import java.awt.*;

public class DeleteKeeperTypeForm {

    private final Component parentFrame;
    private final KeeperType keeperType;
    private final Runnable successListener;

    public DeleteKeeperTypeForm(Component parentFrame, @NonNull KeeperType keeperType, @NonNull Runnable successListener) {
        this.parentFrame = parentFrame;
        this.keeperType = keeperType;
        this.successListener = successListener;

        var keeperTypeName = keeperType.name();
        var userResponse = JOptionPane.showConfirmDialog(
                parentFrame,
                "Do you really want to delete all information associated with keeper type " + keeperTypeName + "? " +
                        "This action cannot be undone!",
                "Deleting keeper type " + keeperTypeName,
                JOptionPane.YES_NO_OPTION
        );

        if (userResponse == JOptionPane.YES_OPTION) {
            confirmDelete();
        }
    }

    private void confirmDelete() {
        try {
            App.INSTANCE.getRepo().keeperTypes().deleteById(keeperType.id());
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
