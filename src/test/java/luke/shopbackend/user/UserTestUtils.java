package luke.shopbackend.user;

import luke.shopbackend.user.enums.ShopRole;
import luke.shopbackend.user.model.Role;
import luke.shopbackend.user.model.User;
import luke.shopbackend.user.model.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface UserTestUtils {

    static Page<User> getUsers() {
        User user1 = getUserOne();
        User user2 = getUserTwo();

        return new PageImpl<>(List.of(user1, user2));
    }

    static User getUserOne() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Jacek");
        user.setPassword("JegoHaslo");
        user.setEmail("innyEmail@gmail.com");
        user.getRoles().add(createRole());
        return user;
    }

    static User getUserTwo() {
        User user = new User();
        user.setId(2L);
        user.setUsername("Inny username");
        user.setPassword("JegoHaslo");
        user.setEmail("mail@o2.pl");
        user.getRoles().add(createRole());
        return user;
    }

    static Role createRole() {
        Role userRole1 = new Role();
        userRole1.setRole(ShopRole.USER);
        return userRole1;
    }

    static UserRequest getUserRequest() {
        UserRequest request = new UserRequest();
        request.setUsername("jacek");
        request.setPassword("JegoHaslo");
        request.setEmail("innyemail@gmail.com");
        return request;
    }
}
