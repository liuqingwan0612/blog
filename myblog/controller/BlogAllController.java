package myblog.ex.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import myblog.ex.model.dao.BlogDao;
import myblog.ex.model.entity.BlogEntity;
import myblog.ex.model.entity.CategoryEntity;
import myblog.ex.model.entity.UserEntity;
import myblog.ex.service.BlogService;
import myblog.ex.service.CategoryService;
import myblog.ex.service.UserService;

@Controller
public class BlogAllController {

	// ユーザーのセッション情報にアクセスする
	@Autowired
	private HttpSession session;

	@Autowired
	private UserService userService;

	@Autowired
	private BlogService blogService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private BlogDao blogDao;

	// ブログホームを表示
	@GetMapping("/blog/all")
	public String getBlogAll(Model model) {
		// セッション空ログインしている人の情報を取得
		UserEntity users = (UserEntity) session.getAttribute("loginUsersInfo");

		// もし、users==null ログイン画面にリダイレクトする
		// そうでない場合
		// ログインしている人の名前の情報を画面に渡してブログホームのhtmlを表示
		if (users == null) {
			return "redirect:/admin/login";
		} else {
			// ブログとカテゴリテーブルからすべてのブログ記事を取得
			List<BlogEntity> blogList = blogService.selectByUserId(users.getUserId());
			model.addAttribute("blogList", blogList);
			List<CategoryEntity> categoryList = categoryService.findAll();
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("userName", users.getUserName());

			return "blog_all.html";
		}
	}

	// 記事一覧画面の表示
	@GetMapping("/blog/list")
	public String getBlogList(Model model) {
		UserEntity users = (UserEntity) session.getAttribute("loginUsersInfo");

		// もし、users==null ログイン画面にリダイレクトする
		// そうでない場合
		// ログインしている人の名前の情報を画面に渡して記事一覧のhtmlを表示
		if (users == null) {
			return "redirect:/admin/login";
		} else {
			List<BlogEntity> blogList = blogService.selectByUserId(users.getUserId());
			model.addAttribute("blogList", blogList);
			model.addAttribute("userName", users.getUserName());
			return "blog_list.html";
		}
	}

	// 記事登録画面の表示
	@GetMapping("/blog/register")
	public String getBlogRegisterPage(Model model) {
		// ログインしている人の情報を取得
		UserEntity users = (UserEntity) session.getAttribute("loginUsersInfo");

		// すべての記事情報を取得
		List<BlogEntity> blogList = blogService.findByAll();

		// 現在の時刻を取得
		LocalDateTime updateTime = LocalDateTime.now();

		// 現在の時刻をフォーマット化してモデルに追加
		String formattedUpdateTime = blogService.formatUpdateTime(updateTime);
		model.addAttribute("formattedUpdateTime", formattedUpdateTime);

		// もし、Userが存在しない場合
		// ホームページにリダイレクトする
		// そうでない場合、情報をブログ登録画面に渡す
		if (users == null) {
			return "redirect:/blog/all";
		} else {
			model.addAttribute("userId", users.getUserId());
			model.addAttribute("blogList", blogList);
			model.addAttribute("category", new CategoryEntity());
			model.addAttribute("userName", users.getUserName());
			return "blog_register.html";
		}
	}

	// 記事を登録した内容の保存
	@PostMapping("/blog/register")
	public String register(@RequestParam String blogTitle, @RequestParam("blogImage") MultipartFile blogImage,
			@RequestParam String categoryName, @RequestParam String article,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime updateTime,
			@RequestParam Long userId) {
		// セッションからログインしている人の情報をusersという変数に格納
		UserEntity users = (UserEntity) session.getAttribute("loginUsersInfo");

		// もし、ユーザが存在しない場合
		// ホームページにリダイレクトする
		// そうでない場合、現在の時刻と画像を取得して
		// アップロードする
		// もし 同じファイルの名前がなかったら保存
		// 記事一覧画面にリダイレクトする
		// そうでない場合、記事登録画面に渡す

		if (users == null) {
			return "redirect:/blog/all";
		}

		// 更新時間を保存
		blogService.saveTime(updateTime);

		// ファイルの名前を取得
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date())
				+ blogImage.getOriginalFilename();

		// ファイルの保存作業
		try {
			Files.copy(blogImage.getInputStream(), Path.of("src/main/resources/static/blog-image/" + fileName));
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}

