package uz.pdp.appatm.repositpry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.appatm.entity.Banknote;

@RepositoryRestResource(path = "banknote")
public interface BanknoteRepository extends JpaRepository<Banknote, Integer> {
}
