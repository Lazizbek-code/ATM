package uz.pdp.appatm.repositpry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.appatm.entity.Currency;

@RepositoryRestResource(path = "currency")
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
}
