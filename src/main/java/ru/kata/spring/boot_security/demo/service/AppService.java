package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.reposirory.RoleRepository;
import ru.kata.spring.boot_security.demo.reposirory.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AppService implements UserDetailsService {// implements UserDetailsService
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
//под вопросом
    @PersistenceContext
    private EntityManager entityManager;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//    @Transactional
//    @Override
//    public void save(User user) {
//        if (user.getRoles().isEmpty()) {
//            user.setRoles(Collections.singletonList(roleRepository.findRoleByName("ROLE_USER")));
//        }
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userRepository.save(user);
//    }
    @Autowired
    public AppService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    @Transactional
    public void saveUser(User user){
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void removeUser(Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            try {
                userRepository.delete(user.get());
            } catch (PersistenceException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                getAuthorities(user));
    }
    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String[] userRoles = user.getRoles().stream().map((role) -> role.getName()).toArray(String[]::new);
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
        return authorities;
    }
}
