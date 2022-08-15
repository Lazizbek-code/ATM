package uz.pdp.appatm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class AtmMachineHistory {
    @Id
    @GeneratedValue
    private UUID id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @CreatedBy
    private Integer createdBy;

    @Column(nullable = false)
    private boolean moneyOut; // pul olingan yoki bankomatga qo'yolgan

    @Column(nullable = false)
    private Integer amount;

    @ManyToOne(optional = false)
    private Currency currency;

    @ManyToOne(optional = false)
    private AtmMachine atmMachine;
}
