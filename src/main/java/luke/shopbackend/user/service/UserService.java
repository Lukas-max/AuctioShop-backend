package luke.shopbackend.user.service;

import luke.shopbackend.order.model.entity.CustomerOrder;
import luke.shopbackend.user.model.User;
import luke.shopbackend.user.model.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<User> getAllUsers(Pageable pageable);

    Page<CustomerOrder> findUserAndOrderWithDataByUserId(Long id, Pageable pageable);

    User getUserByUsername(String username);

    User getUserById(Long id);

    void deleteUserAndAllUserDataByUserId(Long id);

    User addUser(UserRequest userRequest);
}
