package uz.pdp.appatm.repositpry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.appatm.entity.Bank;

@RepositoryRestResource(path = "bank")
public interface BankRepository extends JpaRepository<Bank, Integer> {
}
