package com.YouRL.service;

import com.YouRL.entity.LongToShort;
import com.YouRL.repository.LongToShortRepository;
import com.YouRL.shortenurl.UrlShortener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LongToShortServiceImpl implements LongToShortService {

    private UrlShortener urlShortener;
    private LongToShortRepository longToShortRepository;

    @Autowired
    public LongToShortServiceImpl(UrlShortener urlShortener, LongToShortRepository longToShortRepository) {
        this.urlShortener = urlShortener;
        this.longToShortRepository = longToShortRepository;
    }

    @Override
    public String generateShortenUrl() {
        return urlShortener.generate();
    }

    @Override
    public LongToShort getShortenUrlByLongUrl(String longUrl) {
        return longToShortRepository.getByLongUrl(longUrl);
    }

    @Override
    @Transactional
    public void saveUrl(LongToShort url) {
        longToShortRepository.save(url);
    }
}
