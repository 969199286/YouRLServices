package com.YouRL.controller;


import com.YouRL.service.LongToShortService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
public class LongToShortController {
    private LongToShortService longToShortService;

    @Autowired
    public LongToShortController(LongToShortService longToShortService) {
        this.longToShortService = longToShortService;
    }

    @GetMapping("/long2short")
    public String getLongToShort(@RequestBody String longUrl) {
        return longToShortService.getShortenUrlByLongUrl(longUrl).toString();
    }

    @PostMapping("/long2short")
    public String addLongToShort(@RequestBody String longUrl) {
        return longToShortService.generateShortenUrl(longUrl);
    }


}
