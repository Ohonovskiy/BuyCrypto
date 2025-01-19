package ohonovskiy.ua.buycrypto.service.user;

import ohonovskiy.ua.buycrypto.DTO.util.auth.UsernamePasswordRequest;
import ohonovskiy.ua.buycrypto.enums.OrderType;
import ohonovskiy.ua.buycrypto.model.user.User;
import ohonovskiy.ua.buycrypto.repository.UserRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(User user) {
        userRepo.save(user);
    }

    public User getById(Long userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Can't find user with id: " + userId));
    }

    public void delete(User user) {
        userRepo.delete(user);
    }

    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    public Double calculateROI() {
        User currentUser = getCurrentUser();

        Double sellOrdersValue = currentUser.getOrders().stream()
                .filter(order -> order.getOrderType() == OrderType.ORDER_SELL)
                .mapToDouble(order -> order.getAmount() * order.getCoin().getPrice())
                .sum();

        Double buyOrdersValue = currentUser.getOrders().stream()
                .filter(order -> order.getOrderType() == OrderType.ORDER_BUY)
                .mapToDouble(order -> order.getPrice() * order.getAmount())
                .sum();

        Double coinValue = currentUser.getUserCoins().stream()
                .mapToDouble(uc -> uc.getCoin().getPrice() * uc.getAmount())
                .sum();

        // ROI = Current Balance + Total Coin Value + Sell Orders + Buy Orders - Initial Investment
        return currentUser.getBalance() + coinValue + sellOrdersValue + buyOrdersValue - currentUser.getInvested();
    }


    public User getByUsername(String username) {
        return userRepo.findFirstByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Can't find user with username: " + username));
    }

    public void register(UsernamePasswordRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        if (username == null || password == null) {
            throw new IllegalArgumentException("Either of fields can be null");
        }

        if(userRepo.findFirstByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken");
        }

        save (
                User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getByUsername(username);
    }

    public User getCurrentUser() {
        return getByUsername(
                        (
                                (UserDetails) SecurityContextHolder
                                        .getContext()
                                        .getAuthentication()
                                        .getPrincipal())
                                .getUsername()
                );

    }
}
