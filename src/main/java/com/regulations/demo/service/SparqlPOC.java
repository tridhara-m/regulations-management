package com.regulations.demo.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SparqlPOC {
	public String fetchReg() {
	RestTemplate restTemplate = new RestTemplate();
	
	String url = "https://eur-lex.europa.eu/sparql?query=" +
	        URLEncoder.encode("""
	                PREFIX cdm: <http://publications.europa.eu/ontology/cdm#>

	                SELECT ?celex ?title WHERE {
	                  ?work cdm:resource_legal_id_celex ?celex .
	                  ?work cdm:title ?title .
	                  FILTER (lang(?title) = 'en')
	                }
	                LIMIT 10
	                """, StandardCharsets.UTF_8);

	String response = restTemplate.getForObject(url, String.class);
	return response;
	}
}
