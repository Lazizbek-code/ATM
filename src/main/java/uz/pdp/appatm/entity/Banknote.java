package uz.pdp.appatm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Banknote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer banknoteValue;

    @Column(nullable = false)
    private Integer amount;

    @SneakyThrows
    public void setAmount(Integer amount) {
        if (amount >= 0) {
            this.amount = amount;
        } else {
            throw new Exception("Banknota qolmadi");
        }
    }
}
