package me.darksidecode.bfu.books.database;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.darksidecode.bfu.books.Utils;
import me.darksidecode.bfu.books.database.entity.Prize;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class PrizesRepository {

    private final @NonNull BooksDatabase db;

    @SneakyThrows
    public Collection<Prize> getAll() {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery("select * from prizes");
        return Utils.readEntities(rs, PrizesRepository::readOne);
    }
    
    @SneakyThrows
    public Prize getById(long prizeId) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("select * from prizes where id = ?");
        stmt.setLong(1, prizeId);
        var rs = stmt.executeQuery();
        return rs.next() ? readOne(rs) : null;
    }

    @SneakyThrows
    public void update(@NonNull Prize prize) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("" +
                "update prizes set " +
                "name = ? " +
                "where id = ?"
        );

        stmt.setString(1, prize.name());
        stmt.setLong(2, prize.id());
        stmt.execute();
    }

    @SneakyThrows
    public void create(@NonNull Prize prize) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("" +
                "insert into prizes (name)" +
                "values (?)"
        );

        stmt.setString(1, prize.name());
        stmt.execute();
    }

    @SneakyThrows
    public void deleteById(long prizeId) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("delete from prizes where id = ?");
        stmt.setLong(1, prizeId);
        stmt.execute();
    }

    @SneakyThrows
    public Collection<Prize> search(@NonNull String query) {
        query = query.toLowerCase().trim();
        String[] tokens = query.split(" ");

        var whereClause = new StringBuilder();
        var prepStmtParams = new ArrayList<>();

        for (int i = 0; i < tokens.length; i++) {
            var token = "%" + tokens[i] + "%";
            if (i > 0) {
                whereClause.append(" or ");
            }
            whereClause.append("(name like ?)");
            prepStmtParams.add(token);
        }

        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("select * from prizes where " + whereClause);

        for (int i = 0; i < prepStmtParams.size(); i++) {
            stmt.setObject(i + 1, prepStmtParams.get(i));
        }

        return Utils.readEntities(stmt.executeQuery(), PrizesRepository::readOne);
    }

    @SneakyThrows
    private static Prize readOne(ResultSet rs) {
        return new Prize(
                rs.getLong("id"),
                rs.getString("name")
        );
    }

}
