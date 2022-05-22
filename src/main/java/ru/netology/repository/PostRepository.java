package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
@Repository
public class PostRepository {

    private final Map<Long, Post> data = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong();

    public List<Post> all() {
        return new ArrayList<>(data.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(data.get(id));
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
        data.remove(id);
    }
}
