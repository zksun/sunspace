package com.space.internal.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zksun on 24/01/2017.
 */
public class BootIOUtils {
    private static final byte STRING_NULL = 0;
    private static final byte STRING_UTF = 1;
    private static final byte STRING_OBJECT = 2;
    private static final int UTF_MAX_LENGTH = 32767;

    public BootIOUtils() {
    }

    public static void writeMapStringString(ObjectOutput output, Map<String, String> map) throws IOException {
        if (null == map) {
            output.writeInt(-1);
        } else {
            int length = map.size();
            output.writeInt(length);
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                writeString(output, entry.getKey());
                writeString(output, entry.getValue());
            }
        }
    }

    public static void writeString(ObjectOutput output, String s) throws IOException {
        if (null == s) {
            output.writeByte(STRING_NULL);
        } else if (s.length() < UTF_MAX_LENGTH) {
            output.writeByte(STRING_UTF);
            output.writeUTF(s);
        } else {
            output.writeByte(STRING_OBJECT);
            output.writeObject(s);
        }
    }

    public static Map<String, String> readMapStringString(ObjectInput input) throws IOException, ClassNotFoundException {
        HashMap<String, String> map = null;
        int length = input.readInt();
        if (length >= 0) {
            map = new HashMap<String, String>(length);

            for (int i = 0; i < length; i++) {
                String key = readString(input);
                String value = readString(input);
                map.put(key, value);
            }
        }
        return map;
    }

    public static String readString(ObjectInput input) throws IOException, ClassNotFoundException {
        byte code = input.readByte();
        switch (code) {
            case STRING_NULL:
                return null;
            case STRING_UTF:
                return input.readUTF();
            case STRING_OBJECT:
                return (String) input.readObject();
            default:
                throw new IllegalStateException("Failed to deserialize a string: unrecognized string type code - " + code);
        }
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

    public static void writeStringArray(ObjectOutput output, String[] array) throws IOException {
        if (null == array) {
            output.writeInt(-1);
        } else {
            int length = array.length;
            output.writeInt(length);

            for (int i = 0; i < length; i++) {
                writeString(output, array[i]);
            }
        }
    }

    public static File[] listFiles(File dir) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir.getPath() + " is not a directory");
        } else if (!dir.canRead()) {
            throw new IllegalArgumentException("No read permissions for " + dir.getPath());
        } else {
            File[] files = dir.listFiles();
            if (null == files) {
                throw new IllegalArgumentException("An unknown i/o error occurred when scanning file in directory " + dir.getPath());
            } else {
                return files;
            }
        }
    }

    public static File[] listFiles(File dir, FileFilter fileFilter) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir.getPath() + " is not a directory");
        } else if (!dir.canRead()) {
            throw new IllegalArgumentException("No read permissions for " + dir.getPath());
        } else {
            File[] files = dir.listFiles(fileFilter);
            if (null == files) {
                throw new IllegalArgumentException("An unknown i/o error occurred when scanning file in directory " + dir.getPath());
            } else {
                return files;
            }
        }
    }

    public static File[] listFiles(File dir, FilenameFilter filter) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir.getPath() + " is not a directory");
        } else if (!dir.canRead()) {
            throw new IllegalArgumentException("No read permissions for " + dir.getPath());
        } else {
            File[] files = dir.listFiles(filter);
            if (null == files) {
                throw new IllegalArgumentException("An unknown i/o error occurred when scanning files in directory " + dir.getPath());
            } else {
                return files;
            }
        }
    }

    public static String wrapIpv6HostAddressIfNeeded(InetAddress hostAddress) {
        if (hostAddress instanceof Inet6Address) {
            return "[" + hostAddress.getHostAddress() + "]";
        } else {
            return hostAddress.getHostAddress();
        }
    }

}
