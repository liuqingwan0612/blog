package myblog.ex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import myblog.ex.service.UserService;

@Controller
public class RegisterController {
	@Autowired
	private UserService usersService;

	// 登録画面の表示
	@GetMapping("/admin/register")
	public String getLoginPage() {
		return "register.html";
	}

	// 登録処理
	@PostMapping("/admin/register/process")
	public String registerProcess(@RequestParam String userName,
			                      @RequestParam String userEmail,
			                      @RequestParam String password,
			                      Model model) {
	    // もし、createUsersがtrue login.htmlに遷移
	    // そうでない場合、register.htmlにとどまります。
		if (usersService.createUsers(userName, userEmail, password)) {
			return "login.html";
		} else {
			//既に登録した場合、警告メッセージを追加します
			model.addAttribute("errorMessage", "ユーザー登録に失敗しました。");
			return "register.html";
		}

	}

}
