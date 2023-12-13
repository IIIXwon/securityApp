package be.shwan.springsecurityjwt.book.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BookController {
    @GetMapping("/book")
    public String book() {
        return "book";
    }
}
