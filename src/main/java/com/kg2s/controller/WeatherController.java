package com.kg2s.controller;

import com.kg2s.domain.WeatherInfoResp;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yanfu Zhang
 * @date 2025-08-08
 * @description
 */
@RestController
@RequestMapping("/weather")
public class WeatherController {


    /**
     *
     * @param city
     * @param country
     * @return
     *
     * desc that is for standard Restful APIs - path vars
     */
    @GetMapping("/query/{city}/{country}")
    @ResponseBody
    public WeatherInfoResp queryWeather(@PathVariable("city") String city, @PathVariable("country") String country){
        System.out.println("WeatherInfo..");
        return new WeatherInfoResp();
    }
    /**
     *
     * @param city
     * @param country
     * @return
     *
     * desc that is for request params
     */
    @GetMapping("/query")
    @ResponseBody
    public WeatherInfoResp selectWeather(@RequestParam("city") String city, @RequestParam("country") String country){
        System.out.println("selectWeather..");
        return new WeatherInfoResp();
    }

}
