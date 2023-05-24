package cn.zhumingwu.base.refresh.annotation;

import cn.zhumingwu.base.refresh.RefreshScopeRegistry;
import org.springframework.context.annotation.Scope;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(RefreshScopeRegistry.NAME)
public @interface RefreshScope {


}
