package net.jini.core.lookup;

import net.jini.core.entry.Entry;
import net.jini.core.lease.Lease;
import net.jini.core.lease.UnknownLeaseException;

import java.rmi.RemoteException;

/**
 * Created by zksun on 25/01/2017.
 */
public interface ServiceRegistration {
    ServiceID getServiceID();

    Lease getLease();

    void addAttributes(Entry[] attrSets) throws UnknownLeaseException, RemoteException;

    void modifyAttributes(Entry[] attrSetTemplates, Entry[] attrSets) throws UnknownLeaseException, RemoteException;

    void setAttributes(Entry[] attrSets) throws UnknownLeaseException, RemoteException;
}
