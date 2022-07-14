package com.YouRL.shortenurl;

import org.springframework.stereotype.Component;

@Component
public class UrlShortener {
    private static final int DEFAULT_LENGTH = 6;
    public String generate() {
        return RandomGenerator.generate(DEFAULT_LENGTH);
    }

    private String generate(int length) {
        return RandomGenerator.generate(length);
    }
}
