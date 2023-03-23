package me.darksidecode.bfu.books;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
public enum OrderingDirection {
    Ascending (entities -> {}),
    Descending (Collections::reverse);

    public final @NonNull Consumer<List<?>> sort;
}
