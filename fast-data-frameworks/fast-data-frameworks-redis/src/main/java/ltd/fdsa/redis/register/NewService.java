package ltd.fdsa.redis.register;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ltd.fdsa.redis.properties.RedisConfigProperties;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

@Slf4j
@Data
public class NewService {
    private final RedisConfigProperties properties;

    private final String host;
    private final int port;
    private final String name;
    private final String id;

    public NewService(RedisConfigProperties properties) {
        this.properties = properties;
        this.host = getApplicationHost();
        this.port = this.properties.getApplicationPort();
        this.name = this.properties.getName();
        this.id = this.host + ":" + this.port + "/" + this.name;
    }

    private String getApplicationHost() {
        if (!Strings.isNullOrEmpty(this.properties.getApplicationHost())) {
            return this.properties.getApplicationHost();
        }
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        this.properties.setApplicationHost(ip.getHostAddress());
                        return this.properties.getApplicationHost();
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取IP异常", e);
        }
        return this.properties.getApplicationHost();
    }


}