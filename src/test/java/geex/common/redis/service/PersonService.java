package geex.common.redis.service;

import geex.common.redis.domain.Person;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

/**
 * //TODO description.
 *
 * @author JuChen
 * @date 2019/2/14
 */
@Service
public class PersonService {

    @CachePut(value = "userName")
    public String getUserName(Person person) {
        return person.getLastName() + " " + person.getFirstName();
    }
}
