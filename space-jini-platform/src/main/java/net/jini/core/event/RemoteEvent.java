package net.jini.core.event;

import java.io.IOException;
import java.rmi.MarshalledObject;
import java.util.EventObject;

/**
 * Created by zksun on 25/01/2017.
 */
public class RemoteEvent extends EventObject {
    protected Object source;
    protected long eventID;
    protected long seqNum;
    protected MarshalledObject handback;

    public RemoteEvent(Object source, long eventID, long seqNum,
                       MarshalledObject handback) {
        super(source);
        this.source = source;
        this.eventID = eventID;
        this.seqNum = seqNum;
        this.handback = handback;
    }

    public long getID() {
        return eventID;
    }

    public long getSequenceNumber() {
        return seqNum;
    }

    public MarshalledObject getRegistrationObject() {
        return handback;
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        super.source = source;
    }
}
