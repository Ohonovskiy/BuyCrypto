package ohonovskiy.ua.buycrypto.model.user;

import jakarta.persistence.*;
import lombok.*;
import ohonovskiy.ua.buycrypto.enums.Role;
import ohonovskiy.ua.buycrypto.model.SimpleEntityModel;
import ohonovskiy.ua.buycrypto.model.crypto.Coin;
import ohonovskiy.ua.buycrypto.model.crypto.UserCoin;
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
    private Double balance = 10000.0;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_ADMIN; //TODO CHANGE

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserCoin> userCoins = new ArrayList<>();

    @Builder.Default
    private boolean isEnabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() { return isEnabled; }

    @Override
    public boolean isAccountNonLocked() { return isEnabled; }

    @Override
    public boolean isCredentialsNonExpired() { return isEnabled; }

    @Override
    public boolean isEnabled() { return isEnabled; }
}
