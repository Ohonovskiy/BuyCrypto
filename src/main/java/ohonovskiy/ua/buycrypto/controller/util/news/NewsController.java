package ohonovskiy.ua.buycrypto.controller.util.news;

import ohonovskiy.ua.buycrypto.service.util.news.NewsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @PostMapping("/subscribe")
    public String postSubscribe(@RequestParam String email) {
        newsService.subscribe(email);
        return "redirect:/";
    }

    @PostMapping("/cancel")
    public String cancelSubscription(@RequestParam String email) {
        newsService.deleteByEmail(email);
        return "redirect:/";
    }
}
