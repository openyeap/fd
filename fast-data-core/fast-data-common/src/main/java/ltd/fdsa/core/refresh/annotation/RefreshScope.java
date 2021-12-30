package ltd.fdsa.core.refresh.annotation;

import ltd.fdsa.core.refresh.RefreshScopeRegistry;
import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(RefreshScopeRegistry.NAME)
public @interface RefreshScope {


}
