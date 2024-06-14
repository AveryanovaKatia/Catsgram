package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class Post {
    Long id;
    long authorId;
    String description;
    Instant postDate;
}
