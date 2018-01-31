package codesquad.web;

import codesquad.CannotDeleteException;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import javassist.NotFoundException;
import netscape.security.ForbiddenTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/qna")
public class QuestionController {
    private static final Logger log = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionRepository questionRepository;

    @Resource(name = "qnaService")
    private QnaService qnaService;

    @PostMapping("")
    public String create(@LoginUser User user, Question question) {
        qnaService.create(user, question);
        return "redirect:/";
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable long id, @LoginUser User user) throws Exception {
        qnaService.deleteQuestion(user, id);
        return "redirect:/";
    }
}
