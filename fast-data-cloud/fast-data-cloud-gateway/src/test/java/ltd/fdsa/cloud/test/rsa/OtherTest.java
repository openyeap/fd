package ltd.fdsa.cloud.test.rsa;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.junit.Before;
import org.junit.Test;
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

@Slf4j
public class OtherTest {
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

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() {
        var address = this.findFirstNonLoopbackAddress().getHostAddress();
        log.info("{}", address);
        System.out.println("address");
        System.out.println(address);
        System.out.println("address");
    }

    private InetUtilsProperties properties = new InetUtilsProperties();

    boolean ignoreInterface(String interfaceName) {
        Iterator<String> var2 = this.properties.getIgnoredInterfaces().iterator();

        String regex;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            regex = (String) var2.next();
        } while (!interfaceName.matches(regex));

        log.trace("Ignoring interface: " + interfaceName);
        return true;
    }

    private InetAddress findFirstNonLoopbackAddress() {
        InetAddress result = null;

        try {
            int lowest = 2147483647;
            Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();

            label61: while (true) {
                NetworkInterface ifc;
                do {
                    while (true) {
                        do {
                            if (!nics.hasMoreElements()) {
                                break label61;
                            }

                            ifc = (NetworkInterface) nics.nextElement();
                        } while (!ifc.isUp());

                        log.trace("Testing interface: " + ifc.getDisplayName());
                        if (ifc.getIndex() >= lowest && result != null) {
                            if (result != null) {
                                continue;
                            }
                        }

                        lowest = ifc.getIndex();
                        break;
                    }
                } while (this.ignoreInterface(ifc.getDisplayName()));

                Enumeration<InetAddress> addrs = ifc.getInetAddresses();

                while (addrs.hasMoreElements()) {
                    InetAddress address = addrs.nextElement();
                    if (address instanceof Inet4Address && !address.isLoopbackAddress()
                            && this.isPreferredAddress(address)) {
                        log.trace("Found non-loopback interface: " + ifc.getDisplayName());
                        result = address;
                    }
                }
            }
        } catch (IOException var8) {
            log.error("Cannot get first non-loopback address", var8);
        }

        if (result != null) {
            return result;
        } else {
            try {
                return InetAddress.getLocalHost();
            } catch (UnknownHostException var7) {
                log.warn("Unable to retrieve localhost");
                return null;
            }
        }
    }

    boolean isPreferredAddress(InetAddress address) {
        if (this.properties.isUseOnlySiteLocalInterfaces()) {
            boolean siteLocalAddress = address.isSiteLocalAddress();
            if (!siteLocalAddress) {
                log.trace("Ignoring address: " + address.getHostAddress());
            }

            return siteLocalAddress;
        } else {
            List<String> preferredNetworks = this.properties.getPreferredNetworks();
            if (preferredNetworks.isEmpty()) {
                return true;
            } else {
                Iterator<String> var3 = preferredNetworks.iterator();

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

    @Data
    @ConfigurationProperties(InetUtilsProperties.PREFIX)
    public class InetUtilsProperties {
        public static final String PREFIX = "spring.cloud.inetutils";
        private String defaultHostname = "localhost";
        private String defaultIpAddress = "127.0.0.1";
        private List<String> ignoredInterfaces = new ArrayList<String>();
        private boolean useOnlySiteLocalInterfaces = false;
        private List<String> preferredNetworks = new ArrayList<String>();

        public InetUtilsProperties() {
        }

    }

}
