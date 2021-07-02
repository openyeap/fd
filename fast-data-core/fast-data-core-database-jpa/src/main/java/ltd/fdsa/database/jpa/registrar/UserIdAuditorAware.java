package ltd.fdsa.database.jpa.registrar;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
public class UserIdAuditorAware implements AuditorAware<Integer> {
    @Autowired
    protected HttpServletRequest request;


    @Override
    public Optional<Integer> getCurrentAuditor() {
        var userId = this.request.getHeader("X-USERID");
        if (Strings.isNullOrEmpty(userId)) {
            return Optional.of(-1);
        }
        return Optional.of(Integer.parseInt(userId));
    }
}
