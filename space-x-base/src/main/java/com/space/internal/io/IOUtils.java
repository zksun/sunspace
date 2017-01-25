package com.space.internal.io;

import com.space.internal.serialization.IClassSerializer;
import com.sun.corba.se.impl.encoding.MarshalOutputStream;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by zksun on 24/01/2017.
 */
public class IOUtils {

    private static ByteArrayOutputStream outputStream = new ByteArrayOutputStream('\uffff');
    private static final Map<Class<?>, IClassSerializer<?>> _typeCache = new HashMap<Class<?>, IClassSerializer<?>>();
    private static final Map<Byte, IClassSerializer<?>> _codeCache = new HashMap<Byte, IClassSerializer<?>>();
    private static final TObjectIntHashMap<Class<?>> _classToCode = new TObjectIntHashMap<Class<?>>();
    private static final TIntObjectHashMap<Class<?>> _codeToClass = new TIntObjectHashMap<Class<?>>();
    private static final IClassSerializer<?> _defaultSerializer = null;
    private static int _swapExtenKey = 0;

    private IOUtils() {
    }

    private static void register(Class<?> type, IClassSerializer<?> serializer) {
        _typeCache.put(type, serializer);
        _codeCache.put(Byte.valueOf(serializer.getCode()), serializer);
    }

