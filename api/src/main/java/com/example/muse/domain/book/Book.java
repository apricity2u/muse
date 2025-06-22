package com.example.muse.domain.book;

import com.example.muse.domain.like.Likes;
import com.example.muse.domain.review.Review;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String titleNormalized;
    private String author;
    private String publisher;
    private LocalDate publishedDate;
    private String isbn;
    private String imageUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Likes> likes = new ArrayList<>();

    @PrePersist
    @PreUpdate
    private void titleNormalized() {

        titleNormalized = title.toLowerCase().replace(" ", "");
    }
}
