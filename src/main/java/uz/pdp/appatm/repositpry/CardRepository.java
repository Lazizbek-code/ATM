package uz.pdp.appatm.repositpry;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appatm.entity.Card;

import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {

    Optional<Card> findBySpecialCode(String specialCode);
}
