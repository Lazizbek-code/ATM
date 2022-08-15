package uz.pdp.appatm.repositpry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.appatm.entity.Client;

import java.util.UUID;

@RepositoryRestResource(path = "client")
public interface ClientRepository extends JpaRepository<Client, UUID> {
}
