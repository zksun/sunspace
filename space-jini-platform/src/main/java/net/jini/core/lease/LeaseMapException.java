package net.jini.core.lease;

import java.io.InvalidObjectException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by zksun on 25/01/2017.
 */
public class LeaseMapException extends LeaseException {
    public Map exceptionMap;

    public LeaseMapException(String s, Map exceptionMap) {
        super(s);
        final Set mapEntries = exceptionMap.entrySet();
        for (Iterator i = mapEntries.iterator(); i.hasNext(); ) {
            final Entry entry = (Entry) i.next();
            final Object key = entry.getKey();
            final Object value = entry.getValue();

            if (null == key) {
                throw new IllegalArgumentException("exceptionMap contains a null key");
            }

            if (!(key instanceof Lease)) {
                throw new IllegalArgumentException("exceptionMap contains a key which is not a Lease: " + key);
            }

            if (null == value) {
                throw new NullPointerException("exceptionMap contains a null value");
            }

            if (!(value instanceof Throwable)) {
                throw new IllegalArgumentException("exceptionMap contains a value which is not a Throwable:" + value);
            }
        }
        this.exceptionMap = exceptionMap;
    }

    private void readObjectNoData() throws InvalidObjectException {
        throw new InvalidObjectException("LeaseMapException should always have data");
    }
}
