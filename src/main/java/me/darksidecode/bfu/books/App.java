package me.darksidecode.bfu.books;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import lombok.Getter;
import me.darksidecode.bfu.books.database.BooksDatabase;
import me.darksidecode.bfu.books.ui.MainForm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class App {

    static {
        System.out.println("Author: German Vekhorev, IKBFU, Applied Mathematics and Computer Science, 3rd year (2023)");
    }

    public static final App INSTANCE = new App();

    public static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy";
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);

    @Getter
    private final BooksDatabase database = new BooksDatabase();

    public static void main(String[] args) {
        FlatMacLightLaf.setup();
        new MainForm();
    }

}
