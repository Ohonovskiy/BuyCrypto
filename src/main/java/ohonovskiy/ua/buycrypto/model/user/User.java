package ohonovskiy.ua.buycrypto.model.user;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
import ohonovskiy.ua.buycrypto.enums.RoleType;
import ohonovskiy.ua.buycrypto.model.SimpleEntityModel;
import ohonovskiy.ua.buycrypto.model.crypto.Order;
import ohonovskiy.ua.buycrypto.model.crypto.UserCoin;
import ohonovskiy.ua.buycrypto.util.exception.NotEnoughBalanceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

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

    @Transactional
    public void addOrder(Order order) {
        subtractBalance(order.getAmount());
        order.setUser(this);
        this.orders.add(order);
    }

    @Transactional
    public void cancelOrder(Order order) {
        Optional<Order> optionalOrder = this.orders.stream()
                .filter(o -> o.equals(order))
                .findFirst();

        if (optionalOrder.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }

        Order nonOptional = optionalOrder.get();
        addBalance(nonOptional.getAmount());

        nonOptional.setUser(null);
        orders.remove(nonOptional);
    }

    @Transactional
    public void addBalance(Double addToBalance) {
        this.setInvested(this.invested+=addToBalance);
        this.balance += addToBalance;
    }

    public void subtractBalance(double amount) {
        if (this.getBalance() < amount) {
            throw new NotEnoughBalanceException("Not enough balance");
        }
        this.setBalance(this.getBalance() - amount);
    }

    public void setInvested(Double newInvested) {
        if(newInvested < invested)
            throw new IllegalArgumentException("Can't decrease the value.");

        this.invested = newInvested;
    }
}
