package ru.yandex.practicum.catsgram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.model.SortOrder;

import java.time.Instant;
import java.util.*;

@Service
public class PostService {
    private final Map<Long, Post> posts = new HashMap<>();
    UserService userService;

    @Autowired
    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Optional<List<Post>> findAll(int size, SortOrder sort, int from) {
        List<Post> postSet = new ArrayList<>(posts.values());
        if (from >= postSet.size()) {
            return Optional.empty();
        }
        postSet.sort(Comparator.comparing(Post::getPostDate));
        if (sort.equals(SortOrder.DESCENDING)) {
            postSet.sort(Comparator.comparing(Post::getPostDate).reversed());
        }
        int toElement = from + size;
        if ((postSet.size() - from) < size) {
            toElement = postSet.size() - 1;
        }
        return Optional.of(postSet.subList(from, toElement));
    }

    public Optional<Post> findById(long id) {
        if (posts.containsKey(id)) {
            return Optional.of(posts.get(id));
        }
        return Optional.empty();
    }


    public Post create(Post post) {
        if (userService.findUserById(post.getAuthorId()).isEmpty()) {
            throw new ConditionsNotMetException("Автор с id" + post.getAuthorId() + "не найден");
        }

        if (Objects.isNull(post.getDescription()) || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }

        post.setId(getNextId());
        post.setPostDate(Instant.now());
        post.setAuthorId(post.getAuthorId());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (Objects.isNull(newPost.getId())) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (Objects.isNull(newPost.getDescription()) || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
