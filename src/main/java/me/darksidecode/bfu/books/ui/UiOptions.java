package me.darksidecode.bfu.books.ui;

import com.github.lgooddatepicker.components.DatePickerSettings;
import lombok.experimental.UtilityClass;
import me.darksidecode.bfu.books.App;

import java.awt.*;
import java.util.Locale;

@UtilityClass
public class UiOptions {

    public static final Font genericFont = new Font("Segoe UI", Font.PLAIN, 18);
    
    private static final DatePickerSettings DATE_PICKER_SETTINGS;
    static {
        DATE_PICKER_SETTINGS = new DatePickerSettings();
        DATE_PICKER_SETTINGS.setLocale(Locale.ENGLISH);
        DATE_PICKER_SETTINGS.setFontValidDate(genericFont);
        DATE_PICKER_SETTINGS.setFontCalendarDateLabels(genericFont);
        DATE_PICKER_SETTINGS.setFontCalendarWeekdayLabels(genericFont);
        DATE_PICKER_SETTINGS.setFontClearLabel(genericFont);
        DATE_PICKER_SETTINGS.setFontMonthAndYearMenuLabels(genericFont);
        DATE_PICKER_SETTINGS.setFontVetoedDate(genericFont);
        DATE_PICKER_SETTINGS.setFontInvalidDate(genericFont);
        DATE_PICKER_SETTINGS.setFontMonthAndYearNavigationButtons(genericFont);
        DATE_PICKER_SETTINGS.setFontTodayLabel(genericFont);
        DATE_PICKER_SETTINGS.setFontCalendarWeekNumberLabels(genericFont);
        DATE_PICKER_SETTINGS.setFormatForDatesCommonEra(App.DATE_FORMAT_PATTERN);
    }
    public static DatePickerSettings datePickerSettings(boolean allowEmptyDates) {
        var settings = DATE_PICKER_SETTINGS.copySettings();
        settings.setAllowEmptyDates(allowEmptyDates);
        return settings;
    }

}
