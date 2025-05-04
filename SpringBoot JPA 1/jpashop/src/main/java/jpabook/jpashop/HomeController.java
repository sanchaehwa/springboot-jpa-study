package jpabook.jpashop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@Slf4j
public class HomeController {
    @GetMapping("/") //localhost:8080 접속하면 보이는 첫화면
    public String home() {
        log.info("home controller");
        return "home";
    }
}
