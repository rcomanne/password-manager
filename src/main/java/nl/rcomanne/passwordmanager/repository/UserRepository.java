package nl.rcomanne.passwordmanager.repository;

import nl.rcomanne.passwordmanager.domain.CustomUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<CustomUser, String> {

    Optional<CustomUser> findByMail(String mail);
}
