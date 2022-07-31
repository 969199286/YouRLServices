package com.YouRL.controller;

import com.YouRL.service.LongToShortService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/yourl")
public class ShortToLongController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortToLongController.class);

    private LongToShortService longToShortService;
    @Autowired
    public ShortToLongController(LongToShortService longToShortService) {
        this.longToShortService = longToShortService;
    }
    @GetMapping("/{shortUrl}")
    public void redirect(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        String longUrl = longToShortService.getLongUrlByShortUrl(shortUrl);
        if (longUrl == null) {
            throw new NoSuchElementException(shortUrl);
        } else {
            response.sendRedirect(longUrl);

        }
    }


}