		// blogService のcreateBlogを使って更新された内容を保存
		if (blogService.createBlog(blogTitle, fileName, categoryName, article, updateTime, userId)) {
			return "redirect:/blog/list";
		} else {
			return "blog_register.html";
		}
	}

	// ブログ記事の編集画面を表示
	@GetMapping("/edit/{blogId}")
	public String getBlogEditPage(@PathVariable Long blogId, Model model) {
		// セッションからログインしている人の情報をusersという変数に格納
		UserEntity users = (UserEntity) session.getAttribute("loginUsersInfo");
		// ブログIDを元に、データベースからBlogEntityを取得する
		BlogEntity blogs = blogService.findByBlogId(blogId);
		// すべてのブログ情報を取得
		List<BlogEntity> blogList = blogService.findByAll();
		// すべてのカテゴリを取得
		List<CategoryEntity> categoryList = categoryService.findAll();
		// 現在の時刻を取得してフォーマット化
		LocalDateTime updateTime = LocalDateTime.now();
		String formattedUpdateTime = blogService.formatUpdateTime(updateTime);

		// もし、users == null || blogs == null 記事一覧画面にリダイレクトする
		// そうでない場合
		// ログインしている人の名前の情報を画面に渡してブログ編集のhtmlを表示
		if (users == null || blogs == null) {
			return "redirect:/blog/list";
		} else {
			// モデルにユーザーID、ブログエンティティ、カテゴリーリスト、ユーザー名を追加する
			model.addAttribute("userId", users.getUserId());
			model.addAttribute("blogs", blogs);
			model.addAttribute("blogList", blogList);
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("formattedUpdateTime", formattedUpdateTime);
			model.addAttribute("userName", users.getUserName());

			return "edit.html";
		}
	}

	// 登録内容を修正（更新）して保存
	@PostMapping("/blog/update")
	public String updateData(@RequestParam Long blogId, @RequestParam String blogTitle,
			@RequestParam("blogImage") MultipartFile blogImage, @RequestParam String categoryName,
			@RequestParam String article,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updateTime,
			@RequestParam Long userId) {

		// セッションからログインしている人の情報をusersという変数に格納
		UserEntity users = (UserEntity) session.getAttribute("loginUsersInfo");

		// もし、users==null 記事編集画面にリダイレクトする
		// そうでない場合
		// 画像名を取得し、アップロードする
		// テーブルに更新後、ブログ一覧にリダイレクトする
		if (users == null) {
			return "redirect:/edit";
		} else {
			String fileName = null;
			try {
				// もし、新しい画像をアップロードしたい場合
				// 画像名を取得して、新しい画像に名前を付ける
				// 新しい画像を保存
				// もし、新しい画像がない場合、
				// DBから現在の画像を取得
				if (!blogImage.isEmpty()) {
					// ファイル名を取得
					String originalFilename = blogImage.getOriginalFilename();
					// 以前の画像名を使って新しい画像を保存
					fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-").format(new Date()) + originalFilename;
					// 新しい画像を保存
					Files.copy(blogImage.getInputStream(), Path.of("src/main/resources/static/blog-image/" + fileName),
							StandardCopyOption.REPLACE_EXISTING);
				} else {
					// DBから現在の画像を取得
					BlogEntity existingBlog = blogService.findByBlogId(blogId);
					if (existingBlog != null) {
						fileName = existingBlog.getBlogImage();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				return "error-page";
			}
			// DBの情報を更新する
			blogService.update(blogId, blogTitle, fileName, categoryName, article, updateTime, userId);
			// カテゴリ情報を更新する
			blogService.updateCategory(blogId, categoryName);
			// 記事一覧画面にリダイレクトする
			return "redirect:/blog/list.html";
		}
	}

	// ブログ内容の削除
	@PostMapping("/blog/delete")
	public String deleteBlog(Long blogId) {
		// Serviceクラスに値を渡す、削除処理を行う
		blogService.deleteBlog(blogId);
		return "redirect:/blog/list";
	}

	// 検索フォーム
	@PostMapping("/blog/search")
	public String searchBlog(@RequestParam String keyword, Model model) {
		// BlogServiceの検索メソッドを呼び出して、一致するブログのリストを取得する
		List<BlogEntity> searchResult = blogService.searchByKeyword(keyword);

		// 検索結果をモデルに追加して、ビューで表示するために使用する
		model.addAttribute("searchResult", searchResult);

		// 検索結果のビューページを返す
		return "search_result.html";
	}

}
