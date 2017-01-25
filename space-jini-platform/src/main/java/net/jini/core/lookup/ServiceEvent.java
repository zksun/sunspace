package net.jini.core.lookup;

import net.jini.core.event.RemoteEvent;

import java.rmi.MarshalledObject;

/**
 * Created by zksun on 25/01/2017.
 */
public abstract class ServiceEvent extends RemoteEvent {

    protected ServiceID serviceID;
    protected int transition;

    public ServiceEvent(Object source, long eventID, long seqNum, MarshalledObject handback, ServiceID serviceID, int transition) {
        super(source, eventID, seqNum, handback);
        this.serviceID = serviceID;
        this.transition = transition;
    }

    public ServiceID getServiceID() {
        return serviceID;
    }

    public int getTransition() {
        return transition;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getName()).append("[serviceID=").append(getServiceID()).append(", transition=");

        switch (getTransition()) {
            case ServiceRegistrar.TRANSITION_MATCH_MATCH:
                stringBuilder.append("TRANSITION_MATCH_MATCH");
                break;
            case ServiceRegistrar.TRANSITION_MATCH_NOMATCH:
                stringBuilder.append("TRANSITION_MATCH_NOMATCH");
                break;
            case ServiceRegistrar.TRANSITION_NOMATCH_MATCH:
                stringBuilder.append("TRANSITION_NOMATCH_MATCH");
                break;
            default:
                stringBuilder.append("UNKNOWN_TRANSITION").append(transition);
        }
        return stringBuilder.append("]").toString();
    }

    public abstract ServiceItem getServiceItem();
}
