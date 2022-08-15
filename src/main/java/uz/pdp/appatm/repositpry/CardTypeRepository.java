package uz.pdp.appatm.repositpry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.appatm.entity.CardType;

@RepositoryRestResource(path = "cardType")
public interface CardTypeRepository extends JpaRepository<CardType, Integer> {
}
