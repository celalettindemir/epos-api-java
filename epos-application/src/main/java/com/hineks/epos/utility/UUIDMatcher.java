package com.hineks.epos.utility;

import java.util.*;
import java.util.regex.Pattern;

public class UUIDMatcher {
    public static boolean IsUUID(String value) {
        Pattern pattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        return pattern.matcher(value).matches();
    }
}
