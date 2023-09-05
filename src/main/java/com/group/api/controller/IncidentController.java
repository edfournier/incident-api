package com.group.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.group.api.repository.IncidentRepository;
import com.group.api.model.Incident;

@RestController
public class IncidentController {

	@Autowired
	private IncidentRepository repository;
    
	/*
	 * Returns incident corresponding to path parameter date. 
	 */
    @GetMapping("/incidents/{date}")
	public Incident getIncident(@PathVariable String date) {
		return repository.getIncident(date);
	}
}
