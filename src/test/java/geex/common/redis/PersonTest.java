package geex.common.redis;

import geex.common.redis.domain.Person;
import geex.common.redis.service.PersonService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * //TODO description.
 *
 * @author JuChen
 * @date 2019/2/14
 */
public class PersonTest extends TestBase {
    @Autowired
    private PersonService personService;

    @Test
    public void testCache() {
        Person person = new Person("Wu", "San", 20);
        personService.getUserName(person);
    }
}
