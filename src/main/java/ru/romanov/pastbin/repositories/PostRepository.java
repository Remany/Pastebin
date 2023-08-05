package ru.romanov.pastbin.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.romanov.pastbin.models.Post;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByUrl(String url);
    @Modifying
    @Transactional
    @Query("DELETE FROM Post m WHERE m.expiresAt <= :currentDateTime")
    void deleteExpiredPost(@Param("currentDateTime") LocalDateTime currentDateTime);
}