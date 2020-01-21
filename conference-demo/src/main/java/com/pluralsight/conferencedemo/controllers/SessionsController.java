package com.pluralsight.conferencedemo.controllers;

import com.pluralsight.conferencedemo.models.Session;
import com.pluralsight.conferencedemo.repositories.SessionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // this will respond to payloads incoming and outgoing as JSON rest endpoints
@RequestMapping("/api/v1/sessions") // tells the router what the mapping URL will look like. Route path - all requests to that URL will be sent to this controller

public class SessionsController {

    @Autowired // inject sessions repository
    private SessionRepository sessionRepository; // spring will autowire. When the sessioncontroller is built, it will create a instance of session repository and put it onto ourr class

    //Create a list endpoint. Will return all of the sessions when called
    @GetMapping // specific get method. tells which http verb to use to call endpoint.
    public List<Session> list() {
        return sessionRepository.findAll(); // will query all "sessions" in db and return as a list of Session objects
    }

    @GetMapping // using http verb GET
    @RequestMapping("{id}") // get a specific session by ID. In addition to class.
    public Session get(@PathVariable Long id) {
        return sessionRepository.getOne(id);
    }

    @PostMapping // requiring http verb "post" to be presented with this API call
    //@ResponseStatus(HttpStatus.CREATED)
    public Session create(@RequestBody final Session session) {
        return sessionRepository.saveAndFlush(session); // doesn't commit to database until you flush it.
    }

    @RequestMapping(value = "{id}",  method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        //Also need to check for children records before deleting
        sessionRepository.deleteById(id); // will only delete records without children
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT) // changing method to http verb PUT. PUT replaces all attributes, PATCH allows only a portion to be updated
    public Session update(@PathVariable Long id, @RequestBody Session session) {
        //because this is a PUT, we expect all attributes to be passed in. A PATCH would only need what is being updated?
        //TODO: Add validation that all attributes are passed in, otherwise return a 400 bad payload
        Session existingSession = sessionRepository.getOne(id);
        BeanUtils.copyProperties(session, existingSession, "session_id"); //BeanUtils takes existing session and copies incoming session data onto it. Ignore session_id attribute because it's the primary key.
        return sessionRepository.saveAndFlush(existingSession);
    }

}
