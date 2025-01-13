package ohonovskiy.ua.buycrypto.model.user;

import jakarta.persistence.*;
import lombok.*;
import ohonovskiy.ua.buycrypto.enums.RoleType;
import ohonovskiy.ua.buycrypto.model.SimpleEntityModel;
import ohonovskiy.ua.buycrypto.model.crypto.Order;
import ohonovskiy.ua.buycrypto.model.crypto.UserCoin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
public class User extends SimpleEntityModel implements UserDetails {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @Column(nullable = false)
    private Double balance = 0.0;

    @Builder.Default
    private Double invested = 0.0;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private RoleType roleType = RoleType.ROLE_ADMIN; //TODO CHANGE

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserCoin> userCoins = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    @Builder.Default
    private boolean isEnabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(roleType.name()));
    }

    @Override
    public boolean isAccountNonExpired() { return isEnabled; }

    @Override
    public boolean isAccountNonLocked() { return isEnabled; }

    @Override
    public boolean isCredentialsNonExpired() { return isEnabled; }

    @Override
    public boolean isEnabled() { return isEnabled; }

    public void addOrder(Order order) {
        order.setUser(this);
        this.orders.add(order);
    }

    public void removeOrder(Order order) {
        order.setUser(null);
        this.orders.remove(order);
    }

    public void setInvested(Double newInvested) {
        if(newInvested < invested)
            throw new IllegalArgumentException("Can't decrease the value.");

        this.invested = newInvested;
    }

    public void addBalance(Double balanceToAdd) {
        this.balance += balanceToAdd;
    }

    public void invest(Double sum) {
        this.balance += sum;
        this.invested += sum;
    }
}
