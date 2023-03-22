package me.darksidecode.bfu.books.database.entity;

import lombok.RequiredArgsConstructor;
import me.darksidecode.bfu.books.App;

import java.sql.Date;

@RequiredArgsConstructor
public class Text {

    private final long id;
    private final long genre;
    private final String name;
    private final Date writingBegun;
    private final Date writingEnded;
    private final Date published;
    private final long keeper;
    private final long writer;

    private Writer writerObj;

    @Override
    public String toString() {
        var s = String.format("\"%s\"", name);
        if (published != null) {
            s += String.format(" (%s)", App.DATE_FORMAT.format(published));
        }
        return s;
    }

    public Writer getWriter() {
        if (writerObj == null) {
            writerObj = App.INSTANCE.getDatabase().writers().getById(writer);
        }
        return writerObj;
    }

}
