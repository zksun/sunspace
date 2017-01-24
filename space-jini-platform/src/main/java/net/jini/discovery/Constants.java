package net.jini.discovery;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/**
 * Created by zksun on 23/01/2017.
 */
public class Constants {
    public static final String MULTICAST_ANNOUNCEMENT_ADDRESS_PROPERTY = "com.space.multicast.announcement";
    public static final String MULTICAST_REQUEST_ADDRESS_PROPERTY = "com.space.multicast.request";
    public static final String MULTICAST_DISCOVERY_PORT_PROPERTY = "com.space.multicast.discoveryPort";
    public static final String MULTICAST_TTL_PROPERTY = "com.space.multicast.ttl";
    public static final String MULTICAST_ENABLED_PROPERTY = "com.space.multicast.enabled";
    public static final String USE_SOCKET_KEEP_ALIVE = "com.space.discover.useSocketKeepAlive";
    public static final String USE_SOCKET_TCP_NO_DELAY = "com.space.discovery.userSocketTcpNoDelay";
    private static final String HOST_ADDRESS = "java.rmi.server.hostname";
    private static final Logger logger = Logger.getLogger(Constants.class.getName());
    private static InetAddress requestAddress = null;
    private static InetAddress announcementAddress = null;
    private static Integer discoveryPort;
    private static Integer ttl;
    private static Boolean multicastEnabled;
    private static Boolean useSocketKeepAlive;
    private static Boolean useTcpNoDelay;
    private static AtomicBoolean isLocalHostLoaded = new AtomicBoolean(false);

    private Constants() {
    }

    public static final InetAddress getRequestAddress() throws UnknownHostException {
        if (null == requestAddress) {
            requestAddress = InetAddress.getByName(System.getProperty(MULTICAST_REQUEST_ADDRESS_PROPERTY, "224.0.1.187"));
        }
        return requestAddress;
    }

    public static final InetAddress getAnnouncementAddress() throws UnknownHostException {
        if (null == announcementAddress) {
            announcementAddress = InetAddress.getByName(System.getProperty(MULTICAST_ANNOUNCEMENT_ADDRESS_PROPERTY, "224.0.1.188"));
        }

        return announcementAddress;
    }

    public static int getDiscoveryPort() {
        if (null == discoveryPort) {
            discoveryPort = Integer.getInteger(MULTICAST_DISCOVERY_PORT_PROPERTY, 4174);
        }

        return discoveryPort.intValue();
    }

    public static int getTtl() {
        if (null == ttl) {
            ttl = Integer.getInteger(MULTICAST_TTL_PROPERTY, 3);
        }

        return ttl.intValue();
    }

    public static boolean isMulticastEnabled() {
        if (null == multicastEnabled) {
            multicastEnabled = Boolean.valueOf(System.getProperty(MULTICAST_ENABLED_PROPERTY, "true"));
        }

        return multicastEnabled.booleanValue();
    }

    public static boolean useSocketKeepAlive() {
        if (null == useSocketKeepAlive) {
            useSocketKeepAlive = Boolean.valueOf(System.getProperty(USE_SOCKET_KEEP_ALIVE, "true"));
        }

        return useSocketKeepAlive.booleanValue();
    }

    public static boolean useSocketTcpNoDelay() {
        if (null == useTcpNoDelay) {
            useTcpNoDelay = Boolean.valueOf(System.getProperty(USE_SOCKET_TCP_NO_DELAY, "true"));
        }

        return useTcpNoDelay.booleanValue();
    }

    private static enum HostTranslationType {
        ip, ip6, host, canonicalhost;
    }


}
