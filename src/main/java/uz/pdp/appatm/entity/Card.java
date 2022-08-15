package uz.pdp.appatm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Card implements UserDetails {
    @Id
    @GeneratedValue
    private UUID id;

    @Size(min = 16, max = 16)
    @Column(nullable = false, unique = true)
    private String specialCode;

    @Size(min = 3, max = 3)
    @Column(nullable = false)
    private Integer cvv;

    @Size(min = 4, max = 4)
    @Column(nullable = false)
    private String pinCode;

    @Column(nullable = false)
    private Date expireDate;

    private Double balance = 0.0;

    @ManyToOne(optional = false)
    private Bank bank;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Client client;

    @ManyToOne(optional = false)
    private CardType cardType;

    @ManyToOne(optional = false)
    private Currency currency;

    @OneToOne(optional = false)
    private Role role;

    private boolean accountNonExpired = true; // user's account has not expired

    private boolean accountNonLocked = true; // user's account has not locked

    private boolean credentialsNonExpired = true; // user's credentials has not expired

    private boolean enabled; // user's account is enabled

    //================= METHODS OF USER DETAILS =================//

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(this.role);
    }

    @Override
    public String getPassword() {
        return pinCode;
    }

    @Override
    public String getUsername() {
        return specialCode;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
