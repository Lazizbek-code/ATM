package uz.pdp.appatm.repositpry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.appatm.entity.Role;
import uz.pdp.appatm.entity.enums.RoleName;

@RepositoryRestResource(path = "role")
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(RoleName roleName);
}
