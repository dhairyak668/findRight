package com.pluralsight.conferencedemo.controllers;

import com.pluralsight.conferencedemo.models.Speakers;
import com.pluralsight.conferencedemo.repositories.SpeakerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/speakers")
public class SpeakerController {
    @Autowired
    private SpeakerRepository speakerRepository;

    @GetMapping
    public List<Speakers> list(){
        return speakerRepository.findAll();
    }

    @GetMapping
    @RequestMapping("/id")
    public Speakers get(@PathVariable Long id){
        return speakerRepository.getOne(id);
    }
}
