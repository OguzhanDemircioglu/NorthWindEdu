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
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "category_name", length = 15, nullable = false)
    private String categoryName;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "picture", columnDefinition = "TEXT")
    private String picture;

}
