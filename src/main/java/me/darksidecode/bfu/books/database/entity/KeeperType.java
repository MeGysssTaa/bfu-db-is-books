package me.darksidecode.bfu.books.database.entity;

public record KeeperType(
        long id,
        String name
) {

    @Override
    public String toString() {
        return name;
    }

}
