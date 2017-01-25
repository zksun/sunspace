package net.jini.core.lookup;

import net.jini.core.discovery.LookupLocator;
import net.jini.core.event.EventRegistration;
import net.jini.core.event.RemoteEventListener;

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;

/**
 * Created by zksun on 25/01/2017.
 */
public interface ServiceRegistrar {
    ServiceRegistration register(ServiceItem item, long leaseDuration)
            throws RemoteException;

    Object lookup(ServiceTemplate tmpl) throws RemoteException;

    int TRANSITION_MATCH_NOMATCH = 1 << 0;

    int TRANSITION_NOMATCH_MATCH = 1 << 1;

    int TRANSITION_MATCH_MATCH = 1 << 2;

    EventRegistration notify(ServiceTemplate tmpl,
                             int transitions,
                             RemoteEventListener listener,
                             MarshalledObject handback,
                             long leaseDuration)
            throws RemoteException;

    Class[] getEntryClasses(ServiceTemplate tmpl) throws RemoteException;

    Object[] getFieldValues(ServiceTemplate tmpl, int setIndex, String field)
            throws NoSuchFieldException, RemoteException;

    Class[] getServiceTypes(ServiceTemplate tmpl, String prefix)
            throws RemoteException;

    ServiceID getServiceID();

    LookupLocator getLocator() throws RemoteException;

    String[] getGroups() throws RemoteException;

}
