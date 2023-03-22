package me.darksidecode.bfu.books.database.entity;

public record Genre(
        long id,
        String name
) {

    @Override
    public String toString() {
        return name;
    }

}
