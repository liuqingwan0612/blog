package myblog.ex.model.entity;



import java.time.LocalDateTime;

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
@Table(name="blog")
public class BlogEntity {
	
    //エンティティの主キー（ID）
    @Id
    // データベースの列名を指定するアノテーション
    @Column(name="blog_id")
    // 主キー値を自動生成する方法を指定するアノテーション
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long blogId;
    
    @NonNull
    @Column(name="blog_title")
    private String blogTitle;
    
    @NonNull
    @Column(name="blog_image")
    private String blogImage;
    
    @NonNull
    @Column(name="category_name")
    private String categoryName;
    
    @NonNull
    @Column(name="article")
    private String article;
    
    @NonNull
    @Column(name="update_time")
    private LocalDateTime   updateTime;

    

    @NonNull
    @Column(name="user_id")
    private Long userId;

    //コンストラクタ
	public BlogEntity( @NonNull String blogTitle, @NonNull String blogImage, @NonNull String categoryName,
			@NonNull String article, @NonNull LocalDateTime   updateTime, @NonNull Long userId) {
		this.blogTitle = blogTitle;
		this.blogImage = blogImage;
		this.categoryName = categoryName;
		this.article = article;
		this.updateTime = updateTime;
		this.userId = userId;
	}


    
}
