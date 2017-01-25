package com.space.internal.utils;

import com.s_space.kernel.SpaceUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Created by zksun on 25/01/2017.
 */
public abstract class StringUtils {
    public static final String FOLDER_SEPARATOR = "/";
    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";
    private static final String TOP_PATH = "..";
    private static final String CURRENT_PATH = ".";
    private static final char EXTENSION_SEPARATOR = '.';
    public static final String NEW_LINE = System.getProperty("line.separator");
    public static String FORMAT_TIME_STAMP = "%1$tF %1$tT.%1$tL";

    public StringUtils() {
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean hasLength(String str) {
        return null != str && str.length() > 0;
    }

    public static boolean hasText(String str) {
        int strLen;
        if (null != str && (strLen = str.length()) != 0) {
            for (int i = 0; i < strLen; i++) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public static String trimWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        } else {
            StringBuilder buf = new StringBuilder(str);

            while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
                buf.deleteCharAt(0);
            }

            while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
                buf.deleteCharAt(buf.length() - 1);
            }

            return buf.toString();
        }
    }

    public static String trimLeadingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        } else {
            StringBuilder buf = new StringBuilder(str);

            while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
                buf.deleteCharAt(0);
            }

            return buf.toString();
        }
    }

    public static String trimTrailingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        } else {
            StringBuilder buf = new StringBuilder(str);

            while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
                buf.deleteCharAt(buf.length() - 1);
            }

            return buf.toString();
        }
    }

    public static boolean equals(String str1, String str2) {
        return null == str1 ? null == str2 : str1.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return null == str1 ? null == str2 : str1.equalsIgnoreCase(str2);
    }

    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return null != str && null != prefix && str.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    public static boolean endsWithIgnoreCase(String str, String suffix) {
        return null != str && null != suffix && str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length());
    }

    public static boolean containsIgnoreCase(String str1, String str2) {
        return indexOfIgnoreCase(str1, str2) != -1;
    }

    public static int indexOfIgnoreCase(String str1, String str2) {
        int length = str2.length();
        int lastPos = str1.length() - length;

        for (int pos = 0; pos <= lastPos; ++pos) {
            if (str1.regionMatches(true, pos, str2, 0, length)) {
                return pos;
            }
        }

        return -1;
    }

    public static int countOccurrencesOf(String s, char c) {
        if (null != s && s.length() != 0) {
            char[] chars = s.toCharArray();
            int count = 0;

            for (int i = 0; i < chars.length; ++i) {
                if (chars[i] == c) {
                    ++count;
                }
            }

            return count;
        } else {
            return 0;
        }
    }

    public static int countOccurrencesOf(String str, String sub) {
        if (null != str && null != sub && str.length() != 0 && sub.length() != 0) {
            int count = 0;

            int idx;
            for (int pos = 0; (idx = str.indexOf(sub, pos)) != -1; pos = idx + sub.length()) {
                ++count;
            }

            return count;
        } else {
            return 0;
        }
    }

    public static String replace(String inString, String oldPattern, String newPattern) {
        if (null == inString) {
            return null;
        } else if (null != oldPattern && null != newPattern) {
            StringBuilder sbuf = new StringBuilder();
            int pos = 0;
            int index = inString.indexOf(oldPattern);

            for (int patLen = oldPattern.length(); index >= 0; index = inString.indexOf(oldPattern, pos)) {
                sbuf.append(inString.substring(pos, index));
                sbuf.append(newPattern);
                pos = index + patLen;
            }

            sbuf.append(inString.substring(pos));
            return sbuf.toString();
        } else {
            return inString;
        }
    }

    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }

    public static String deleteAny(String inString, String charsToDelete) {
        if (null != inString && null != charsToDelete) {
            StringBuilder out = new StringBuilder();

            for (int i = 0; i < inString.length(); ++i) {
                char c = inString.charAt(i);
                if (charsToDelete.indexOf(c) == -1) {
                    out.append(c);
                }
            }

            return out.toString();
        } else {
            return inString;
        }
    }

    public static String quote(String str) {
        return null != str ? "\'" + str + "\'" : null;
    }

    public static Object quoteIfString(Object obj) {
        return obj instanceof String ? quote((String) obj) : obj;
    }

    public static String unqualify(String qualifiedName) {
        return unqualify(qualifiedName, EXTENSION_SEPARATOR);
    }

    public static String unqualify(String qualifiedName, char separator) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }

    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    public static String uncapitalize(String str) {
        return changeFirstCharacterCase(str, false);
    }

    private static String changeFirstCharacterCase(String str, boolean capitalize) {
        if (null != str && str.length() != 0) {
            StringBuilder buf = new StringBuilder(str.length());
            if (capitalize) {
                buf.append(Character.toUpperCase(str.charAt(0)));
            } else {
                buf.append(Character.toLowerCase(str.charAt(0)));
            }

            buf.append(str.substring(1));
            return buf.toString();
        } else {
            return str;
        }
    }

    public static String getFilename(String path) {
        if (null == path) {
            return null;
        } else {
            int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
            return separatorIndex != -1 ? path.substring(separatorIndex + 1) : path;
        }
    }

    public static String getFilenameExtension(String path) {
        if (null == path) {
            return null;
        } else {
            int sepIndex = path.lastIndexOf(46);
            return sepIndex != -1 ? path.substring(sepIndex + 1) : null;
        }
    }

    public static String stripFilenameExtension(String path) {
        if (null == path) {
            return null;
        } else {
            int sepIndex = path.lastIndexOf(46);
            return sepIndex != -1 ? path.substring(0, sepIndex) : path;
        }
    }

    public static String applyRelativePath(String path, String relativePath) {
        int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
                newPath = newPath + FOLDER_SEPARATOR;
            }

            return newPath + relativePath;
        } else {
            return relativePath;
        }
    }

    public static String cleanPath(String path) {
        String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);
        int prefixIndex = pathToUse.indexOf(":");
        String prefix = "";
        if (prefixIndex != -1) {
            prefix = pathToUse.substring(0, prefixIndex + 1);
            pathToUse = pathToUse.substring(prefixIndex + 1);
        }

        String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
        LinkedList pathElements = new LinkedList();
        int tops = 0;

        int i;
        for (i = pathArray.length - 1; i >= 0; --i) {
            if (!CURRENT_PATH.equals(pathArray[i])) {
                if (TOP_PATH.equals(pathArray[i])) {
                    ++tops;
                } else if (tops > 0) {
                    --tops;
                } else {
                    pathElements.add(0, pathArray[i]);
                }
            }
        }

        for (i = 0; i < tops; ++i) {
            pathElements.add(0, TOP_PATH);
        }

        return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
    }

    public static boolean pathEquals(String path1, String path2) {
        return cleanPath(path1).equals(cleanPath(path2));
    }

    public static Locale parseLocaleString(String localeString) {
        String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
        String language = parts.length > 0 ? parts[0] : "";
        String country = parts.length > 1 ? parts[1] : "";
        String variant = parts.length > 2 ? parts[2] : "";
        return language.length() > 0 ? new Locale(language, country, variant) : null;
    }

    public static String[] addStringToArray(String[] array, String str) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[]{str};
        } else {
            String[] newArr = new String[array.length + 1];
            System.arraycopy(array, 0, newArr, 0, array.length);
            newArr[array.length] = str;
            return newArr;
        }
    }

    public static String[] concatenateStringArrays(String[] array1, String[] array2) {
        if (ObjectUtils.isEmpty(array1)) {
            return array2;
        } else if (ObjectUtils.isEmpty(array2)) {
            return array1;
        } else {
            String[] newArr = new String[array1.length + array2.length];
            System.arraycopy(array1, 0, newArr, 0, array1.length);
            System.arraycopy(array2, 0, newArr, array1.length, array2.length);
            return newArr;
        }
    }

    public static String[] mergeStringArrays(String[] array1, String[] array2) {
        if (ObjectUtils.isEmpty(array1)) {
            return array2;
        } else if (ObjectUtils.isEmpty(array2)) {
            return array1;
        } else {
            ArrayList result = new ArrayList();
            result.addAll(Arrays.asList(array1));
            for (int i = 0; i < array2.length; i++) {
                String str = array2[i];
                if (!result.contains(str)) {
                    result.add(str);
                }
            }
            return toStringArray(result);
        }
    }

    public static String[] sortStringArray(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[0];
        } else {
            Arrays.sort(array);
            return array;
        }
    }

    public static String[] toStringArray(Collection<String> collection) {
        return null == collection ? null : collection.toArray(new String[collection.size()]);
    }

    public static String[] removeDuplicateStrings(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return array;
        } else {
            TreeSet set = new TreeSet();
            for (int i = 0; i < array.length; i++) {
                String anArray = array[i];
                set.add(anArray);
            }

            return toStringArray(set);
        }
    }

    public static String join(String[] array, String delimiter, int firstIndex, int count) {
        switch (count) {
            case 0:
                return "";
            case 1:
                return array[firstIndex];
            case 2:
                return array[firstIndex] + delimiter + array[firstIndex + 1];
            case 3:
                return array[firstIndex] + delimiter + array[firstIndex + 1] + delimiter + array[firstIndex + 2];
            case 4:
                return array[firstIndex] + delimiter + array[firstIndex + 1] + delimiter + array[firstIndex + 2] + delimiter + array[firstIndex + 3];
            case 5:
                return array[firstIndex] + delimiter + array[firstIndex + 1] + delimiter + array[firstIndex + 2] + delimiter + array[firstIndex + 3] + delimiter + array[firstIndex + 4];
            default:
                StringBuilder sb = new StringBuilder(array[0]);

                for (int i = firstIndex + 1; i < count; ++i) {
                    sb.append(delimiter);
                    sb.append(array[i]);
                }

                return sb.toString();
        }
    }

    public static String[] split(String toSplit, String delimiter) {
        if (hasLength(toSplit) && hasLength(delimiter)) {
            int offset = toSplit.indexOf(delimiter);
            if (offset < 0) {
                return null;
            } else {
                String beforeDelimiter = toSplit.substring(0, offset);
                String afterDelimiter = toSplit.substring(offset + delimiter.length());
                return new String[]{beforeDelimiter, afterDelimiter};
            }
        } else {
            return null;
        }
    }

    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
        return splitArrayElementsIntoProperties(array, delimiter, (String) null);
    }

    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete) {
        if (null != array && array.length != 0) {
            Properties result = new Properties();

            for (int i = 0; i < array.length; i++) {
                String element = array[i];
                if (charsToDelete != null) {
                    element = deleteAny(element, charsToDelete);
                }

                String[] splittedElement = split(element, delimiter);
                if (splittedElement != null) {
                    result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
                }
            }

            return result;
        } else {
            return null;
        }
    }

    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        StringTokenizer st = new StringTokenizer(str, delimiters);
        ArrayList tokens = new ArrayList();

        while (st.hasMoreElements()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if ((!ignoreEmptyTokens) || (token.length() > 0)) {
                tokens.add(token);
            }
        }

        return toStringArray(tokens);
    }


    public static String[] delimitedListToStringArray(String str, String delimiter) {
        if (null == str) {
            return new String[0];
        } else if (null == delimiter) {
            return new String[]{str};
        } else {
            ArrayList result = new ArrayList();
            int pos;
            if ("".equals(delimiter)) {
                for (pos = 0; pos < str.length(); ++pos) {
                    result.add(str.substring(pos, pos + 1));
                }
            } else {
                int delPos;
                for (pos = 0; (delPos = str.indexOf(delimiter, pos)) != -1; pos = delPos + delimiter.length()) {
                    result.add(str.substring(pos, delPos));
                }

                if (str.length() > 0 && pos <= str.length()) {
                    result.add(str.substring(pos));
                }
            }

            return toStringArray(result);
        }
    }

    public static String[] commaDelimitedListToStringArray(String str) {
        return delimitedListToStringArray(str, ",");
    }

    public static Set<String> commaDelimitedListToSet(String str) {
        TreeSet set = new TreeSet();
        String[] tokens = commaDelimitedListToStringArray(str);

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            set.add(token);
        }

        return set;
    }

    public static String arrayToDelimitedString(Object[] arr, String delim) {
        if (null == arr) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < arr.length; ++i) {
                if (i > 0) {
                    sb.append(delim);
                }
                sb.append(arr[i]);
            }

            return sb.toString();
        }
    }


    public static String collectionToDelimitedString(Collection<String> coll, String delim, String prefix, String suffix) {
        if (null == coll) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            Iterator it = coll.iterator();

            for (int i = 0; it.hasNext(); i++) {
                if (i > 0) {
                    sb.append(delim);
                }

                sb.append(prefix).append((String) it.next()).append(suffix);
            }

            return sb.toString();
        }
    }

    public static String collectionToDelimitedString(Collection<String> coll, String delim) {
        return collectionToDelimitedString(coll, delim, "", "");
    }

    public static String arrayToCommaDelimitedString(Object[] arr) {
        return arrayToDelimitedString(arr, ",");
    }

    public static String collectionToCommaDelimitedString(Collection<String> coll) {
        return collectionToDelimitedString(coll, ",");
    }

    public static Set<String> convertArrayToSet(String[] array) {
        HashSet resultSet = new HashSet(array.length);

        for (int i = 0; i < array.length; i++) {
            String val = array[i];
            resultSet.add(val);
        }

        return resultSet;
    }

    public static Set<String> getParametersSet(String locatorsStr) {
        if (SpaceUtilities.isEmpty(locatorsStr, true)) {
            return new HashSet(1);
        } else {
            String[] locatorsArray = tokenizeToStringArray(locatorsStr, ",");
            return convertArrayToSet(locatorsArray);
        }
    }

    public static String extractTokenFromStart(String s, int tokenNum, char separator) {
        if (null != s && s.length() != 0) {
            int posStart = 0;

            int posEnd;
            for (posEnd = 0; posEnd < tokenNum; posEnd++) {
                posStart = s.indexOf(separator, posStart) + 1;
                if (posStart == 0) {
                    return null;
                }
            }

            posEnd = s.indexOf(separator, posStart);
            if (posEnd == -1) {
                return null;
            } else {
                return s.substring(posStart, posEnd);
            }
        } else {
            return null;
        }
    }

    public static void appendProperties(StringBuilder sb, Properties properties) {
        if (properties == null) {
            sb.append("null");
        } else {
            Enumeration e = properties.propertyNames();

            while (e.hasMoreElements()) {
                String propName = (String) e.nextElement();
                sb.append("\n\t XPath element key: ").append(propName);
                String propValue = properties.getProperty(propName);
                sb.append("\n\t Value: ").append(propValue);
                sb.append(NEW_LINE);
            }
        }
    }

    public static String[] convertKeyValuePairsToArray(Map<String, String> value, String keyValueSeperator) {
        ArrayList keyValuePairs = new ArrayList();
        Iterator iterator = value.keySet().iterator();

        while (iterator.hasNext()) {
            String pairkey = (String) iterator.next();
            String pairvalue = value.get(pairkey);
            if (pairkey.contains(keyValueSeperator)) {
                throw new IllegalArgumentException("Key " + pairkey + " cannot contain separator " + keyValueSeperator);
            }

            keyValuePairs.add(pairkey + keyValueSeperator + pairvalue);
        }

        return (String[]) keyValuePairs.toArray(new String[keyValuePairs.size()]);
    }

    public static Map<String, String> convertArrayToKeyValuePairs(String[] pairs, String keyValueSeperator) {
        Map<String, String> value = new HashMap();

        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            int sepindex = pair.indexOf(keyValueSeperator);
            String pairkey = pair.substring(0, sepindex);
            String pairvalue = pair.substring(sepindex + 1);
            value.put(pairkey, pairvalue);
        }

        return value;
    }

    public static String getTimeStamp() {
        return getTimeStamp(System.currentTimeMillis());
    }

    public static String getTimeStamp(long timeInMillis) {
        return String.format(FORMAT_TIME_STAMP, new Object[]{Long.valueOf(timeInMillis)});
    }

    public static String getSuffix(String s, String separator) {
        if (!hasLength(s)) {
            return s;
        } else {
            int lastIndexOf = s.lastIndexOf(separator);

            if (lastIndexOf == -1) {
                return s;
            } else {
                return s.substring(lastIndexOf + separator.length(), s.length());
            }
        }
    }

    public static String getCurrentStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StringBuilder stackTraceStr = new StringBuilder();

        for (int i = 2; i < stackTrace.length; i++) {
            stackTraceStr.append("\tat " + stackTrace[i].toString());
            stackTraceStr.append(NEW_LINE);
        }

        return stackTraceStr.toString();
    }

    public static boolean isStrictPrefix(String s, String prefix) {
        return s.startsWith(prefix) && s.length() > prefix.length();
    }
}
