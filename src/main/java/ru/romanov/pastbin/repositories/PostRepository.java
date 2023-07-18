package ru.romanov.pastbin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.pastbin.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
