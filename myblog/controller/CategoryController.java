package myblog.ex.controller;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import myblog.ex.model.entity.CategoryEntity;
import myblog.ex.model.entity.UserEntity;
import myblog.ex.service.CategoryService;
import myblog.ex.service.UserService;

@Controller
@Transactional
public class CategoryController {

	@Autowired
	private HttpSession session;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserService userService;

	// カテゴリー一覧表示
	@GetMapping("/category/all")
	public String getCategoryAll(Model model) {
		UserEntity users = (UserEntity) session.getAttribute("loginUsersInfo");
		// もし、users==null ホームページにリダイレクトする
		// そうでない場合
		// ログインしている人の名前の情報を画面に渡してカテゴリー一覧のhtmlを表示
		if (users == null) {
			return "redirect:/blog/all";
		} else {
			List<CategoryEntity> categorylist = categoryService.findAll();
			model.addAttribute("categorylist", categorylist);
			model.addAttribute("userName", users.getUserName());
			return "category_all.html";
		}
	}

	// カテゴリー登録画面の表示
	@GetMapping("/category/register")
	public String getCategoryRegister(Model model) {
		UserEntity users = (UserEntity) session.getAttribute("loginUsersInfo");
		// もし、users==null ホームページにリダイレクトする
		// そうでない場合
		// ログインしている人の名前の情報を画面に渡してカテゴリー一覧のhtmlを表示
		if (users == null) {
			return "redirect:/blog/all";
		} else {
			model.addAttribute("userName", users.getUserName());
			return "category_register.html";
		}
	}

	// カテゴリー登録内容を保存
	@PostMapping("/category/register")
	public String register(@RequestParam String categoryName, @RequestParam MultipartFile categoryImage) {
		UserEntity users = (UserEntity) session.getAttribute("loginUsersInfo");
		// もし、user==null ホームページにリダイレクトする
		// そうでない場合 画像のファイル名を取得
		// 画像のアップロード
		// もし 同じファイルの名前がなかったら保存
		// カテゴリーの一覧画面にリダイレクトする
		// そうでない場合 カテゴリー登録画面に渡す

		if (users == null) {
			return "redirect:/blog/all";
		} else {
			// ファイルの名前を取得
			String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date())
					+ categoryImage.getOriginalFilename();
			// ファイルの保存作業
			try {
				Files.copy(categoryImage.getInputStream(), Path.of("src/main/resources/static/blog-image/" + fileName));
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (categoryService.createCategory(categoryName, fileName)) {
				return "redirect:/category/all";
			} else {
				return "category_register.html";
			}
		}
	}

	// カテゴリー編集画面の表示
	@GetMapping("/category/edit/{categoryId}")
	public String getCategoryEditPage(@PathVariable Long categoryId, Model model) {
		UserEntity users = (UserEntity) session.getAttribute("loginUsersInfo");
		// もし、users==null ホームページにリダイレクトする
		// そうでない場合
		// ログインしている人の名前の情報を画面に渡してカテゴリー編集画面のhtmlを表示

		if (users == null) {
			return "redirect:/blog/all";
		} else {
			CategoryEntity categoryEntity = categoryService.selectCategoryId(categoryId);
			model.addAttribute("category", categoryEntity);
			model.addAttribute("userName", users.getUserName());
			return "category_edit.html";
		}
	}

	// カテゴリー編集内容の保存
	@PostMapping("/category/update")
	public String categoryUpdate(@RequestParam String categoryName,
			@RequestParam("categoryImage") MultipartFile categoryImage, @RequestParam Long categoryId) {
		UserEntity users = (UserEntity) session.getAttribute("loginUsersInfo");
		// もし、users==null ホームページにリダイレクトする
		// そうでない場合、
		// 画像を取得して、アップロードする
		// 情報を更新して、カテゴリ一覧画面にリダイレクする
		if (users == null) {
			return "redirect:/blog/all";
		} else {
			String fileName = null;
			try {
				// もし、新しい画像をアップロードしたい場合
				// 画像名を取得して、新しい画像に名前を付ける
				// 新しい画像を保存
				// もし、新しい画像がない場合、
				// DBから現在の画像を取得
				if (!categoryImage.isEmpty()) {
					String originalFilename = categoryImage.getOriginalFilename();
					fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date()) + originalFilename;
					Files.copy(categoryImage.getInputStream(),
							Path.of("src/main/resources/static/blog-image/" + fileName),
							StandardCopyOption.REPLACE_EXISTING);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			// DBに情報を更新
			if (categoryService.updateCategory(categoryId, categoryName, fileName)) {
				return "redirect:/category/all";
			} else {
				return "category_edit.html";
			}
		}
	}

	// カテゴリー登録内容の削除
	@PostMapping("/category/delete")
	public String deleteCategory(Long categoryId) {
		UserEntity users = (UserEntity) session.getAttribute("loginUsersInfo");
		if (users == null) {
			return "redirect:/blog/all";
		} else {

			if (categoryService.deleteByCategory(categoryId)) {
				return "redirect:/category/all";
			} else {
				return "category_edit.html";
			}
		}
	}

}
