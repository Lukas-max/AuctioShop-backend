package luke.shopbackend.model.entity;

import luke.shopbackend.model.enums.ShopRole;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    @NotNull
    private ShopRole role;

    public Role() {
    }

    public Role(ShopRole role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShopRole getRole() {
        return role;
    }

    public void setRole(ShopRole role) {
        this.role = role;
    }
}
