package myblog.ex.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import myblog.ex.model.entity.BlogEntity;
import myblog.ex.model.entity.CategoryEntity;
import myblog.ex.service.BlogService;
import myblog.ex.service.CategoryService;

@Controller

public class UserBlogController {

	@Autowired
	private BlogService blogService;

	@Autowired
	private CategoryService categoryService;

	// ブログ記事詳細画面の表示
	@GetMapping("/blog/{blogId}")
	public String getBlogPage(@PathVariable Long blogId, Model model) {
		// ブログ情報を取得
		BlogEntity blogs = blogService.findByBlogId(blogId);
		// すべてのカテゴリ情報を取得
		List<CategoryEntity> categoryList = categoryService.findAll();
		// ブログ更新時刻を取得
		LocalDateTime updateTime = blogs.getUpdateTime();
		// 更新時刻をフォーマット化
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String formattedDateTime = updateTime.format(formatter);

		// フォーマットされた時刻ををモデルに追加
		model.addAttribute("formattedUpdateTime", formattedDateTime);
		model.addAttribute("blogs", blogs);
		model.addAttribute("categoryList", categoryList);
		// ブログ閲覧画面に渡す
		return "blog_browse.html";

	}

	// ブログ記事をカテゴリ別に表示する
	@GetMapping("/category/list/{categoryName}")
	public String getBlogCategoryPage(@PathVariable String categoryName, Model model) {
		// カテゴリ名で検索してブログ記事情報を引っ張り出す
		List<BlogEntity> blogList = blogService.selectByCategoryName(categoryName);
		// カテゴリ情報をすべて取得
		List<CategoryEntity> categoryList = categoryService.findAll();
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("blogList", blogList);

		return "blog_category_list.html";
	}

}
