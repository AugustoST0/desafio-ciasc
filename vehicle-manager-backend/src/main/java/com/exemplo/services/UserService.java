package com.exemplo.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.exemplo.exception.exceptions.RegisterNotFoundException;
import com.exemplo.model.user.User;
import com.exemplo.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll().list();
    }

    public User getUserById(Long id) {
        User user = userRepository.findById(id);

        if (user == null) {
            throw new RegisterNotFoundException("Usuário não encontrado");
        }

        return user;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.find("email", email).firstResult();

        if (user == null) {
            throw new RegisterNotFoundException("Usuário não encontrado");
        }

        return user;
    }

    @Transactional
    public User registerUser(User user) {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());

        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    @Transactional
    public void updateUser(Long id, User updatedUser) {
        User user = getUserById(id);

        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }

        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().equals(user.getPassword())) {
            String hashedPassword = BCrypt.withDefaults()
                    .hashToString(12, updatedUser.getPassword().toCharArray());
            user.setPassword(hashedPassword);
        }

        userRepository.persist(user);
    }

    @Transactional
    public void deleteUserById(Long id) {
        User user = getUserById(id);
        userRepository.deleteById(id);
    }
}
