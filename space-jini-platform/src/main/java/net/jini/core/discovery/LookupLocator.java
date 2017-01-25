package net.jini.core.discovery;

import net.jini.core.lookup.ServiceRegistrar;
import net.jini.discovery.Constants;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.rmi.MarshalledObject;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Created by zksun on 25/01/2017.
 */
public class LookupLocator implements Serializable {
    private static final int discorycoveryProt = Constants.getDiscoveryPort();
    private static final int protoVersion = 1;

    protected String host;
    protected int port;
    static final int defaultTimeout = ((Integer) AccessController.doPrivileged(new PrivilegedAction() {
        public Object run() {
            Integer timeout = new Integer(60 * 1000);

            try {
                Integer val = Integer.getInteger("net.jini.discovery.timeout", timeout);
                return (val.intValue() < 0 ? timeout : val);
            } catch (SecurityException e) {
                return timeout;
            }
        }
    })).intValue();

    public LookupLocator(String url) throws MalformedURLException {
        if (null == url) {
            throw new NullPointerException("url is null");
        }
        URI uri = null;

        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            MalformedURLException malformedURLException = new MalformedURLException("URI parsing failure: " + url);
            malformedURLException.initCause(e);
            throw malformedURLException;
        }

        if (!uri.isAbsolute()) {
            throw new MalformedURLException("no scheme specified: " + url);
        }

        if (uri.isOpaque()) {
            throw new MalformedURLException("not a hierarchical url: " + url);

        }

        if (!uri.getScheme().toLowerCase().equalsIgnoreCase("jini")) {
            throw new MalformedURLException("Invalid URL scheme: " + url);
        }

        String uriPath = uri.getPath();
        if ((uriPath.length() != 0) && (!uriPath.equals("/"))) {
            throw new MalformedURLException("URL path contains path segments: " + url);
        }
        if (null != uri.getQuery()) {
            throw new MalformedURLException("invalid character, '?' , in URL: " + url);
        }
        if (null != uri.getFragment()) {
            throw new MalformedURLException("invalid character, '#' , in URL: " + url);
        }

        try {
            uri.parseServerAuthority();
            if (null != uri.getUserInfo()) {
                throw new MalformedURLException("invalid character,'@',in URL host: " + url);
            }
            if (null == (host = uri.getHost())) {
                throw new MalformedURLException("Not a hierarchical URL: " + url);
            }
            port = uri.getPort();
            if (port == -1) {
                port = discorycoveryProt;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if ((port <= 0) || port >= 65535) {
            throw new MalformedURLException("port number out of range: " + url);
        }
    }

    public LookupLocator(String host, int port) {
        if (null == host) {
            throw new NullPointerException("null host");
        }
        if (port <= 0 || port >= 65535) {
            throw new IllegalArgumentException("port number out of range");
        }

        URI uri;

        try {
            uri = new URI(null, null, host, port, null, null, null);
            if (null != uri.getUserInfo()) {
                throw new IllegalArgumentException("invalid character, '@', in host: " + host);
            }
            this.host = host;
            this.port = port;
        } catch (URISyntaxException e) {
            uri = try3986Authority(host, port);
            assert ((this.port > 0) && (this.port < 65535));
        }
        if (uri.getPath().length() != 0) {
            throw new IllegalArgumentException("invalid character, '/', in host: " + host);
        }
        if (null != uri.getQuery()) {
            throw new IllegalArgumentException("invalid character, '?', in host: " + host);
        }
        if (null != uri.getFragment()) {
            throw new IllegalArgumentException("invalid character, '#', in host: " + host);
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public ServiceRegistrar getRegistrar()
            throws IOException, ClassNotFoundException {
        return getRegistrar(defaultTimeout);
    }

    public ServiceRegistrar getRegistrar(int timeout)
            throws IOException, ClassNotFoundException {
        InetAddress[] addresses = null;

        try {
            addresses = InetAddress.getAllByName(host);
        } catch (UnknownHostException e) {
            Socket socket = new Socket(this.host, this.port);
            return getRegistrarFromSocket(socket, timeout);
        }
        IOException ioEx = null;
        SecurityException secEx = null;
        ClassNotFoundException cnfEx = null;

        for (int i = 0; i < addresses.length; i++) {
            try {
                Socket socket = new Socket(addresses[i], this.port);
                return getRegistrarFromSocket(socket, timeout);
            } catch (IOException e) {
                ioEx = e;
            } catch (ClassNotFoundException e) {
                cnfEx = e;
            } catch (SecurityException e) {
                secEx = e;
            }
        }

        if (null != cnfEx) {
            throw cnfEx;
        }
        if (null != ioEx) {
            throw ioEx;
        }
        assert (null != secEx);
        throw secEx;
    }

    private static ServiceRegistrar getRegistrarFromSocket(Socket sock, int timeout) throws IOException, ClassNotFoundException {

        try {
            sock.setSoTimeout(timeout);
            try {
                sock.setTcpNoDelay(true);
            } catch (SocketException e) {
                //ignore
            }
            try {
                sock.setKeepAlive(true);
            } catch (SocketException e) {
                //ignore
            }
            DataOutputStream dataOutputStream = new DataOutputStream(sock.getOutputStream());
            dataOutputStream.writeInt(protoVersion);
            dataOutputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(sock.getInputStream());
            ServiceRegistrar serviceRegistrar = (ServiceRegistrar) ((MarshalledObject) inputStream.readObject()).get();
            for (int grpCount = inputStream.readInt(); --grpCount >= 0; ) {
                inputStream.readUTF();
            }
            return serviceRegistrar;
        } finally {
            try {
                if (null != sock) {
                    sock.close();
                }
            } catch (IOException e) {
                //ignore
            }
        }
    }

    private URI try3986Authority(String host, int port) {

        try {
            URI uri = new URI("jini://" + host + ":" + port);
            handle3986Authority(uri);
            return uri;
        } catch (URISyntaxException e) {
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("syntax error in host: " + host);
            illegalArgumentException.initCause(e);
            throw illegalArgumentException;
        } catch (MalformedURLException e) {
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("syntax error in host: " + host);
            illegalArgumentException.initCause(e);
            throw illegalArgumentException;
        }
    }

    private void handle3986Authority(URI uri) throws MalformedURLException {
        assert (!uri.isOpaque());
        String authority;
        if (null == (authority = uri.getAuthority())) {
            throw new MalformedURLException("Missing authority: " + uri);
        }
        if (authority.indexOf('@') != -1) {
            throw new MalformedURLException("ivalid character, '@', in host: " + uri);
        }
        parseHostPort(authority, uri);
    }

    private void parseHostPort(String authority, URI uri) throws MalformedURLException {
        int index = authority.lastIndexOf(':');
        if (index == -1) {
            port = discorycoveryProt;
            host = authority;
            return;
        }
        if (authority.indexOf(':') != index) {
            throw new MalformedURLException(": not allowed in host name: " + uri);
        }
        String portString = authority.substring(index + 1);
        int portInt;
        if (portString.length() == 0) {
            throw new MalformedURLException("invalid port in authority: " + uri);
        } else {
            try {
                portInt = Integer.parseInt(portString);
            } catch (NumberFormatException e) {
                MalformedURLException malformedURLException = new MalformedURLException("invalid port in authority: " + uri);
                malformedURLException.initCause(e);
                throw malformedURLException;
            }
        }
        port = portInt;
        host = authority.substring(0, index);
        if (host.length() == 0) {
            throw new MalformedURLException("zero length host name: " + uri);
        }
    }

}
