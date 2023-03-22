package me.darksidecode.bfu.books.database.entity;

public record Prize(
        long id,
        String name
) {

    @Override
    public String toString() {
        return name;
    }

}
