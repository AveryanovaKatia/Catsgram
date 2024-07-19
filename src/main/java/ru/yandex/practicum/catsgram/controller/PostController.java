package ru.yandex.practicum.catsgram.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.SortOrder;
import ru.yandex.practicum.catsgram.service.PostService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Optional<List<Post>> findAll(@RequestParam(defaultValue = "10") @Positive int size,
                                        @RequestParam(defaultValue = "desc") String sort,
                                        @RequestParam(defaultValue = "0") @Min(value = 0) int from) {
        return postService.findAll(size, SortOrder.from(sort), from);
    }

    @GetMapping("/post/{postId}")
    public Optional<Post> findById(@PathVariable("postId") long id) {
        return postService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}