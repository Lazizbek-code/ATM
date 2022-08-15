package uz.pdp.appatm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Client {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Card> cards;

    public void setCards(List<Card> cards) {
        for (Card card : cards) {
            card.setClient(this);
        }
        this.cards = cards;
    }
}
