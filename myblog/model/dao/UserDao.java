package myblog.ex.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import myblog.ex.model.entity.UserEntity;
@Repository
public interface UserDao extends JpaRepository<UserEntity, Long> {
	
	//用途：管理者の登録処理をする時に、同じメールアドレスがあったらば登録させないようにする
	//1行だけしかレコードは取得できない
	UserEntity findByUserEmail(String userEmail);
	
	//保存処理と更新処理
	UserEntity save(UserEntity userEntity);
	
	//用途：ログイン処理に使用。入力したメールアドレスとパスワードが一致している時だけデータを取得
	UserEntity findByUserEmailAndPassword(String userEmail,String password);
	
	//ユーザー情報一覧を取得
	List<UserEntity> findAll();
	

}
