package com.regulations.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.*;
import org.apache.jena.sparql.exec.http.QueryExecutionHTTP;
import org.apache.jena.sparql.exec.http.QuerySendMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.regulations.demo.entity.Regulation;
import com.regulations.demo.repository.RegulationRepository;

@Service
public class EurlexService {

	private static final String ENDPOINT = "https://publications.europa.eu/webapi/rdf/sparql";

	@Autowired
	private RegulationRepository repository;

	public List<String> fetchCelexIds() {

		String query = """
												SELECT ?celex
				WHERE {
				  ?work <http://publications.europa.eu/ontology/cdm#resource_legal_id_celex> ?celex .
				}
				LIMIT 1
												""";
		List<String> celexList = new ArrayList<>();
		Query queryObj = QueryFactory.create(query);
		try (QueryExecution qe = (QueryExecutionHTTP.service(ENDPOINT).query(queryObj)).sendMode(QuerySendMode.asPost)
				.build()) {
			ResultSet results = qe.execSelect();
			while (results.hasNext()) {
				QuerySolution sol = results.next();
				System.out.println("Sol:" + sol);

				if (sol.contains("celex")) {
					celexList.add(sol.get("celex").toString());
				}

			}

		} catch (Exception e) {
			System.out.println("SPARQL query failed");
			e.printStackTrace();
		}
		System.out.println("CELEX fetched: " + celexList.size());
		return celexList;

	}

	private Regulation fetchRegulation(String celex) {
		try {
			String url = "https://publications.europa.eu/resource/celex/" + celex+"?format=xml";
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", "application/rdf+xml");

			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<String> response = restTemplate.exchange(
			        url,
			        HttpMethod.GET,
			        entity,
			        String.class
			);
			if (response.getStatusCode().is3xxRedirection()) {

			    String redirectUrl = response.getHeaders().getLocation().toString();

			    System.out.println("Redirecting to: " + redirectUrl);

			    response = restTemplate.exchange(
			            redirectUrl,
			            HttpMethod.GET,
			            entity,
			            String.class
			    );
			}

			System.out.println("Status: " + response.getStatusCode());
			System.out.println("Body: " + response.getBody());
			Regulation reg = new Regulation();
			reg.setCelex(celex);
			reg.setTitle("Yet to be parsed");
			return reg;
		} catch (Exception e) {
			System.out.println("Caught exception " + e);
			return null;
		}

	}

	public void fetchAndSave() {
		List<String> celexIds = fetchCelexIds();
		List<Regulation> regulations = new ArrayList();

		if (celexIds.isEmpty()) {
			System.out.println("No regulations fetched");
			return;
		}
		for (String celex : celexIds) {
			Regulation reg = fetchRegulation(celex);
			if (reg != null) {
				regulations.add(reg);
			}
		}
		repository.saveAll(regulations);
		System.out.println("Saved " + regulations.size() + " regulations.");
	}

}
