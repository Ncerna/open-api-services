package org.adminBo.utils;

public class UserChannelUtil {

    public static String getChannel(String externalReference) {
        if (externalReference == null || externalReference.isBlank())  return null;
        String[] parts = externalReference.split("_");
        if (parts.length < 2)  return null;
        String userId = parts[1];

        return "user_" + userId + "_channel";
    }
}