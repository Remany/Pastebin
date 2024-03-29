package ru.romanov.pastbin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.pastbin.models.Post;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByUrl(String url);
    List<Post> findByExpiresAtBefore(Date currentTime);
}