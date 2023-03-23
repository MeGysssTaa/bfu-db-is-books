package me.darksidecode.bfu.books.database.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.darksidecode.bfu.books.App;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@RequiredArgsConstructor
public class Award {

    private final long writer;
    private final long prize;
    private final @NonNull Date date;
    private final BigDecimal prizeAmountDollars;

    private transient Writer writerObj;
    private transient Prize prizeObj;

    @Override
    public String toString() {
        var s = "";
        var w = getWriterObj();
        if (w == null) {
            s += "Unknown Writer";
        } else {
            s += w.firstName();
            if (w.patronymic() != null) {
                s += " " + w.patronymic();
            }
            s += " " + w.secondName();
        }
        s += " - ";
        var p = getPrizeObj();
        if (p == null) {
            s += "Unknown Prize";
        } else {
            s += p.name();
        }
        s += String.format(" (%s) - $%s", App.DATE_FORMAT.format(date), prizeAmountDollars);
        return s;
    }

    public Writer getWriterObj() {
        if (writerObj == null) {
            writerObj = App.INSTANCE.getRepo().writers().getById(writer);
        }
        return writerObj;
    }
    
    public Prize getPrizeObj() {
        if (prizeObj == null) {
            prizeObj = App.INSTANCE.getRepo().prizes().getById(prize);
        }
        return prizeObj;
    }

}
