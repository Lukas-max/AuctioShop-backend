package luke.shopbackend.model;

import luke.shopbackend.model.enums.ShopRole;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
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
