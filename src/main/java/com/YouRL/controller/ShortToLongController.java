package com.YouRL.controller;

import com.YouRL.service.ShortToLongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/yourl")
public class ShortToLongController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShortToLongController.class);

    private ShortToLongService shortToLongService;
    @Autowired
    public ShortToLongController(ShortToLongService shortToLongService) {
        this.shortToLongService = shortToLongService;
    }
    @GetMapping("/{shortUrl}")
    public void redirect(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        String longUrl = shortToLongService.getLongUrlByShortUrl(shortUrl);
        if (longUrl == null) {
            throw new NoSuchElementException("Cannot find long URL mapping to " + shortUrl);
        } else {
            response.sendRedirect(longUrl);

        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public void handleException(NoSuchElementException ex)
    {
        LOGGER.error(ex.getMessage());
    }
}
