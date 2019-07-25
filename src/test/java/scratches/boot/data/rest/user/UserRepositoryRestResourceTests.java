package scratches.boot.data.rest.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static scratches.boot.data.rest.user.UserStatus.ACTIVE;
import static scratches.boot.data.rest.user.UserStatus.DELETED;

/**
 * @author Rashidi Zin
 */
@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
public class UserRepositoryRestResourceTests {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private MockMvc mvc;

    @Test
    public void getActiveUser() throws Exception {
        String name = "Rashidi Zin", username = "rashidi";

        Long id = em.persistAndGetId(new User(name, username), Long.class);

        mvc
                .perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(name)))
                .andExpect(jsonPath("username", is(username)))
                .andExpect(jsonPath("status", is(ACTIVE.name())));
    }

    @Test
    public void getDeletedUser() throws Exception {
        String name = "Rashidi Zin", username = "rashidi";

        User user = new User(name, username);

        user.setStatus(DELETED);

        Long id = em.persistAndGetId(user, Long.class);

        mvc
                .perform(get("/users/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAll() throws Exception {
        var activeUser = new User("Active User", "active.user");

        em.persist(activeUser);

        var deletedUser = new User("Deleted User", "deleted.user");

        deletedUser.setStatus(DELETED);

        em.persist(deletedUser);

        mvc
                .perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("page.totalElements", is(1)))
                .andExpect(jsonPath("_embedded.users[0].username", is("active.user")));
    }

    @Test
    public void getAllWithPagination() throws Exception {
        em.persist(new User("Active User", "active.user"));
        em.persist(new User("Another Active User", "another.active.user"));

        var deletedUser = new User("Deleted User", "deleted.user");

        deletedUser.setStatus(DELETED);

        em.persist(deletedUser);

        mvc
                .perform(get("/users").param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("page.totalElements", is(2)))
                .andExpect(jsonPath("_embedded.users", hasSize(1)))
                .andExpect(jsonPath("_embedded.users[0].username", is("active.user")));
    }

}