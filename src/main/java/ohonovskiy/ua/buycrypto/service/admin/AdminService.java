package ohonovskiy.ua.buycrypto.service.admin;

import ohonovskiy.ua.buycrypto.DTO.util.news.PostNewsRequest;
import ohonovskiy.ua.buycrypto.model.util.news.NewsSubscriber;
import ohonovskiy.ua.buycrypto.service.util.news.NewsService;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final NewsService newsService;

    public AdminService(NewsService newsService) {
        this.newsService = newsService;
    }

    public void postNews(PostNewsRequest postNewsRequest) {
        newsService.postNews(postNewsRequest);
    }
}
