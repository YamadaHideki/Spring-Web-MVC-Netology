package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// Stub
@Repository
public class PostRepository {

    private final Map<Long, Post> data = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong();

    public List<Post> all() {
        return data.values().stream()
                .filter(post -> !post.isRemoved())
                .collect(Collectors.toList());
    }

    public Optional<Post> getById(long id) {
        Post post = data.get(id);

        if (post == null) {
            throw new NotFoundException("Post does not exist");
        }

        if (post.isRemoved()) {
            throw new NotFoundException("Post has been deleted");
        }

        return Optional.of(post);
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            post.setId(counter.addAndGet(1));
            data.putIfAbsent(post.getId(), post);
            return post;
        } else if (post.getId() > 0) {
            getById(post.getId()).ifPresent(
                    e -> e.setContent(post.getContent())
            );
            return data.get(post.getId()) != null && data.get(post.getId()).getContent().equals(post.getContent()) ? post : null;
        }
        return null;
    }

    public void removeById(long id) {
        Post post = data.get(id);

        if (post == null) {
            throw new NotFoundException("Post does not exist");
        }

        if(!post.isRemoved()) {
            post.setRemoved(true);
        }
    }
}
