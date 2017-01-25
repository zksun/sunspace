package net.jini.core.lease;

import java.rmi.RemoteException;

/**
 * Created by zksun on 25/01/2017.
 */
public interface Lease {
    long FOREVER = Long.MAX_VALUE;
    long ANY = -1L;
    int DURATION = 1;
    int ABSOLUTE = 2;

    long getExpiration();
    void cancel() throws UnknownLeaseException, RemoteException;
    void renew(long duration) throws LeaseDeniedException, UnknownLeaseException, RemoteException;
    void setSerialFormat(int expiration);
    int getSerialFormat();
}
