$(function() {
    $(".menu-toggle_btn").on('click', function() {
        $(this).toggleClass('active');
        $('.menu-inner').toggleClass('active');
        $('menu__item').toggleClass('active');
    });

    // 这里假设服务器端通过模型传递了格式化后的时间字符串到前端
    var formattedDateTime = "2024-05-27 09:34" /* 格式化后的时间字符串 */;
    $("#alt-t").text(formattedDateTime); // 假设时间显示在 id 为 alt-t 的元素中

    // 検索を実行する関数
    function search() {
        // 获取搜索关键词
        var keyword = $("#searchInput").val();

        // 如果搜索关键词不为空，则进行搜索
        if (keyword.trim() !== "") {
            // 构建搜索结果页面的 URL，将搜索关键词作为查询参数传递
            var searchUrl = "/blog/search?keyword=" + encodeURIComponent(keyword);

            // 重定向到搜索结果页面
            window.location.href = searchUrl;
        }
    }

    // 当用户按下回车键时触发搜索功能
    $("#searchInput").on("keypress", function(event) {
        if (event.key === "Enter") {
            search();
        }
    });
    

});
