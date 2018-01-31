package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.service.QnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    QnaService qnaService;

    @GetMapping("/")
    public String home(Model model) {
        List<Question> questions = (List<Question>) qnaService.findAll();
        model.addAttribute("questions", questions);
        return "home";
    }
}
