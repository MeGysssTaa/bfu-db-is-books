package me.darksidecode.bfu.books.database;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.darksidecode.bfu.books.Utils;
import me.darksidecode.bfu.books.database.entity.KeeperType;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class KeeperTypesRepository {

    private final @NonNull BooksDatabase db;

    @SneakyThrows
    public Collection<KeeperType> getAll() {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery("select * from keeper_types");
        return Utils.readEntities(rs, KeeperTypesRepository::readOne);
    }

    @SneakyThrows
    public void update(@NonNull KeeperType keeperType) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("" +
                "update keeper_types set " +
                "name = ? " +
                "where id = ?"
        );

        stmt.setString(1, keeperType.name());
        stmt.setLong(2, keeperType.id());
        stmt.execute();
    }

    @SneakyThrows
    public void create(@NonNull KeeperType keeperType) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("" +
                "insert into keeper_types (name)" +
                "values (?)"
        );

        stmt.setString(1, keeperType.name());
        stmt.execute();
    }

    @SneakyThrows
    public void deleteById(long keeperTypeId) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("delete from keeper_types where id = ?");
        stmt.setLong(1, keeperTypeId);
        stmt.execute();
    }

    @SneakyThrows
    public Collection<KeeperType> search(@NonNull String query) {
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
        var stmt = conn.prepareStatement("select * from keeper_types where " + whereClause);

        for (int i = 0; i < prepStmtParams.size(); i++) {
            stmt.setObject(i + 1, prepStmtParams.get(i));
        }

        return Utils.readEntities(stmt.executeQuery(), KeeperTypesRepository::readOne);
    }

    @SneakyThrows
    private static KeeperType readOne(ResultSet rs) {
        return new KeeperType(
                rs.getLong("id"),
                rs.getString("name")
        );
    }

}
