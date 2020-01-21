package com.pluralsight.conferencedemo.controllers;

import com.pluralsight.conferencedemo.models.Speaker;
import com.pluralsight.conferencedemo.repositories.SpeakerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // this will respond to payloads incoming and outgoing as JSON rest endpoints
@RequestMapping("/api/v1/speakers") // tells the router what the mapping URL will look like. Route path - all requests to that URL will be sent to this controller

public class SpeakersController {

    @Autowired // inject speakers repository. Gives CRUD access to database
    private SpeakerRepository speakerRepository;

    //Create a list endpoint. Will return all of the speakers when called
    @GetMapping // specific get method. tells which http verb to use to call endpoint.
    public List<Speaker> list() {
        return speakerRepository.findAll();
    }

    @GetMapping // using http verb GET
    @RequestMapping("{id}") // get a specific speaker by ID. In addition to class.
    public Speaker get(@PathVariable Long id) {
        return speakerRepository.getOne(id);
    }

    @PostMapping // requiring http verb "post" to be presented with this API call
    //@ResponseStatus(HttpStatus.CREATED)
    public Speaker create(@RequestBody final Speaker speaker) {
        return speakerRepository.saveAndFlush(speaker); // doesn't commit to database until you flush it.
    }

    @RequestMapping(value = "{id}",  method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        //Also need to check for children records before deleting
        speakerRepository.deleteById(id); // will only delete records without children
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT) // changing method to http verb PUT. PUT replaces all attributes, PATCH allows only a portion to be updated
    public Speaker update(@PathVariable Long id, @RequestBody Speaker speaker) {
        //because this is a PUT, we expect all attributes to be passed in. A PATCH would only need what is being updated?
        //TODO: Add validation that all attributes are passed in, otherwise return a 400 bad payload
        Speaker existingSpeaker = speakerRepository.getOne(id);
        BeanUtils.copyProperties(speaker, existingSpeaker, "speaker_id"); //BeanUtils takes existing speaker and copies incoming speaker data onto it. Ignore speaker_id attribute because it's the primary key.
        return speakerRepository.saveAndFlush(existingSpeaker);
    }


}
