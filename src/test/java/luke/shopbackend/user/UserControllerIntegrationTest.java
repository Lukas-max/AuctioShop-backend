package luke.shopbackend.user;

import luke.shopbackend.order.OrderTestUtils;
import luke.shopbackend.product.ProductTestUtils;
import luke.shopbackend.security.controller.JwtAuthorizationController;
import luke.shopbackend.user.model.UserRequest;
import luke.shopbackend.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtAuthorizationController authorizationController;

    @Test
    void shouldCreateMockMvc() {
        assertThat(mockMvc, is(not(nullValue())));
    }

    @Test
    void getUsersNotAuthenticated() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);

        mockMvc.perform(get("/api/users?page=0&size=2"))
                .andExpect(status().isForbidden());

        then(userService).should(times(0)).getAllUsers(pageable);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void getUsersUSER() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);

        mockMvc.perform(get("/api/users?page=0&size=2"))
                .andExpect(status().isForbidden());

        then(userService).should(times(0)).getAllUsers(pageable);
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void getUsersADMIN() throws Exception {
        Pageable pageable = PageRequest.of(0, 2);
        given(userService.getAllUsers(pageable)).willReturn(UserTestUtils.getUsers());

        mockMvc.perform(get("/api/users?page=0&size=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(2)))
                .andExpect(jsonPath("$.content[0].username", is("Jacek")))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[1].username", is("Inny username")))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(2)));

        then(userService).should().getAllUsers(pageable);
    }

    @Test
    void getUserWithAllDataByIdUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "USER")
    void getUserWithAllDataByIdUSER() throws Exception {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 2);
        given(userService.findUserAndOrderWithDataByUserId(userId, pageable))
                .willReturn(OrderTestUtils.getPageOfOrders());

        mockMvc.perform(get("/api/users/1?page=0&size=2"))
                .andExpect(status().isOk());

        then(userService).should().findUserAndOrderWithDataByUserId(userId, pageable);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getUserWithAllDataByIdADMIN() throws Exception {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 2);
        given(userService.findUserAndOrderWithDataByUserId(userId, pageable))
                .willReturn(OrderTestUtils.getPageOfOrders());

        mockMvc.perform(get("/api/users/1?page=0&size=2"))
                .andExpect(status().isOk());

        then(userService).should().findUserAndOrderWithDataByUserId(userId, pageable);
    }

    @Test
    void deleteUserByIdUnauthenticated() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isForbidden());

        then(userService).should(times(0)).deleteUserAndAllUserDataByUserId(id);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void deleteUserByIdUSER() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isForbidden());

        then(userService).should(times(0)).deleteUserAndAllUserDataByUserId(id);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteUserByIdADMIN() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());

        then(userService).should(times(1)).deleteUserAndAllUserDataByUserId(id);
    }

    @Test
    void register() throws Exception {
        UserRequest userRequest = UserTestUtils.getUserRequest();
        given(userService.addUser(any(UserRequest.class)))
                .willReturn(UserTestUtils.getUserOne());

        mockMvc.perform(post("/api/users/register")
                .content(ProductTestUtils.asJson(userRequest))
                .contentType("application/json;charset=UTF-8")
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isCreated());

        then(userService).should().addUser(any(UserRequest.class));
    }

    @Test
    void registerValidation() throws Exception {
        UserRequest userRequest = new UserRequest();

        mockMvc.perform(post("/api/users/register")
                .content(ProductTestUtils.asJson(userRequest))
                .contentType("application/json;charset=UTF-8")
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.size()", is(3)));

        then(userService).should(times(0)).addUser(any(UserRequest.class));
    }

    @Test
    void registerValidation2() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("aa");
        userRequest.setEmail("aabb@ds");
        userRequest.setPassword("bb");

        mockMvc.perform(post("/api/users/register")
                .content(ProductTestUtils.asJson(userRequest))
                .contentType("application/json;charset=UTF-8")
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.size()", is(3)));

        then(userService).should(times(0)).addUser(any(UserRequest.class));
    }
}





















