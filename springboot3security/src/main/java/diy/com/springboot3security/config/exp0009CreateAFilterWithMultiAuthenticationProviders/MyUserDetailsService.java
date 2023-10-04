package diy.com.springboot3security.config.exp0009CreateAFilterWithMultiAuthenticationProviders;


import diy.com.springboot3security.persistent.entity.Users;
import diy.com.springboot3security.persistent.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Slf4j
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService{


    final private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Users> userByUsername = usersRepository.findByUsername(username);
        if (!userByUsername.isPresent()) {
            log.error("Could not find user with that username: {}", username);
            throw new UsernameNotFoundException("Invalid credentials!");
        }
        Users user = userByUsername.get();
        if (user == null || !user.getUsername().equals(username)) {
            log.error("Could not find user with that username: {}", username);
            throw new UsernameNotFoundException("Invalid credentials!");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        user.getAuthorities().stream().forEach(authority ->
                grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority())));

        return new MyUser(user.getUsername(), user.getPassword(), true, true, true, true, grantedAuthorities,
                user.getFirst_name(), user.getLast_name(), user.getEmail_address(), user.getBirthdate());
    }

}
