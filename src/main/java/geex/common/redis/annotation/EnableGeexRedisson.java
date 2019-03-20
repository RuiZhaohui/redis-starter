package geex.common.redis.annotation;

import geex.common.redis.cache.RedissonCacheConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * //TODO description.
 *
 * @author JuChen
 * @date 2019/1/23
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedissonCacheConfig.class)
public @interface EnableGeexRedisson {
}
