package net.jini.core.lookup;

import net.jini.core.entry.Entry;

import java.io.Serializable;

/**
 * Created by zksun on 24/01/2017.
 */
public class ServiceItem implements Serializable {
    public ServiceID serviceID;
    public Object service;
    public Entry[] attributeSets;

    public ServiceItem(ServiceID serviceID, Object service, Entry[] attrSets) {
        this.serviceID = serviceID;
        this.service = service;
        this.attributeSets = attrSets;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getClass().getName()).append("[serviceID=").append(this.serviceID).append(", service=").append(this.service).append(", attributeSets=");
        if (null != this.attributeSets) {
            stringBuilder.append("[");
            if (this.attributeSets.length > 0) {
                for (int i = 0; i < this.attributeSets.length - 1; i++) {
                    stringBuilder.append(this.attributeSets[i]).append(" ");
                }
                stringBuilder.append(this.attributeSets[this.attributeSets.length - 1]);
            }

            stringBuilder.append("]");
        } else {
            stringBuilder.append((Object) null);
        }
        return stringBuilder.append("]").toString();
    }
}
