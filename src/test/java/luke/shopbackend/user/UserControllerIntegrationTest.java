package luke.shopbackend.user;

import luke.shopbackend.ShopBackendApplication;
import luke.shopbackend.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@ContextConfiguration(classes = UserController.class)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void shouldCreateMockMvc() {
        assertThat(mockMvc, is(not(nullValue())));
    }

//    @Test
//    void getUsersNotAuthenticated() throws Exception {
//        Pageable pageable = PageRequest.of(0,2);
//
//        mockMvc.perform(get("/api/users?page=0&size=2"))
//                .andExpect(status().isUnauthorized());
//
//        then(userService).should(times(0)).getAllUsers(pageable);
//    }
//
//    @Test
//    @WithMockUser(authorities = "USER")
//    void getUsersUSER() throws Exception {
//        Pageable pageable = PageRequest.of(0,2);
//
//        mockMvc.perform(get("/api/users?page=0&size=2"))
//                .andExpect(status().isForbidden());
//
//        then(userService).should(times(0)).getAllUsers(pageable);
//    }
//
//    @Test
//    @WithMockUser(username = "admin", authorities = "ADMIN")
//    void getUsersADMIN() throws Exception {
//        Pageable pageable = PageRequest.of(0,2);
//        given(userService.getAllUsers(pageable)).willReturn(UserTestUtils.getUsers());
//
//        mockMvc.perform(get("/api/users?page=0&size=2"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content.size", is(2)))
//                .andExpect(jsonPath("$.content[0].username", is("Jacek")))
//                .andExpect(jsonPath("$.content[0].id", is(1)))
//                .andExpect(jsonPath("$.content[1].username", is("Inny username")))
//                .andExpect(jsonPath("$.content[1].id", is(2)));
//
//        then(userService).should().getAllUsers(pageable);
//    }
}





















