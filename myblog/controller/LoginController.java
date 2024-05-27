package myblog.ex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import myblog.ex.model.entity.UserEntity;
import myblog.ex.service.UserService;

@Controller
public class LoginController {
	@Autowired
	private UserService userService;

	// Sessionが使えるように宣言
	@Autowired
	private HttpSession session;

	// ログイン画面の表示
	@GetMapping("/admin/login")
	public String getLoginPage() {
		return "login.html";
	}

	// ログイン処理
	@PostMapping("/admin/login/process")
	public String LoginProcess(@RequestParam String userEmail, @RequestParam String password, Model model) {
		// loginCheckメソッドを呼び出してその結果をuserという変数に格納
		UserEntity user = userService.loginCheck(userEmail, password);
		// もし、user＝＝Nullログイン画面にとどまります
		// そうでない場合は、sessionにログイン情報に保存
		// ブログ home画面にリダイレクトにする/blog/all

		if (user == null) {
			// ユーザーが見つからない場合、警告メッセージを追加してログインページに戻る
			model.addAttribute("errorMessage", "IDまたはパスワードが正しくありません。");
			return "login.html";
		} else {
			session.setAttribute("loginUsersInfo", user);
			return "redirect:/blog/all";
		}

	}
}
