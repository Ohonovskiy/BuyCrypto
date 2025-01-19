package ohonovskiy.ua.buycrypto.controller.util.admin;

import ohonovskiy.ua.buycrypto.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/news")
    public String getNewsPage() {
        return "admin/news";
    }

    @PostMapping("/news")
    public String postNews(@RequestBody PostNewsRequest postNewsRequest) {
        adminService.handlePostNews(postNewsRequest);
    }
}
