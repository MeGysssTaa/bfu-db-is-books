package me.darksidecode.bfu.books.database;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.darksidecode.bfu.books.Utils;
import me.darksidecode.bfu.books.database.entity.Text;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TextsRepository {

    private final @NonNull BooksDatabase db;

    @SneakyThrows
    public List<Text> getAll() {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery("select * from texts");
        return Utils.readEntities(rs, TextsRepository::readOne);
    }
    
    @SneakyThrows
    public Text getById(long textId) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("select * from texts where id = ?");
        stmt.setLong(1, textId);
        var rs = stmt.executeQuery();
        return rs.next() ? readOne(rs) : null;
    }

    @SneakyThrows
    public void create(@NonNull Text text) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("" +
                "insert into texts (genre, name, writing_begun, writing_ended, published, keeper, writer)" +
                "values (?, ?, ?, ?, ?, ?, ?)"
        );

        stmt.setLong(1, text.getGenre());
        stmt.setString(2, text.getName());
        stmt.setDate(3, text.getWritingBegun());
        stmt.setDate(4, text.getWritingEnded());
        stmt.setDate(5, text.getPublished());
        stmt.setLong(6, text.getKeeper());
        stmt.setLong(7, text.getWriter());
        stmt.execute();
    }

    @SneakyThrows
    public void update(@NonNull Text text) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("" +
                "update texts set " +
                "genre = ?, " +
                "name = ?, " +
                "writing_begun = ?, " +
                "writing_ended = ?, " +
                "published = ?, " +
                "keeper = ?, " +
                "writer = ? " +
                "where id = ?"
        );

        stmt.setLong(1, text.getGenre());
        stmt.setString(2, text.getName());
        stmt.setDate(3, text.getWritingBegun());
        stmt.setDate(4, text.getWritingEnded());
        stmt.setDate(5, text.getPublished());
        stmt.setLong(6, text.getKeeper());
        stmt.setLong(7, text.getWriter());
        stmt.setLong(8, text.getId());
        stmt.execute();
    }

    @SneakyThrows
    public void deleteById(long textId) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("delete from texts where id = ?");
        stmt.setLong(1, textId);
        stmt.execute();
    }

    @SneakyThrows
    public List<Text> search(@NonNull String query) {
        query = query.toLowerCase().trim();
        String[] tokens = query.split(" ");

        var whereClause = new StringBuilder();
        var prepStmtParams = new ArrayList<>();

        for (int i = 0; i < tokens.length; i++) {
            var token = tokens[i];
            var dateToken = Utils.parseSqlDateToken(token);

            if (i > 0) {
                whereClause.append(" or ");
            }

            whereClause.append("(");

            if (dateToken == null) {
                // Search text.
                token = "%" + token + "%";
                whereClause
                        .append("writers.first_name like ?")
                        .append(" or writers.second_name like ?")
                        .append(" or writers.patronymic like ?")
                        .append(" or texts.name like ?")
                        .append(" or genres.name like ?");
                for (int p = 0; p < 5; p++) {
                    prepStmtParams.add(token);
                }
            } else {
                // Search date.
                whereClause
                        .append("texts.writing_begun = ?")
                        .append(" or texts.writing_ended = ?")
                        .append(" or texts.published = ?");
                for (int p = 0; p < 3; p++) {
                    prepStmtParams.add(dateToken);
                }
            }

            whereClause.append(")");
        }

        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement(
                """
                select * from texts
                left join genres on genres.id = texts.genre
                left join keepers on keepers.id = texts.keeper
                left join writers on writers.id = texts.writer
                """ + "where " + whereClause
        );

        for (int i = 0; i < prepStmtParams.size(); i++) {
            stmt.setObject(i + 1, prepStmtParams.get(i));
        }

        return Utils.readEntities(stmt.executeQuery(), TextsRepository::readOne);
    }

    @SneakyThrows
    private static Text readOne(ResultSet rs) {
        return new Text(
                rs.getLong("id"),
                rs.getLong("genre"),
                rs.getString("name"),
                rs.getDate("writing_begun"),
                rs.getDate("writing_ended"),
                rs.getDate("published"),
                rs.getLong("keeper"),
                rs.getLong("writer")
        );
    }

}
