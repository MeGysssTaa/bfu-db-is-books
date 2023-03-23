package me.darksidecode.bfu.books.database;

import lombok.Cleanup;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.darksidecode.bfu.books.Utils;
import me.darksidecode.bfu.books.database.entity.Award;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AwardsRepository {

    private final @NonNull BooksDatabase db;

    @SneakyThrows
    public List<Award> getAll() {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery("select * from awards");
        return Utils.readEntities(rs, AwardsRepository::readOne);
    }

    @SneakyThrows
    public void create(@NonNull Award award) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement("" +
                "insert into awards (writer, prize, date, prize_amount_dollars) " +
                "values (?, ?, ?, ?)"
        );

        stmt.setLong(1, award.getWriter());
        stmt.setLong(2, award.getPrize());
        stmt.setDate(3, award.getDate());
        stmt.setBigDecimal(4, award.getPrizeAmountDollars());
        stmt.execute();
    }
    
    @SneakyThrows
    public void update(@NonNull Award oldAward, @NonNull Award newAward) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement(
                """
                update awards
                set writer = ?, prize = ?, date = ?, prize_amount_dollars = ?
                where writer = ? and prize = ? and date = ? and prize_amount_dollars = ?
                """
        );

        stmt.setLong(1, newAward.getWriter());
        stmt.setLong(2, newAward.getPrize());
        stmt.setDate(3, newAward.getDate());
        stmt.setBigDecimal(4, newAward.getPrizeAmountDollars());
        stmt.setLong(5, oldAward.getWriter());
        stmt.setLong(6, oldAward.getPrize());
        stmt.setDate(7, oldAward.getDate());
        stmt.setBigDecimal(8, oldAward.getPrizeAmountDollars());
        stmt.execute();
    }

    @SneakyThrows
    public void delete(@NonNull Award award) {
        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement(
                """
                delete from awards
                where writer = ?
                  and prize = ?
                  and date = ?
                  and prize_amount_dollars = ?
                """
        );
        stmt.setLong(1, award.getWriter());
        stmt.setLong(2, award.getPrize());
        stmt.setDate(3, award.getDate());
        stmt.setBigDecimal(4, award.getPrizeAmountDollars());
        stmt.execute();
    }

    @SneakyThrows
    public List<Award> search(@NonNull String query) {
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
                // Search award.
                token = "%" + token + "%";
                whereClause
                        .append("writers.first_name like ?")
                        .append(" or writers.second_name like ?")
                        .append(" or writers.patronymic like ?")
                        .append(" or prizes.name like ?");
                for (int p = 0; p < 4; p++) {
                    prepStmtParams.add(token);
                }
            } else {
                // Search date.
                whereClause.append("awards.date = ?");
                prepStmtParams.add(dateToken);
            }

            whereClause.append(")");
        }

        @Cleanup var conn = db.getConnection();
        var stmt = conn.prepareStatement(
                """
                select * from awards
                left join prizes on prizes.id = awards.prize
                left join writers on writers.id = awards.writer
                """ + "where " + whereClause
        );

        for (int i = 0; i < prepStmtParams.size(); i++) {
            stmt.setObject(i + 1, prepStmtParams.get(i));
        }

        return Utils.readEntities(stmt.executeQuery(), AwardsRepository::readOne);
    }

    @SneakyThrows
    private static Award readOne(ResultSet rs) {
        return new Award(
                rs.getLong("writer"),
                rs.getLong("prize"),
                rs.getDate("date"),
                rs.getBigDecimal("prize_amount_dollars")
        );
    }

}
