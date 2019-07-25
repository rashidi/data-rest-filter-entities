package scratches.boot.data.rest.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rashidi Zin
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
