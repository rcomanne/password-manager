package nl.rcomanne.passwordmanager.repository;

import nl.rcomanne.passwordmanager.domain.CustomUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<CustomUser, Long> {

    Optional<CustomUser> findByMail(String mail);
}
