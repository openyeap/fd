package ltd.fdsa.database.mybatis.plus.interceptor;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@Component
public class AutoUpdateHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var userId = request.getHeader("X_USER_ID");
        if (Strings.isNullOrEmpty(userId)) {
            this.setFieldValByName("updateBy", -1, metaObject);
            this.setFieldValByName("createBy", -1, metaObject);

        } else {
            this.setFieldValByName("updateBy", userId, metaObject);
            this.setFieldValByName("createBy", userId, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        var userId = request.getHeader("X_USER_ID");
        if (Strings.isNullOrEmpty(userId)) {
            this.setFieldValByName("updateBy", -1, metaObject);
        } else {
            this.setFieldValByName("updateBy", userId, metaObject);
        }
    }
}
