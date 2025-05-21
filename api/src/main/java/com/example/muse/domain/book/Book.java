package com.example.muse.domain.book;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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

    @PrePersist
    @PreUpdate
    private void tTitleNormalized() {

        titleNormalized = title.toLowerCase().replace(" ", "");
    }
}
