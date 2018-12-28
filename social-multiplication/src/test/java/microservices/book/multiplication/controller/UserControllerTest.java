package microservices.book.multiplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<User> json;

    @Before
    public void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void getUserByIdTest() throws Exception {
        // given
        User user = new User(1L, "john");
        given(userRepository.findOne(1L)).willReturn(user);
        String jsonExpected = json.write(user).getJson();

        // when & then
        mvc.perform(get("/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonExpected));
    }
}