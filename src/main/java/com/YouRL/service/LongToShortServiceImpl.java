package com.YouRL.service;

import com.YouRL.entity.LongToSequenceId;
import com.YouRL.entity.LongToShort;
import com.YouRL.repository.LongToSequenceIdRepository;
import com.YouRL.repository.LongToShortRepository;
import com.YouRL.shortenurl.UrlShortener;
import com.YouRL.util.UrlUtils;
import com.YouRL.util.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LongToShortServiceImpl implements LongToShortService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LongToShortServiceImpl.class);
    private final UrlShortener urlShortener;
    private final LongToShortRepository longToShortRepository;
    private final LongToSequenceIdRepository longToSequenceIdRepository;
    private final RedisService redisService;
    private final SequenceIdService sequenceIdService;

    @Autowired
    public LongToShortServiceImpl(UrlShortener urlShortener, LongToShortRepository longToShortRepository, LongToSequenceIdRepository longToSequenceIdRepository, RedisService redisService, SequenceIdService sequenceIdService) {
        this.urlShortener = urlShortener;
        this.longToShortRepository = longToShortRepository;
        this.longToSequenceIdRepository = longToSequenceIdRepository;
        this.redisService = redisService;
        this.sequenceIdService = sequenceIdService;
    }


    @Override
    @Transactional
    public String generateShortenUrl(String longUrl) throws ValidationException {
        if (!UrlUtils.isValidLongUrl(longUrl)) {
            throw new ValidationException(longUrl);
        }
        String shortUrl = fetchShortUrlFromCache(longUrl);
        if (shortUrl != null) {
            LOGGER.info("get from cache");
            return shortUrl;
        }
        LongToShort LTS = longToShortRepository.getByLongUrl(longUrl);
        if (LTS != null) {
            shortUrl = LTS.getShortUrl();
            LOGGER.info("get from DB");
            redisService.setLTSandSTL(longUrl, shortUrl, 20);
            LOGGER.info(("DB: save to cache"));
            return shortUrl;
        }
        long sequence_id = sequenceIdService.getNextSequenceIdByAtomic();
        shortUrl = urlShortener.generate(sequence_id);

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
        longToShortRepository.save(longToShort);
    }

    @Override
    public String getLongUrlByShortUrl(String shortUrl) {
        if (longToShortRepository.getByShortUrl(shortUrl) != null) {
            return longToShortRepository.getByShortUrl(shortUrl).getLongUrl();
        }
        return null;
    }

    private long generateSequenceId(String longUrl) {
        LongToSequenceId lastRecord = longToSequenceIdRepository.findTopByOrderByIdDesc();
        long sequence_id = 0;
        if (lastRecord != null) {
            sequence_id = lastRecord.getSequenceId() + 1;
        }
        LongToSequenceId longToSequenceId = new LongToSequenceId(sequence_id, longUrl);
        longToSequenceIdRepository.save(longToSequenceId);
        return sequence_id;
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
