package nl.rcomanne.passwordmanager.repository;

import nl.rcomanne.passwordmanager.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByMail(String mail);
}
