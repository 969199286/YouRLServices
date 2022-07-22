package com.YouRL.util;

public class UrlUtils {
    public static boolean isValidLongUrl(String longUrl) {
        return !longUrl.startsWith("http://localhost") && (longUrl.startsWith("http://") || longUrl.startsWith("https://")) && longUrl.contains(".com");
    }

    public static boolean isValidShortUrl(String shortUrl) {
        return shortUrl.startsWith("http://localhost:8080");
    }
}
