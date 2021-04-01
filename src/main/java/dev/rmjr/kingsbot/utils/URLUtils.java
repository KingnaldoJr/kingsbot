package dev.rmjr.kingsbot.utils;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class URLUtils {
    private URLUtils() {}

    public static boolean isValidUrl(String url) {
        try {
            new URL(url).toURI();
            return true;
        }catch(URISyntaxException | MalformedURLException e) {
            return false;
        }
    }
}
