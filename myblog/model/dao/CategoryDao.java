package myblog.ex.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import myblog.ex.model.entity.CategoryEntity;

public interface CategoryDao extends JpaRepository<CategoryEntity, Long> {

	//カテゴリー内容の保存
	CategoryEntity save(CategoryEntity categoryEntity);
	
	
	CategoryEntity findByCategoryName(String categoryName);
	//categoryIdを使ってDBで検索
	//category編集
	CategoryEntity findByCategoryId(Long categoryId);
	
	//削除
    void deleteByCategoryId(Long categoryId );
	
	//カテゴリー一覧を取得
	List<CategoryEntity> findAll();
	
	
	
	
	
	
	
	
}
