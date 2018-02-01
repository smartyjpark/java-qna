package codesquad.web;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import static org.junit.Assert.assertThat;

import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import codesquad.domain.UserRepository;
import codesquad.dto.QuestionDto;
import codesquad.dto.QuestionsDto;
import codesquad.dto.UserDto;
import codesquad.security.LoginUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import support.test.AcceptanceTest;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
    public static final User SANJIGI = new User(2L, "sanjigi", "test", "name", "sanjigi@slipp.net");

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void create() throws Exception {
        QuestionDto newQuestion = createQuestionDto();
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/qna", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        Question dbQuestion = questionRepository.findOne(3L);
        assertThat(dbQuestion.getTitle(), is(newQuestion.getTitle()));
        assertThat(dbQuestion.getContents(), is(newQuestion.getContents()));
    }

    private QuestionDto createQuestionDto() {
        return new QuestionDto("제목입니다gg", "글입니다gg");
    }

    private QuestionDto createQuestionDto(long id) {
        return new QuestionDto(id, "제목입니다gg", "글입니다gg");
    }

    @Test
    public void show() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate().getForEntity("/api/qna/1", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        Question dbQuestion = questionRepository.findOne(1L);
        assertThat(response.getBody().contains(dbQuestion.getTitle()), is(true));
        assertThat(response.getBody().contains(dbQuestion.getContents()), is(true));
    }

    @Test
    public void delete_fail_noLogin() throws Exception {
        template().delete("/api/qna/1/delete", String.class);
        Question dbQuestion = questionRepository.findOne(1L);
        assertThat(dbQuestion.isDeleted(), is(false));
    }

    @Test
    public void delete_fail_anotherUser() throws Exception {
        basicAuthTemplate().delete("/api/qna/2/delete", String.class);
        Question dbQuestion = questionRepository.findOne(2L);
        assertThat(dbQuestion.isDeleted(), is(false));
    }

    @Test
    public void delete() throws Exception {
        basicAuthTemplate().delete("/api/qna/1/delete", String.class);
        Question dbQuestion = questionRepository.findOne(1L);
        assertThat(dbQuestion.isDeleted(), is(true));
    }

    @Test
    public void update_fail_noLogin() throws Exception {
        QuestionDto newQuestion = createQuestionDto(1L);
        template().put("/api/qna/1/update", newQuestion);
        Question dbQuestion = questionRepository.findOne(1L);
        assertThat(dbQuestion.getTitle(), not(newQuestion.getTitle()));
        assertThat(dbQuestion.getContents(), not(newQuestion.getContents()));
    }

    @Test
    public void update_fail_anotherUser() throws Exception {
        QuestionDto newQuestion = createQuestionDto(2L);
        basicAuthTemplate().put("/api/qna/1/update", newQuestion);
        Question dbQuestion = questionRepository.findOne(2L);
        assertThat(dbQuestion.getTitle(), not(newQuestion.getTitle()));
        assertThat(dbQuestion.getContents(), not(newQuestion.getContents()));
    }

    @Test
    public void update() throws Exception {
        QuestionDto newQuestion = createQuestionDto(1L);
        basicAuthTemplate().put("/api/qna/1/update", newQuestion);
        Question dbQuestion = questionRepository.findOne(1L);
        assertThat(dbQuestion.getTitle(), is(newQuestion.getTitle()));
        assertThat(dbQuestion.getContents(), is(newQuestion.getContents()));
    }
}