    public static Object objectFromByteBuffer(byte[] buffer) throws Exception {
        if (null == buffer) {
            return null;
        } else {
            ObjectInputStream in = null;
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
                new ObjectInputStream(inputStream);
                Object retval = in.readObject();
                return retval;
            } finally {
                if (null != in) {
                    in.close();
                }
            }
        }
    }

    public static byte[] objectToByteBuffer(Object object) throws Exception {
        synchronized (outputStream) {
            ObjectOutputStream out = null;
            try {
                outputStream.reset();
                out = new ObjectOutputStream(outputStream);
                out.writeObject(object);
                out.flush();
                return outputStream.toByteArray();
            } finally {
                if (null != out) {
                    out.close();
                }
            }

        }
    }

    public static Object deepClone(Object object) {
        try {
            byte[] bytes = objectToByteBuffer(object);
            return objectFromByteBuffer(bytes);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to perform deep clone on [" + object + "] object. Check that the all object context are implements java.io.Serializable ", e);
        }
    }

    public static void writeUUID(ObjectOutput output, UUID value) throws IOException {
        output.writeLong(value.getLeastSignificantBits());
        output.writeLong(value.getMostSignificantBits());
    }

    public static UUID readUUID(ObjectInput input) throws IOException {
        long least = input.readLong();
        long most = input.readLong();
        return new UUID(most, least);
    }

    public static void marshalValue(Class type, Object value, ObjectOutput output) throws IOException {
        if (type.isPrimitive()) {
            if (type == Integer.TYPE) {
                output.writeInt(((Integer) value).intValue());
            } else if (type == Boolean.TYPE) {
                output.writeBoolean(((Boolean) value).booleanValue());
            } else if (type == Long.TYPE) {
                output.writeLong(((Long) value).longValue());
            } else if (type == Short.TYPE) {
                output.writeShort(((Short) value).shortValue());
            } else if (type == Float.TYPE) {
                output.writeFloat(((Float) value).floatValue());
            } else if (type == Double.TYPE) {
                output.writeDouble(((Double) value).doubleValue());
            } else if (type == Character.TYPE) {
                output.writeChar(((Character) value).charValue());
            } else {
                if (type != Byte.TYPE) {
                    throw new AssertionError("Unrecognized primitive type: " + type);
                }
                output.writeByte(((Byte) value).byteValue());
            }
        } else {
            output.writeObject(value);
        }
    }

    public static Object unmarshalValue(Class type, ObjectInput input) throws IOException, ClassNotFoundException {
        if (type.isPrimitive()) {
            if (type == Integer.TYPE) {
                return Integer.valueOf(input.readInt());
            } else if (type == Boolean.TYPE) {
                return Boolean.valueOf(input.readBoolean());
            } else if (type == Long.TYPE) {
                return Long.valueOf(input.readLong());
            } else if (type == Short.TYPE) {
                return Short.valueOf(input.readShort());
            } else if (type == Float.TYPE) {
                return Float.valueOf(input.readFloat());
            } else if (type == Double.TYPE) {
                return Double.valueOf(input.readDouble());
            } else if (type == Character.TYPE) {
                return Character.valueOf(input.readChar());
            } else if (type == Byte.TYPE) {
                return Byte.valueOf(input.readByte());
            } else {
                throw new AssertionError("Unrecognized primitive type: " + type);
            }
        } else {
            return input.readObject();
        }
    }

    public static boolean isPortBusy(int port, String bindAddr) throws UnknownHostException {
        InetAddress inetBindAddr = null != bindAddr ? InetAddress.getByName(bindAddr) : null;

        try {
            (new ServerSocket(port, 0, inetBindAddr)).close();
            return false;
        } catch (IOException e) {
            return true;
        }

    }

    public static int getAnonymousPort() throws IOException {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(0);
            int port = socket.getLocalPort();
            return port;
        } finally {
            if (null != socket) {
                socket.close();
            }
        }


    }

    public static void writeShortArray(ObjectOutput output, short[] array) throws IOException {
        if (null == array) {
            output.writeInt(-1);
        } else {
            int length = array.length;
            output.writeInt(length);

            for (int i = 0; i < length; i++) {
                output.writeShort(array[i]);
            }
        }
    }

    public static short[] readShortArray(ObjectInput input) throws IOException {
        short[] array = null;
        int length = input.readInt();
        if (length >= 0) {
            array = new short[length];

            for (int i = 0; i < length; i++) {
                array[i] = input.readShort();
            }
        }
        return array;
    }

    public static void writeIntegerArray(ObjectOutput output, int[] array) throws IOException {
        if (null == array) {
            output.writeInt(-1);
        } else {
            int length = array.length;
            output.writeInt(length);

            for (int i = 0; i < length; i++) {
                output.writeInt(array[i]);
            }
        }
    }

    public static int[] readIntegerArray(ObjectInput input) throws IOException {
        int[] array = null;
        int length = input.readInt();
        if (length >= 0) {
            array = new int[length];

            for (int i = 0; i < length; i++) {
                array[i] = input.readInt();
            }
        }
        return array;
    }

    public static void writeLongArray(ObjectOutput output, long[] array) throws IOException {
        if (null == array) {
            output.writeInt(-1);
        } else {
            int length = array.length;
            output.writeInt(length);

            for (int i = 0; i < length; i++) {
                output.writeLong(array[i]);
            }
        }
    }

    public static long[] readLongArray(ObjectInput input) throws IOException {
        long[] array = null;
        int length = input.readInt();
        if (length >= 0) {
            array = new long[length];

            for (int i = 0; i < length; i++) {
                array[i] = input.readLong();
            }
        }
        return array;
    }

    public static void writeByteArray(ObjectOutput output, byte[] array) throws IOException {
        if (null == array) {
            output.writeInt(-1);
        } else {
            output.writeInt(array.length);
            output.write(array);
        }
    }

    public static byte[] readByteArray(ObjectInput input) throws IOException {
        int length = input.readInt();
        if (length == -1) {
            return null;
        } else {
            byte[] array = new byte[length];
            input.readFully(array);
            return array;
        }
    }

    public static void writeBooleanArray(ObjectOutput output, boolean[] array) throws IOException {
        if (null == array) {
            output.writeInt(-1);
        } else {
            int length = array.length;
            output.writeInt(length);

            for (int i = 0; i < length; i++) {
                output.writeBoolean(array[i]);
            }
        }
    }

    public static boolean[] readBooleanArray(ObjectInput input) throws IOException {
        boolean[] array = null;
        int length = input.readInt();
        if (length >= 0) {
            array = new boolean[length];

            for (int i = 0; i < length; i++) {
                array[i] = input.readBoolean();
            }
        }
        return array;
    }

    public static void writeRepetitiveString(ObjectOutput output, String s) {
        if (output instanceof MarshalOutputStream) {

        }
    }

    public static void writeString(ObjectOutput output, String s) throws IOException {
        BootIOUtils.writeString(output, s);
    }

    public static String readString(ObjectInput input) throws IOException, ClassNotFoundException {
        return BootIOUtils.readString(input);
    }

    public static void writeStringArray(ObjectOutput output, String[] array) throws IOException {
        BootIOUtils.writeStringArray(output, array);
    }

    public static String[] readStringArray(ObjectInput input) throws IOException, ClassNotFoundException {
        String[] array = null;
        int length = input.readInt();
        if (length >= 0) {
            array = new String[length];

            for (int i = 0; i < length; i++) {
                array[i] = readString(input);
            }
        }
        return array;
    }

    public static void writeStringSet(ObjectOutput output, Set<String> set) throws IOException {
        if (null == set) {
            output.writeInt(-1);
        } else {
            int length = set.size();
            output.writeInt(length);
            Iterator<String> iterator = set.iterator();

            while (iterator.hasNext()) {
                String string = iterator.next();
                writeString(output, string);
            }
        }
    }

    public static Set<String> readStringSet(ObjectInput input) throws IOException, ClassNotFoundException {
        HashSet<String> set = null;
        int length = input.readInt();
        if (length >= 0) {
            set = new HashSet<String>();

            for (int i = 0; i < length; i++) {
                set.add(readString(input));
            }
        }
        return set;
    }


}
