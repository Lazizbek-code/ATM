package uz.pdp.appatm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Bank bank;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private AtmMachine atmMachine;
}
