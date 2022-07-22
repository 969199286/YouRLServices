package com.YouRL.service;

import com.YouRL.entity.LongToShort;

public interface LongToShortService {
    public String generateShortenUrl(String longUrl);
    public LongToShort getShortenUrlByLongUrl(String longUrl);
    public void saveUrl(String longUrl, String shortUrl);

}
