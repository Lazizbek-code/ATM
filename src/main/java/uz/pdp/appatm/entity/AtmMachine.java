package uz.pdp.appatm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AtmMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    private Bank bank;

    private Double maxCashTakeOff = 1_000_000.0;

    @ManyToOne(optional = false)
    private CardType cardType;

    @ManyToOne(optional = false)
    private Currency currency;

    @OneToOne(mappedBy = "atmMachine", cascade = CascadeType.ALL)
    private Address address;

    @ManyToMany
    private List<Banknote> banknoteAmount;

    private Double minCash = 5_000_000.0;

    private Double moneyAmount;

    public void setMoneyAmount() {
        double amount = 0;
        for (Banknote banknote : banknoteAmount) {
            amount = amount + (banknote.getAmount() * banknote.getBanknoteValue());
        }
        this.moneyAmount = amount;
    }
}
