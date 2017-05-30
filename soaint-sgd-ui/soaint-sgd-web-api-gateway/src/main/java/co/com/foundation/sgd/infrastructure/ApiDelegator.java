package co.com.foundation.sgd.infrastructure;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Service
public @interface ApiDelegator {
}
