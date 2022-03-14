package com.pichincha.mock.server.controller;

import com.pichincha.mock.server.service.CountriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/countries")
public class CountriesController {

    @Autowired
    CountriesService countriesService;

    @GetMapping(value = "names")
    public ResponseEntity getCountryNames() {
        return new ResponseEntity<>(countriesService.getCountryNames(), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(value = "phoneCodes")
    public ResponseEntity getPhoneCodes() {
        return new ResponseEntity<>(countriesService.getPhoneCodes(), new HttpHeaders(), HttpStatus.OK);
    }
}
