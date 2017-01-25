package net.jini.core.lookup;

import net.jini.core.entry.Entry;

import java.io.Serializable;

/**
 * Created by zksun on 25/01/2017.
 */
public class ServiceTemplate implements Serializable {
    public ServiceID serviceID;
    public Class[] serviceTypes;
    public Entry[] attributeSetTemplates;

    public ServiceTemplate(ServiceID serviceID,
                           Class[] serviceTypes,
                           Entry[] attrSetTemplates) {
        this.serviceID = serviceID;
        this.serviceTypes = serviceTypes;
        this.attributeSetTemplates = attrSetTemplates;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getName()).append("[serviceID=").append(serviceID)
                .append(", serviceTypes=");
        if (null != serviceTypes) {
            stringBuilder.append("[");
            if (serviceTypes.length > 0) {
                for (int i = 0; i < serviceTypes.length - 1; i++) {
                    stringBuilder.append(serviceTypes[i]).append(" ");
                }
                stringBuilder.append(serviceTypes[serviceTypes.length - 1]);
            }
            stringBuilder.append("]");
        } else {
            stringBuilder.append((Object) null);
        }

        stringBuilder.append(", attributeSetTemplates=");
        if (null != attributeSetTemplates) {
            stringBuilder.append("[");
            if (attributeSetTemplates.length > 0) {
                for (int i = 0; i < attributeSetTemplates.length - 1; i++) {
                    stringBuilder.append(attributeSetTemplates[i]).append(" ");
                }
                stringBuilder.append(attributeSetTemplates[attributeSetTemplates.length - 1]);
            }
            stringBuilder.append("]");
        } else {
            stringBuilder.append((Object) null);
        }
        return stringBuilder.append("]").toString();
    }
}
