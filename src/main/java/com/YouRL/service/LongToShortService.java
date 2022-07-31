package com.YouRL.service;

import com.YouRL.entity.LongToShort;
import com.YouRL.util.ValidationException;

public interface LongToShortService {
    public String generateShortenUrl(String longUrl) throws ValidationException;
    public LongToShort getShortenUrlByLongUrl(String longUrl);
    public void saveUrl(String longUrl, String shortUrl);
    public String getLongUrlByShortUrl(String shortUrl);
}
