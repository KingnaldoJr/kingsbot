package dev.rmjr.kingsbot.utils;

import java.util.regex.Pattern;

public class URLUtils {
    private URLUtils() {}

    private static final String INITIALIZER = "^";
    private static final String PROTOCOL_IDENTIFIER = "(?:(?:(?:https?|ftp):)?\\/\\/)";
    private static final String USER_PASS_BASIC_AUTH = "(?:\\S+(?::\\S*)?@)?";
    private static final String IP_HOST_DOMAIN_INITIALIZER = "(?:";
    private static final String IP_ADDRESS_EXCLUSIONS_PRIVATE_LOCAL_NETWORKS_PART_1 = "(?!(?:10|127)(?:\\.\\d{1,3}){3})" +
            "(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})";
    private static final String IP_ADDRESS_EXCLUSIONS_PRIVATE_LOCAL_NETWORKS_PART_2 = "(?!172\\.(?:1[6-9]|2\\d|3[0-1])" +
            "(?:\\.\\d{1,3}){2})";
    private static final String IP_ADDRESS_EXCLUSIONS_DOTTED_NOTATION_OCTETS_PART_1 = "(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])" +
            "(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}";
    private static final String IP_ADDRESS_EXCLUSIONS_DOTTED_NOTATION_OCTETS_PART_2 = "(?:\\.(?:[1-9]\\d?|1\\d\\d|" +
            "2[0-4]\\d|25[0-4]))";
    private static final String IP_OR_HOSTNAME_SPLIT = "|";
    private static final String HOST_DOMAINS_NAMES = "(?:(?:[a-z0-9\\u00a1-\\uffff][a-z0-9\\u00a1-\\uffff_-]{0,62})?" +
            "[a-z0-9\\u00a1-\\uffff]\\.)+";
    private static final String TLD_IDENTIFIER_NAME = "(?:[a-z\\u00a1-\\uffff]{2,}\\.?)";
    private static final String IP_OR_HOSTNAME_END = ")";
    private static final String PORT_NUMBER = "(?::\\d{2,5})?";
    private static final String RESOURCE_PATH = "(?:[/?#]\\S*)?";
    private static final String FINALIZER = "$";

    private static final Pattern URL_PATTERN = Pattern.compile(INITIALIZER + PROTOCOL_IDENTIFIER +
            USER_PASS_BASIC_AUTH + IP_HOST_DOMAIN_INITIALIZER + IP_ADDRESS_EXCLUSIONS_PRIVATE_LOCAL_NETWORKS_PART_1 +
            IP_ADDRESS_EXCLUSIONS_PRIVATE_LOCAL_NETWORKS_PART_2 + IP_ADDRESS_EXCLUSIONS_DOTTED_NOTATION_OCTETS_PART_1 +
            IP_ADDRESS_EXCLUSIONS_DOTTED_NOTATION_OCTETS_PART_2 + IP_OR_HOSTNAME_SPLIT + HOST_DOMAINS_NAMES +
            TLD_IDENTIFIER_NAME + IP_OR_HOSTNAME_END + PORT_NUMBER + RESOURCE_PATH + FINALIZER);

    public static boolean isValidUrl(String url) {
        return URL_PATTERN.matcher(url).matches();
    }
}
