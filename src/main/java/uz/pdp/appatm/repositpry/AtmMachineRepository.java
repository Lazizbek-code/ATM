package uz.pdp.appatm.repositpry;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appatm.entity.AtmMachine;

public interface AtmMachineRepository extends JpaRepository<AtmMachine, Integer> {
}
