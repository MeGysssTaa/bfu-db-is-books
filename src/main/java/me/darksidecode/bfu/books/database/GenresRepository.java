package me.darksidecode.bfu.books.database;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.darksidecode.bfu.books.Utils;
import me.darksidecode.bfu.books.database.entity.Genre;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class GenresRepository {

    private final @NonNull BooksDatabase db;

    @SneakyThrows
    public Collection<Genre> getAll() {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery("select * from genres");
        return Utils.readEntities(rs, GenresRepository::readOne);
    }
    
    @SneakyThrows
    public Genre getById(long genreId) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("select * from genres where id = ?");
        stmt.setLong(1, genreId);
        var rs = stmt.executeQuery();
        return rs.next() ? readOne(rs) : null;
    }

    @SneakyThrows
    public void update(@NonNull Genre genre) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("" +
                "update genres set " +
                "name = ? " +
                "where id = ?"
        );

        stmt.setString(1, genre.name());
        stmt.setLong(2, genre.id());
        stmt.execute();
    }

    @SneakyThrows
    public void create(@NonNull Genre genre) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("" +
                "insert into genres (name)" +
                "values (?)"
        );

        stmt.setString(1, genre.name());
        stmt.execute();
    }

    @SneakyThrows
    public void deleteById(long genreId) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("delete from genres where id = ?");
        stmt.setLong(1, genreId);
        stmt.execute();
    }

    @SneakyThrows
    public Collection<Genre> search(@NonNull String query) {
        query = query.toLowerCase().trim();
        String[] tokens = query.split(" ");

        var whereClause = new StringBuilder();
        var prepStmtParams = new ArrayList<>();

        for (int i = 0; i < tokens.length; i++) {
            var token = tokens[i];
            if (i > 0) {
                whereClause.append(" or ");
            }
            whereClause.append("(name like ?)");
            prepStmtParams.add(token);
        }

        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("select * from genres where " + whereClause);

        for (int i = 0; i < prepStmtParams.size(); i++) {
            stmt.setObject(i + 1, prepStmtParams.get(i));
        }

        return Utils.readEntities(stmt.executeQuery(), GenresRepository::readOne);
    }

    @SneakyThrows
    private static Genre readOne(ResultSet rs) {
        return new Genre(
                rs.getLong("id"),
                rs.getString("name")
        );
    }

}
