package myblog.ex.model.entity;


import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="category")
public class CategoryEntity {
	@Id
    @Column(name="category_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long categoryId;
    
    @NonNull
    @Column(name="category_name")
    private String categoryName;
    
    @NonNull
    @Column(name="category_image")
    private String categoryImage;


	public CategoryEntity(@NonNull String categoryName, @NonNull String categoryImage) {
		this.categoryName = categoryName;
		this.categoryImage = categoryImage;
	}
    
    
}
