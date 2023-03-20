package me.darksidecode.bfu.books;

import com.github.lgooddatepicker.components.DatePicker;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

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
    public static <T> Collection<T> readEntities(
            @NonNull ResultSet rs,
            @NonNull Function<? super ResultSet, ? extends T> mappingFunc
    ) {
        var entities = new ArrayList<T>();
        while (rs.next()) entities.add(mappingFunc.apply(rs));
        return entities;
    }

}
