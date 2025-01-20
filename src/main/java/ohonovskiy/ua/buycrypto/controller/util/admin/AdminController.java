package ohonovskiy.ua.buycrypto.controller.util.admin;

import ohonovskiy.ua.buycrypto.DTO.util.news.PostNewsRequest;
import ohonovskiy.ua.buycrypto.service.util.news.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final NewsService newsService;

    @Autowired
    public AdminController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/news")
    public String getNewsPage() {
        return "admin/news";
    }

    @PostMapping("/news")
    public String postNews(@RequestBody PostNewsRequest postNewsRequest) {
        newsService.postNews(postNewsRequest);

        return "redirect:/admin/news";
    }
}
