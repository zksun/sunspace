package net.jini.discovery;

import com.space.internal.io.BootIOUtils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
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

    public static String getHostAddress() throws UnknownHostException {
        if (!isLocalHostLoaded.getAndSet(true)) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("---Before local host initialization---");
            }

            try {
                InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING, e.toString(), e);
                }
            }
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("---After local host initialization---");
            }
        }

        String value = System.getProperty(HOST_ADDRESS);
        if (null == value) {
            return BootIOUtils.wrapIpv6HostAddressIfNeeded(InetAddress.getLocalHost());
        } else if (value.startsWith("#") && value.endsWith("#")) {
            value = translateHostName(value);
            System.setProperty(HOST_ADDRESS, value);
            return value;
        } else {
            InetAddress inetAddress = InetAddress.getByName(value);
            if (inetAddress instanceof Inet6Address && value.startsWith("[")) {
                return value;
            } else {
                String hostAddress = inetAddress.getHostAddress();
                if (value.equalsIgnoreCase(hostAddress)) {
                    return hostAddress;
                } else {
                    String canonicalHostName = inetAddress.getCanonicalHostName();
                    if (value.equalsIgnoreCase(canonicalHostName)) {
                        return canonicalHostName;
                    } else {
                        if (logger.isLoggable(Level.FINE)) {
                            logger.fine(">>> RETURN inetAddress:" + inetAddress + ", value=" + value + ", object=" + Integer.toHexString(System.identityHashCode(inetAddress)));
                        }

                        return inetAddress.getHostName();
                    }
                }
            }
        }
    }

    private static String translateHostName(String value) throws UnknownHostException {
        value = value.substring(1, value.length() - 1);
        String name = value.substring(0, value.indexOf(':'));
        String type = value.substring(value.indexOf(':') + 1);
        HostTranslationType hostTranslationType = null;

        try {
            hostTranslationType = HostTranslationType.valueOf(type);
        } catch (IllegalArgumentException e) {
            //ignore
        }

        if ("local".equals(name)) {
            return translateHost(InetAddress.getLocalHost(), hostTranslationType);
        }

        Enumeration<NetworkInterface> niEnum = null;

        try {
            niEnum = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RuntimeException("Failed to get network interfaces", e);
        }
        while (niEnum.hasMoreElements()) {
            NetworkInterface networkInterface = niEnum.nextElement();
            if (name.equals(networkInterface.getName()) || name.equals(networkInterface.getDisplayName())) {
                Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
                while (inetAddressEnumeration.hasMoreElements()) {
                    InetAddress address = inetAddressEnumeration.nextElement();
                    if (!(address.getHostAddress().equals("127.0.0.1")) &&
                            (!(address instanceof Inet6Address) || ((hostTranslationType == HostTranslationType.ip6) && (!address.isLoopbackAddress()))) &&
                            (!(address instanceof Inet4Address) || (hostTranslationType != HostTranslationType.ip6))) {
                        return translateHost(address, hostTranslationType);
                    }
                }
            }
        }
        throw new RuntimeException("Failed to find network interface for [" + value + "]");
    }

    private static String translateHost(InetAddress address, HostTranslationType hostTranslationType) {
        if (null == hostTranslationType) {
            return BootIOUtils.wrapIpv6HostAddressIfNeeded(address);
        } else {
            switch (hostTranslationType) {
                case ip:
                    return address.getCanonicalHostName();
                case ip6:
                    return address.getHostName();
                case host:
                case canonicalhost:
                    return BootIOUtils.wrapIpv6HostAddressIfNeeded(address);
                default:
                    throw new IllegalStateException("Unsupported host type: " + hostTranslationType);
            }
        }
    }

    private enum HostTranslationType {
        ip, ip6, host, canonicalhost;
    }


}
