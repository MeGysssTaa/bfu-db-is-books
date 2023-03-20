package me.darksidecode.bfu.books.ui.writer;

import lombok.NonNull;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.database.entity.Writer;

import javax.swing.*;
import java.awt.*;

public class DeleteWriterForm {

    private final Component parentFrame;
    private final Writer writer;
    private final Runnable successListener;

    public DeleteWriterForm(Component parentFrame, @NonNull Writer writer, @NonNull Runnable successListener) {
        this.parentFrame = parentFrame;
        this.writer = writer;
        this.successListener = successListener;

        var writerName = writer.firstName() + " " + writer.secondName();
        var userResponse = JOptionPane.showConfirmDialog(
                parentFrame,
                "Do you really want to delete all information associated with writer " + writerName + "? " +
                        "This action cannot be undone!",
                "Deleting writer " + writerName,
                JOptionPane.YES_NO_OPTION
        );

        if (userResponse == JOptionPane.YES_OPTION) {
            confirmDelete();
        }
    }

    private void confirmDelete() {
        try {
            App.INSTANCE.getDatabase().writers().deleteWriterById(writer.id());
            successListener.run();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    parentFrame,
                    "Failed to delete writer. Please refresh and ensure they still exist.\n\n" + e,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

}
