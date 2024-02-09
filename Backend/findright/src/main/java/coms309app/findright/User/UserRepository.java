package coms309app.findright.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();

    Optional<User> findByEmail(String email);

    void deleteById(Long id);

    void deleteByEmail(String email);
}
