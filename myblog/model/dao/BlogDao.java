package myblog.ex.model.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import myblog.ex.model.entity.BlogEntity;

@Repository
@Transactional
public interface BlogDao extends JpaRepository<BlogEntity, Long> {

	//blogテーブルのuser_idとUsersテーブルのuserIdを使ってテーブルを結合させてデータを取得
	//ログインしている人の記事一覧取得
	List<BlogEntity> findByUserId(Long userId);
	
	//キーワードでブログを検索する
	 @Query("SELECT b FROM BlogEntity b WHERE b.blogTitle LIKE %:keyword% OR b.article LIKE %:keyword%")
	    List<BlogEntity> searchByKeyword(@Param("keyword") String keyword);
	 
	 
	//blogIdを使ってDBに検索する
	//ブログ記事の編集の際に使用
	BlogEntity findByBlogId(Long blogId);

	 
	//ブログの内容を保存
	BlogEntity save(BlogEntity blogEntity);
	
	//ブログタイトルを検索して情報を取得
	BlogEntity findByBlogTitle(String blogTitle);
	
	//blogテーブルの全ての情報を取得
	//ユーザー側のブログ記事一覧取得の際に使用
	List<BlogEntity> findAll();
	
    //カテゴリ名を使ってDBに検索する
	//ユーザー側のブログカテゴリー検索の際に使用
	List<BlogEntity> findByCategoryName(String categoryName);
	
	//blogIdを取得して該当ブログ情報を削除する
	List<BlogEntity> deleteByBlogId(Long blogId);

	
}
