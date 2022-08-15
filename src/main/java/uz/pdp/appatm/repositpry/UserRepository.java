package uz.pdp.appatm.repositpry;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appatm.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndEmailCode(String email, String emailCode);

    Optional<User> findByEmail(String email);
}
