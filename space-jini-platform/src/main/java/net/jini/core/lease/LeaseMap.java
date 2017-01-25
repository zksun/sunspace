package net.jini.core.lease;

import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by zksun on 25/01/2017.
 */
public interface LeaseMap extends Map {
    boolean canContainKey(Object key);

    void renewAll() throws LeaseMapException, RemoteException;

    void cancelAll() throws LeaseMapException, RemoteException;
}
