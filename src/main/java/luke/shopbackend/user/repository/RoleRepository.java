package luke.shopbackend.user.repository;

import luke.shopbackend.user.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
