package ohonovskiy.ua.buycrypto.service.util.news;

import ohonovskiy.ua.buycrypto.model.util.news.NewsSubscriber;
import ohonovskiy.ua.buycrypto.repository.util.NewsRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    private final NewsRepo newsRepo;

    public NewsService(NewsRepo newsRepo) {
        this.newsRepo = newsRepo;
    }

    public void save(NewsSubscriber newsSubscriber) {
        newsRepo.save(newsSubscriber);
    }

    public void delete(NewsSubscriber newsSubscriber) {
        newsRepo.delete(newsSubscriber);
    }

    public void deleteByEmail(String email) {
        newsRepo.deleteByEmail(email);
    }

    public List<NewsSubscriber> getAll() {
        return newsRepo.findAll();
    }

    public void subscribe(String email) {
        if (newsRepo.findNewsSubscriberByEmail(email).isEmpty()) {
            save(new NewsSubscriber(email));
        }
    }
}
