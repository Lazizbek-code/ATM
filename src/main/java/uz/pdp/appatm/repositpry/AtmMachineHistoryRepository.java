package uz.pdp.appatm.repositpry;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.appatm.entity.AtmMachine;
import uz.pdp.appatm.entity.AtmMachineHistory;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface AtmMachineHistoryRepository extends JpaRepository<AtmMachineHistory, UUID> {

    // KIRIM CHIQIMLAR
    List<AtmMachineHistory> findAllByMoneyOutAndAtmMachine(boolean moneyOut, AtmMachine atmMachine);

    // KUNLIK KIRIM CHIQIMLAR
    List<AtmMachineHistory> findAllByMoneyOutAndAtmMachineAndCreatedAt(boolean moneyOut, AtmMachine atmMachine, Timestamp createdAt);
}
