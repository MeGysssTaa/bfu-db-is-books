package me.darksidecode.bfu.books;

import com.github.lgooddatepicker.components.DatePicker;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@UtilityClass
public class Utils {

    public static String nullIfBlank(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    public static Date extractSqlDate(@NonNull DatePicker dp) {
        var localDate = dp.getDate();
        return localDate == null ? null : Date.valueOf(localDate);
    }

    @SneakyThrows
    public static <T> List<T> readEntities(
            @NonNull ResultSet rs,
            @NonNull Function<? super ResultSet, ? extends T> mappingFunc
    ) {
        var entities = new ArrayList<T>();
        while (rs.next()) entities.add(mappingFunc.apply(rs));
        return entities;
    }

    public static Date parseSqlDateToken(String token) {
        try {
            java.util.Date javaDate = App.DATE_FORMAT.parse(token);
            return new Date(javaDate.toInstant().toEpochMilli());
        } catch (ParseException e) {
            return null;
        }
    }

    public static <T> Optional<Integer> firstIndexOf(
            @NonNull T[] arr,
            @NonNull Predicate<? super T> pred
    ) {
        for (int i = 0; i < arr.length; i++) {
            if (pred.test(arr[i])) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public static void error(@NonNull Component parent, @NonNull String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

}
