package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.dto.QuestionsDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/qna")
public class ApiQuestionController {
    private static final Logger log = LoggerFactory.getLogger(ApiQuestionController.class);
    
    @Resource(name = "qnaService")
    private QnaService qnaService;


    @PostMapping("")
    public ResponseEntity<Void> create(@Valid @RequestBody QuestionDto question, @LoginUser User user) throws Exception {
        Question saveQuestion = qnaService.add(user ,question);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/qna/" + saveQuestion.getId()));
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public QuestionDto show(@PathVariable long id) {
        Question question = qnaService.findById(id);
        return question.toQuestionDto();
    }

    @GetMapping("")
    public QuestionsDto list() {
        Iterable<Question> questions = qnaService.findAll();
        List<QuestionDto> questionDtoList = new ArrayList<>();
        questions.forEach(i -> questionDtoList.add(i.toQuestionDto()));
        return new QuestionsDto(questionDtoList);
    }

    @DeleteMapping("{questionId}/delete")
    public ResponseEntity<Void> delete(@PathVariable long questionId, @LoginUser User user) throws Exception {
        qnaService.deleteQuestion(user ,questionId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/qna/"));
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @PutMapping("{questionId}/update")
    public ResponseEntity<Void> update(@PathVariable long questionId, @LoginUser User user, @Valid @RequestBody QuestionDto questionDto) throws Exception {
        qnaService.update(user, questionId, questionDto.toQuestion());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/qna/"));
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
}
