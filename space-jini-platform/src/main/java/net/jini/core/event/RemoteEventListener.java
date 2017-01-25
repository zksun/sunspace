package net.jini.core.event;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.util.EventListener;

/**
 * Created by zksun on 25/01/2017.
 */
public interface RemoteEventListener extends Remote, EventListener {
    void notify(RemoteEvent theEvent) throws UnexpectedException, RemoteException;
}
