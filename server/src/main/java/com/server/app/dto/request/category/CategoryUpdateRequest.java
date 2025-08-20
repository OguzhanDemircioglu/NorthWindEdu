package com.server.app.dto.request.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateRequest {
    private Long categoryId;
    private String categoryName;
    private String description;
    private String picture;
}
