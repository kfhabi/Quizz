package quizz.bt.quizz.Controller; // Phải dùng package này mới chạy được

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller // Bắt buộc dùng @Controller (không dùng @RestController)
public class WebController {

    // 1. Vào trang chủ (localhost:8080) -> Mở trang banlaai.html
    @GetMapping("/")
    public String home() {
        return "banlaai"; 
    }

    // 2. Tự động bắt tất cả các đường dẫn khác
    // Ví dụ: gõ /dangnhap -> mở file dangnhap.html
    // Ví dụ: gõ /ketqualbtv -> mở file ketqualbtv.html
    @GetMapping("/{page}")
    public String showPage(@PathVariable String page) {
        return page; 
    }
}