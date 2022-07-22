package com.YouRL.service;

import com.YouRL.controller.ShortToLongController;
import com.YouRL.entity.LongToShort;
import com.YouRL.entity.ShortToLong;
import com.YouRL.repository.LongToShortRepository;
import com.YouRL.repository.ShortToLongRepository;
import com.YouRL.shortenurl.UrlShortener;
import com.YouRL.util.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LongToShortServiceImpl implements LongToShortService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LongToShortServiceImpl.class);
    private final UrlShortener urlShortener;
    private final LongToShortRepository longToShortRepository;
    private final ShortToLongRepository shortToLongRepository;
    private final RedisService redisService;

    @Autowired
    public LongToShortServiceImpl(UrlShortener urlShortener, LongToShortRepository longToShortRepository, ShortToLongRepository shortToLongRepository, RedisService redisService) {
        this.urlShortener = urlShortener;
        this.longToShortRepository = longToShortRepository;
        this.shortToLongRepository = shortToLongRepository;
        this.redisService = redisService;
    }


    @Override
    @Transactional
    public String generateShortenUrl(String longUrl) {
        if (!UrlUtils.isValidLongUrl(longUrl)) {
            LOGGER.error("Invalid long URL");
            return null;
        }
        String shortUrl = fetchShortUrlFromCache(longUrl);
        if (shortUrl != null) {
            LOGGER.info("get from cache");
            return shortUrl;
        }
        if (validateDB(longUrl)) {
            shortUrl = longToShortRepository.getByLongUrl(longUrl).getShortUrl();
            LOGGER.info("get from DB");
            redisService.setLTSandSTL(longUrl, shortUrl, 20);
            LOGGER.info(("DB: save to cache"));
            return shortUrl;
        }
        shortUrl = urlShortener.generate();

        redisService.setLTSandSTL(longUrl, shortUrl, 20);
        LOGGER.info("save to cache");

        saveUrl(longUrl, shortUrl);
        return shortUrl;
    }

    @Override
    public LongToShort getShortenUrlByLongUrl(String longUrl) {
        return longToShortRepository.getByLongUrl(longUrl);
    }

    @Override
    @Transactional
    public void saveUrl(String longUrl, String shortUrl) {
        LongToShort longToShort = new LongToShort(longUrl, shortUrl);
        ShortToLong shortToLong = new ShortToLong(shortUrl, longUrl);
        longToShortRepository.save(longToShort);
        shortToLongRepository.save(shortToLong);
    }

    @Override
    public Boolean validateDB(String longUrl) {
        return (longToShortRepository.getByLongUrl(longUrl) != null);
    }

    private String fetchShortUrlFromCache(String longUrl) {
        String shortUrl = fetchValueByKey(longUrl);
        return shortUrl;
    }

    private String fetchValueByKey(String key) {
        String value = (String)redisService.get(key);
        redisService.expire(key, 20);
        return value;
    }
}
