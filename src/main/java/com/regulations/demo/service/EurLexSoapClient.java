package com.regulations.demo.service;

import javax.xml.namespace.QName;

import org.springframework.stereotype.Service;

import org.springframework.ws.client.core.WebServiceTemplate;

import com.eurlex.client.ObjectFactory;
import com.eurlex.client.SearchRequest;
import com.eurlex.client.SearchResults;

import jakarta.xml.bind.JAXBElement;
import com.eurlex.client.SearchLanguageType;


@Service
public class EurLexSoapClient {

    private final WebServiceTemplate webServiceTemplate;

    public EurLexSoapClient(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    public SearchResults search(String query, int page, int pageSize) {

        SearchRequest request = new SearchRequest();
        request.setExpertQuery(query);   // e.g. "DN=3*"
        request.setPage(page);
        request.setPageSize(pageSize);
        request.setSearchLanguage(SearchLanguageType.EN);

     // 👇 MANUAL WRAPPING (THIS IS THE FIX)
        QName qName = new QName("http://eur-lex.europa.eu/search", "searchRequest");

        JAXBElement<SearchRequest> jaxbRequest =
                new JAXBElement<>(qName, SearchRequest.class, request);

        Object response = webServiceTemplate.marshalSendAndReceive(jaxbRequest);

        // Response handling
        if (response instanceof JAXBElement) {
            return (SearchResults) ((JAXBElement<?>) response).getValue();
        } else {
            return (SearchResults) response;
        }
    }
}
