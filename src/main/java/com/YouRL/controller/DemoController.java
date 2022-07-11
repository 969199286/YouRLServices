package com.YouRL.controller;

import com.YouRL.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api")
public class DemoController {

    private final DemoService demoService;

    @Autowired
    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }
    @GetMapping("/welcome")
    public List<String> welcome() {
        String message = "lalala";
        List<String> list = new ArrayList<String>();
        list.add(message);
        return list;
    }

    @GetMapping("/welcomes")
    public String welcomes() {
        String message = "lalalala";
        List<String> list = new ArrayList<String>();
        list.add(message);
        return message;
    }

    @PostMapping("/long2short")
    public String longToShort(@RequestBody String longUrl) {
        String url = longUrl;
        return demoService.getShortenMessage(url) + " " +  url;
    }
}
