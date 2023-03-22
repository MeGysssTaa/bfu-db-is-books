package me.darksidecode.bfu.books.database.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.darksidecode.bfu.books.App;

import java.sql.Date;

@Getter
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

    private transient Genre genreObj;
    private transient Writer writerObj;

    @Override
    public String toString() {
        var s = "";
        var w = getWriterObj();
        if (w == null) {
            s = "Unknown Writer";
        } else {
            s = w.firstName();
            if (w.patronymic() != null) {
                s += " " + w.patronymic();
            }
            s += " " + w.secondName();
        }
        s += " - \"" + name + "\"";
        if (published != null) {
            s += " (" + App.DATE_FORMAT.format(published) + ")";
        }
        s += "    |    ";
        var g = getGenreObj();
        if (g == null) {
            s += "Unknown Genre";
        } else {
            s += g.name();
        }
        return s;
    }
    
    public Genre getGenreObj() {
        if (genreObj == null) {
            genreObj = App.INSTANCE.getRepo().genres().getById(genre);
        }
        return genreObj;
    }

    public Writer getWriterObj() {
        if (writerObj == null) {
            writerObj = App.INSTANCE.getRepo().writers().getById(writer);
        }
        return writerObj;
    }

}
