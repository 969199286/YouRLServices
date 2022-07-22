package com.YouRL.shortenurl;

import org.springframework.stereotype.Component;

@Component
public class UrlShortener {
    private static final int DEFAULT_LENGTH = 6;
    public String generate() {
        return RandomGenerator.generate(DEFAULT_LENGTH);
    }

    private String generate(int id) {
        return Base62Generator.generate(id, DEFAULT_LENGTH);
    }

    public long convertToId(String url){
        return Base62Generator.generateId(url);
    }

}
