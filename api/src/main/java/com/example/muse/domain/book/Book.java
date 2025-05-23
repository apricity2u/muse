package com.example.muse.domain.book;

import com.example.muse.domain.like.Likes;
import com.example.muse.domain.review.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
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
    private Date publishedDate;
    ;
    private String isbn;
    private String description;
    private String imageUrl;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Likes> likes = new ArrayList<>();

    @PrePersist
    @PreUpdate
    private void tTitleNormalized() {

        titleNormalized = title.toLowerCase().replace(" ", "");
    }
}
