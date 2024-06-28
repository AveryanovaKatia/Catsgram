package ru.yandex.practicum.catsgram.model;

public enum SortOrder {
    ASCENDING, DESCENDING;

    // Преобразует строку в элемент перечисления
    public static SortOrder from(String order) {
        switch (order.toLowerCase()) {
            case "ascending":
            case "asc":
                return ASCENDING; // по возрастанию
            case "descending":
            case "desc":
                return DESCENDING; //по убыванию
            default: return null;
        }
    }
}
