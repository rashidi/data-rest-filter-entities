package scratches.boot.data.rest.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Rashidi Zin
 */
@Configuration
public class UserResourceProcessorConfiguration {

    @Bean
    public ResourceProcessor<Resource<User>> removeDeleted() {
        return resource -> {
            var user = resource.getContent();

            if (UserStatus.DELETED == user.getStatus()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            return resource;
        };
    }

}
