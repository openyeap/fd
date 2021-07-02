package ltd.fdsa.core.properties;

import com.google.common.base.Strings;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;


@Data
@ConfigurationProperties(prefix = ProjectProperties.PREFIX)
@ToString
@Slf4j
public class ProjectProperties implements InitializingBean {
    public static final String PREFIX = "project";

    /**
     * default application name.
     */
    @Value("${project.name:${spring.application.name:project}}")
    private String name;

    @Value("${server.address:${spring.application.address:}}")
    private String address;

    @Value("${server.port:8080}")
    private int port;

    /**
     * ignored network interfaces.
     */
    private List<String> ignoredInterfaces = new ArrayList();
    private boolean useOnlySiteLocalInterfaces = false;
    /**
     * preferred network interfaces.
     */
    private List<String> preferredNetworks = new ArrayList();

    @Override
    public void afterPropertiesSet() throws Exception {

        if (Strings.isNullOrEmpty(this.address)) {

            this.address = getHostAddress();
        }
        log.info("{}", this);
    }

    private String getHostAddress() {

        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取IP异常", e);
        }
        return "";
    }

    /**
     * 针对多网卡、容器内部署等情况，可借助 "spring-cloud-commons" 提供的 "InetUtils" 组件灵活定制注册IP；
     * <p>
     * 1、引入依赖：
     * <dependency>
     * <groupId>org.springframework.cloud</groupId>
     * <artifactId>spring-cloud-commons</artifactId>
     * <version>${version}</version>
     * </dependency>
     * <p>
     * 2、配置文件，或者容器启动变量
     * spring.cloud.inetutils.preferred-networks: 'xxx.xxx.xxx.'
     * <p>
     * 3、获取IP
     * String ip_ = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
     */

    private InetAddress findFirstNonLoopBackAddress() {
        InetAddress result = null;

        try {
            int lowest = 2147483647;
            Enumeration nics = NetworkInterface.getNetworkInterfaces();

            label61:
            while (true) {
                NetworkInterface ifc;
                do {
                    while (true) {
                        do {
                            if (!nics.hasMoreElements()) {
                                break label61;
                            }

                            ifc = (NetworkInterface) nics.nextElement();
                        } while (!ifc.isUp());

                        this.log.trace("Testing interface: " + ifc.getDisplayName());
                        if (ifc.getIndex() >= lowest && result != null) {
                            if (result != null) {
                                continue;
                            }
                            break;
                        }

                        lowest = ifc.getIndex();
                        break;
                    }
                } while (this.ignoreInterface(ifc.getDisplayName()));

                Enumeration addrs = ifc.getInetAddresses();

                while (addrs.hasMoreElements()) {
                    InetAddress address = (InetAddress) addrs.nextElement();
                    if (address instanceof Inet4Address && !address.isLoopbackAddress() && this.isPreferredAddress(address)) {
                        this.log.trace("Found non-loopback interface: " + ifc.getDisplayName());
                        result = address;
                    }
                }
            }
        } catch (IOException var8) {
            this.log.error("Cannot get first non-loopback address", var8);
        }

        if (result != null) {
            return result;
        } else {
            try {
                return InetAddress.getLocalHost();
            } catch (UnknownHostException var7) {
                this.log.warn("Unable to retrieve localhost");
                return null;
            }
        }
    }


    boolean ignoreInterface(String interfaceName) {
        Iterator iterator = this.ignoredInterfaces.iterator();

        String regex;
        do {
            if (!iterator.hasNext()) {
                return false;
            }

            regex = (String) iterator.next();
        } while (!interfaceName.matches(regex));

        log.trace("Ignoring interface: " + interfaceName);
        return true;
    }


    private boolean isPreferredAddress(InetAddress address) {
        if (this.useOnlySiteLocalInterfaces) {
            boolean siteLocalAddress = address.isSiteLocalAddress();
            if (!siteLocalAddress) {
                log.trace("Ignoring address: " + address.getHostAddress());
            }

            return siteLocalAddress;
        } else {
            List<String> preferredNetworks = this.preferredNetworks;
            if (preferredNetworks.isEmpty()) {
                return true;
            } else {
                Iterator var3 = preferredNetworks.iterator();

                String regex;
                String hostAddress;
                do {
                    if (!var3.hasNext()) {
                        log.trace("Ignoring address: " + address.getHostAddress());
                        return false;
                    }

                    regex = (String) var3.next();
                    hostAddress = address.getHostAddress();
                } while (!hostAddress.matches(regex) && !hostAddress.startsWith(regex));

                return true;
            }
        }
    }

}