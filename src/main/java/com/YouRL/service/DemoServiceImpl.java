package com.YouRL.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DemoServiceImpl implements DemoService{
    static Random random = new Random();
    @Override
    public String getShortenMessage(String longUrl) {
        String str = longUrl;
        str = str.concat(Integer.toString(random.nextInt()));

        return "http://yourl.com/"+str.hashCode();
    }
}
