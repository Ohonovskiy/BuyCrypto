package ohonovskiy.ua.buycrypto.service.util.news;

import ohonovskiy.ua.buycrypto.DTO.util.email.EmailSendRequest;
import ohonovskiy.ua.buycrypto.DTO.util.news.PostNewsRequest;
import ohonovskiy.ua.buycrypto.enums.EmailSendType;
import ohonovskiy.ua.buycrypto.model.util.news.NewsSubscriber;
import ohonovskiy.ua.buycrypto.repository.util.NewsRepo;
import ohonovskiy.ua.buycrypto.service.util.email.EmailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    private final NewsRepo newsRepo;
    private final EmailService emailService;

    public NewsService(NewsRepo newsRepo, EmailService emailService) {
        this.newsRepo = newsRepo;
        this.emailService = emailService;
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

    public void postNews(PostNewsRequest postNewsRequest) {
        for (NewsSubscriber ns : getAll()) {
            emailService.handleRequest(
                    EmailSendRequest.builder()
                            .message(postNewsRequest.getMessage())
                            .email(ns.getEmail())
                            .emailSendType(EmailSendType.NEWS)
                            .build()
            );
        }
    }
}
