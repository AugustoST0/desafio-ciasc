package com.exemplo.repositories;

import com.exemplo.model.user.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    @Transactional
    public User save(User user) {
        persistAndFlush(user);
        return user;
    }
}
