package me.darksidecode.bfu.books.database.entity;

import me.darksidecode.bfu.books.App;

import java.sql.Date;

public record Writer(
        long id,
        String firstName,
        String secondName,
        String patronymic,
        Date born,
        Date died
) {

    @Override
    public String toString() {
        return String.format("%s (%s -- %s)",
                fullName(),
                born != null ? App.DATE_FORMAT.format(born) : "??/??/????",
                died != null ? App.DATE_FORMAT.format(died) : "??/??/????"
        );
    }

    public String fullName() {
        return String.format("%s %s %s",
                firstName != null ? firstName : "-",
                patronymic != null ? patronymic : "-",
                secondName != null ? secondName : "-"
        );
    }

}
