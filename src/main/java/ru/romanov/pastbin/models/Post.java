package ru.romanov.pastbin.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "Post")
@RequiredArgsConstructor
public class Post {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
//    @Column(name = "url")
//    private String url;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Transient
    private String text;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;
}
