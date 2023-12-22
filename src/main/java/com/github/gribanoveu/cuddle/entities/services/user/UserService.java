package com.github.gribanoveu.cuddle.entities.services.user;

import com.github.gribanoveu.cuddle.dtos.enums.Role;
import com.github.gribanoveu.cuddle.entities.tables.User;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Evgeny Gribanov
 * @version 28.08.2023
 */
public interface UserService {
    @Transactional void saveUser(User user);
    @Transactional void deleteUserById(Long userId);
    @Transactional void deleteUserByEmail(String userEmail);
    @Transactional void updatePasswordByEmail(User user, String password);
    @Transactional void updateEmail(User user, String newEmail);
    @Transactional void updateEnabled(User user, Boolean enabled);
    @Transactional void updateLocked(User user, Boolean locked);
    @Transactional void updateRole(User user, Role role);
    User findUserByEmail(String email);
    User findUserById(Long id);
    Boolean userExistByEmail(String email);
    List<User> getAllUsers(Pageable pageable);
}
