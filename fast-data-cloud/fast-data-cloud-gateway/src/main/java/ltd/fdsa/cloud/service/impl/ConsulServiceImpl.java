package ltd.fdsa.cloud.service.impl;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.cloud.constant.Constant;
import ltd.fdsa.cloud.service.ConsulService;
import ltd.fdsa.core.util.NamingUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ConsulServiceImpl implements ApplicationListener<ApplicationStartedEvent>, ConsulService {

    private static ConcurrentHashMap<String, String> authList = new ConcurrentHashMap<>();
    @Autowired
    private ConsulClient consulClient;
    private Thread authThread;

    @Override
    public boolean checkAuthorize(String path, String[] userRoles) {
        AntPathMatcher matcher = new AntPathMatcher();

        String[] accessRoles = null;
        for (Map.Entry<String, String> entry : authList.entrySet()) {
            if (matcher.match(entry.getKey(), path)) {
                accessRoles = entry.getValue().split(",");
                break;
            }
        }
        if (accessRoles != null) {
            List<String> accessRoleList = Arrays.asList(accessRoles);
            if (accessRoleList.contains("*")) {
                return true;
            }
            if (userRoles == null || userRoles.length == 0) {
                return false;
            }
            for (String userRole : userRoles) {
                if (accessRoleList.contains(userRole)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }


    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        if (authThread == null) {
            authThread = new Thread(() -> {
                long index = 0;
                while (true) {
                    try {
                        NamingUtils.formatLog(log,"try to get config:{}", index);
                        QueryParams queryParams = QueryParams.Builder.builder().setIndex(index).build();
                        Response<List<GetValue>> result = this.consulClient.getKVValues(Constant.AUTH + "/", queryParams);
                        index = result.getConsulIndex();
                        if (result != null && result.getValue() != null && result.getValue().size() != 0) {
                            for (GetValue gv : result.getValue()) {
                                String value = gv.getValue();
                                if (StringUtils.isEmpty(value)) {
                                    continue;
                                }
                                value = new String(Base64.decode(gv.getValue()));
                                Map<String, String> map = new ObjectMapper().readValue(value, Map.class);
                                for (Map.Entry<String, String> entry : map.entrySet()) {
                                    authList.put(entry.getKey(), entry.getValue());
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.info(e.getMessage());
                    }
                }
            });
            authThread.start();
        }
    }
}
