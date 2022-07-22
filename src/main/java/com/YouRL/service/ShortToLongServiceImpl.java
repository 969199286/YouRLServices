package com.YouRL.service;

import com.YouRL.repository.ShortToLongRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShortToLongServiceImpl implements ShortToLongService{

    private ShortToLongRepository shortToLongRepository;

    @Autowired
    public ShortToLongServiceImpl(ShortToLongRepository shortToLongRepository) {
        this.shortToLongRepository = shortToLongRepository;
    }

    @Override
    public String getLongUrlByShortUrl(String shortUrl) {
        if (shortToLongRepository.getByShortUrl(shortUrl) != null) {
            return shortToLongRepository.getByShortUrl(shortUrl).getLongUrl();
        }
        return null;
    }
}
