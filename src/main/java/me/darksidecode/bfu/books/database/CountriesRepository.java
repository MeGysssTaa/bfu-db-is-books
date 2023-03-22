package me.darksidecode.bfu.books.database;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.darksidecode.bfu.books.Utils;
import me.darksidecode.bfu.books.database.entity.Country;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class CountriesRepository {

    private final @NonNull BooksDatabase db;

    @SneakyThrows
    public Collection<Country> getAll() {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery("select * from countries");
        return Utils.readEntities(rs, CountriesRepository::readOne);
    }
    
    @SneakyThrows
    public Country getById(long countryId) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("select * from countries where id = ?");
        stmt.setLong(1, countryId);
        var rs = stmt.executeQuery();
        return rs.next() ? readOne(rs) : null;
    }

    @SneakyThrows
    public void update(@NonNull Country country) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("" +
                "update countries set " +
                "name = ? " +
                "where id = ?"
        );

        stmt.setString(1, country.name());
        stmt.setLong(2, country.id());
        stmt.execute();
    }

    @SneakyThrows
    public void create(@NonNull Country country) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("" +
                "insert into countries (name)" +
                "values (?)"
        );

        stmt.setString(1, country.name());
        stmt.execute();
    }

    @SneakyThrows
    public void deleteById(long countryId) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("delete from countries where id = ?");
        stmt.setLong(1, countryId);
        stmt.execute();
    }

    @SneakyThrows
    public Collection<Country> search(@NonNull String query) {
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
        var stmt = conn.prepareStatement("select * from countries where " + whereClause);

        for (int i = 0; i < prepStmtParams.size(); i++) {
            stmt.setObject(i + 1, prepStmtParams.get(i));
        }

        return Utils.readEntities(stmt.executeQuery(), CountriesRepository::readOne);
    }

    @SneakyThrows
    private static Country readOne(ResultSet rs) {
        return new Country(
                rs.getLong("id"),
                rs.getString("name")
        );
    }

}
