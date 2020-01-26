package nl.rcomanne.passwordmanager.web;

import nl.rcomanne.passwordmanager.service.UserService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest
@AutoConfigureMockMvc
class UserControllerIT {

    @MockBean
    private UserService userService;

    @Autowired
    UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenRegisterValidRequestThenCorrectResponse() throws Exception {
        String body = "{\"username\": \"test-user\", \"password\": \"test-password\", \"mail\": \"valid@mail.com\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isCreated());
        // TODO: validate response body maybe?
    }

    @Test
    public void whenRegisterInvalidRequestThenBadRequestRespone() throws Exception {
        String body = "{\"username\": \"test-user\", \"mail\": \"valid@mail.com\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", Is.is("Password is required")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenLoginValidRequestThenCorrectResponse() throws Exception {
        String body = "{\"mail\": \"valid@mail.com\", \"password\": \"test-password\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        // TODO: validate response body maybe?
    }

    @Test
    public void whenLoginInvalidRequestThenBadRequestRespone() throws Exception {
        String body = "{\"mail\": \"valid@mail.com\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", Is.is("Password is required")))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

}