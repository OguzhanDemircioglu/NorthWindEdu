package com.server.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @SequenceGenerator(name = "category_seq", sequenceName = "category_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @Column(name = "category_id", nullable = false)
    private Short categoryId;

    @Column(name = "category_name", length = 15, nullable = false)
    private String categoryName;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "picture", columnDefinition = "bytea")
    private byte[] picture;

}
