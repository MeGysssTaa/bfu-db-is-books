package me.darksidecode.bfu.books.database;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.darksidecode.bfu.books.App;
import me.darksidecode.bfu.books.Utils;
import me.darksidecode.bfu.books.database.entity.Text;

import java.sql.Date;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.Collection;

@RequiredArgsConstructor
public class TextsRepository {

    private final @NonNull BooksDatabase db;

    @SneakyThrows
    public Collection<Text> getAllTexts() {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery("select * from texts");
        return Utils.readEntities(rs, TextsRepository::readText);
    }

    private Date parseSqlDateToken(String token) {
        try {
            java.util.Date javaDate = App.DATE_FORMAT.parse(token);
            return new Date(javaDate.toInstant().toEpochMilli());
        } catch (ParseException e) {
            return null;
        }
    }

    @SneakyThrows
    private static Text readText(ResultSet rs) {
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
