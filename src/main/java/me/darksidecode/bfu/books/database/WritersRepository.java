package me.darksidecode.bfu.books.database;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.darksidecode.bfu.books.Utils;
import me.darksidecode.bfu.books.database.entity.Writer;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class WritersRepository {

    private final @NonNull BooksDatabase db;

    @SneakyThrows
    public List<Writer> getAll() {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery("select * from writers");
        return Utils.readEntities(rs, WritersRepository::readOne);
    }

    @SneakyThrows
    public Writer getById(long writerId) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("select * from writers where id = ?");
        stmt.setLong(1, writerId);
        var rs = stmt.executeQuery();
        return rs.next() ? readOne(rs) : null;
    }

    @SneakyThrows
    public void create(@NonNull Writer writer) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("" +
                "insert into writers (first_name, second_name, patronymic, born, died)" +
                "values (?, ?, ?, ?, ?)"
        );

        stmt.setString(1, writer.firstName());
        stmt.setString(2, writer.secondName());
        stmt.setString(3, writer.patronymic());
        stmt.setDate(4, writer.born());
        stmt.setDate(5, writer.died());
        stmt.execute();
    }

    @SneakyThrows
    public void update(@NonNull Writer writer) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("" +
                "update writers set " +
                "first_name = ?, " +
                "second_name = ?, " +
                "patronymic = ?, " +
                "born = ?, " +
                "died = ? " +
                "where id = ?"
        );

        stmt.setString(1, writer.firstName());
        stmt.setString(2, writer.secondName());
        stmt.setString(3, writer.patronymic());
        stmt.setDate(4, writer.born());
        stmt.setDate(5, writer.died());
        stmt.setLong(6, writer.id());
        stmt.execute();
    }

    @SneakyThrows
    public void deleteById(long writerId) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("delete from writers where id = ?");
        stmt.setLong(1, writerId);
        stmt.execute();
    }

    @SneakyThrows
    public List<Writer> search(@NonNull String query) {
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
                        .append("first_name like ?")
                        .append(" or second_name like ?")
                        .append(" or patronymic like ?");
                for (int p = 0; p < 3; p++) {
                    prepStmtParams.add(token);
                }
            } else {
                // Search date.
                whereClause
                        .append("born = ?")
                        .append(" or died = ?");
                for (int p = 0; p < 2; p++) {
                    prepStmtParams.add(dateToken);
                }
            }

            whereClause.append(")");
        }

        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("select * from writers where " + whereClause);

        for (int i = 0; i < prepStmtParams.size(); i++) {
            stmt.setObject(i + 1, prepStmtParams.get(i));
        }

        return Utils.readEntities(stmt.executeQuery(), WritersRepository::readOne);
    }

    @SneakyThrows
    private static Writer readOne(ResultSet rs) {
        return new Writer(
                rs.getLong("id"),
                rs.getString("first_name"),
                rs.getString("second_name"),
                rs.getString("patronymic"),
                rs.getDate("born"),
                rs.getDate("died")
        );
    }

}
