package net.jini.core.event;

import net.jini.core.lease.Lease;

/**
 * Created by zksun on 25/01/2017.
 */
public class EventRegistration {
    protected long eventID;
    protected Object source;
    protected Lease lease;
    protected long seqNum;


    public EventRegistration(long eventID, Object source,
                             Lease lease, long seqNum) {
        this.eventID = eventID;
        this.source = source;
        this.lease = lease;
        this.seqNum = seqNum;
    }

    public long getID() {
        return eventID;
    }

    public Object getSource() {
        return source;
    }

    public Lease getLease() {
        return lease;
    }

    public long getSequenceNumber() {
        return seqNum;
    }
}
