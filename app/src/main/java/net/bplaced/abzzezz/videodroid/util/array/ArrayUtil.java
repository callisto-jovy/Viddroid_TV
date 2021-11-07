package net.bplaced.abzzezz.videodroid.util.array;

import java.util.HashMap;
import java.util.Map;

public class ArrayUtil {

    public static Map<String, String> stringArrayToMap(final String[]... requestProperties) {
        final Map<String, String> map = new HashMap<>();
        for (final String[] requestProperty : requestProperties) {
            map.put(requestProperty[0], requestProperty[1]);
        }
        return map;
    }
}
