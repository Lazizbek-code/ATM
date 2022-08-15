package uz.pdp.appatm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import uz.pdp.appatm.entity.enums.RoleName;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // unique id

    @Enumerated(EnumType.STRING)
    private RoleName roleName; // role name

    // constructor
    public Role(RoleName roleName) {
        this.roleName = roleName;
    }

    //============= METHOD OF GRANTED AUTHORITY =============//

    @Override
    public String getAuthority() {
        return roleName.name();
    }
}
