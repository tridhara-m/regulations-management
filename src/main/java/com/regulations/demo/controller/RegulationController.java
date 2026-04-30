package com.regulations.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.regulations.demo.entity.Regulation;
import com.regulations.demo.repository.RegulationRepository;
import com.regulations.demo.service.EurLexSoapClient;
import com.regulations.demo.service.EurlexService;
import com.regulations.demo.service.SparqlPOC;

@RestController
@RequestMapping("/regulations")
public class RegulationController {

    @Autowired
    private EurlexService service;
    
//    @Autowired
//    private EurLexSoapService eurlexService;
    
    @Autowired
    private EurLexSoapClient eurLexSoapClient;
    
    @Autowired
    private SparqlPOC sparqlpoc;
    
    
    @Autowired
    private RegulationRepository repository;

    @GetMapping("/fetch")
    public String fetch() {
        service.fetchAndSave();
        return "Data fetched and saved!";
    }
    
    @GetMapping("/getAllRegulations")
    public List<Regulation> getAll(){
    	return repository.findAll();
    }
    
//    @GetMapping("/getEurLexRegulations")
//    public String geteurlexRegs(){
//    	return eurlexService.searchByDn();
//    }
    
    @GetMapping("/eurlex")
    public Object test() {
        return eurLexSoapClient.search("DN=3*", 1, 10);
    }
    
    @GetMapping("/fetchonereg")
    public String fetchRegulation() {
        return sparqlpoc.fetchReg();
    }
}
