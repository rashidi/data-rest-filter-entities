package scratches.boot.data.rest.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author Rashidi Zin
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    @Query("SELECT u FROM User u WHERE u.status = scratches.boot.data.rest.user.UserStatus.ACTIVE")
    Page<User> findAll(Pageable pageable);

    @Override
    @Query("SELECT u FROM User u WHERE u.id = ?1 AND u.status = scratches.boot.data.rest.user.UserStatus.ACTIVE")
    Optional<User> findById(Long id);

}
