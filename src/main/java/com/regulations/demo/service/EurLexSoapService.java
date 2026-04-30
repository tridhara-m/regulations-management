package com.regulations.demo.service;
import java.util.List;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EurLexSoapService {

    private static final String URL = "https://eur-lex.europa.eu/eurlex-ws";

    public String searchByDn() {

        String soapRequest =
                "<soap:Envelope xmlns:sear=\"http://eur-lex.europa.eu/search\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">" +
                "<soap:Header>" +
                "<wsse:Security soap:mustUnderstand=\"true\" " +
                "xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" " +
                "xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">" +
                "<wsse:UsernameToken>" +
                "<wsse:Username>n00mk0ap</wsse:Username>" +
                "<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">" +
                "ZLxtG2ep10V</wsse:Password>" +
                "</wsse:UsernameToken>" +
                "</wsse:Security>" +
                "</soap:Header>" +
                "<soap:Body>" +
                "<sear:searchRequest>" +
                "<sear:expertQuery>DN=\"31970L0157\"</sear:expertQuery>" +
                "<sear:page>1</sear:page>" +
                "<sear:pageSize>10</sear:pageSize>" +
                "<sear:searchLanguage>en</sear:searchLanguage>" +
                "</sear:searchRequest>" +
                "</soap:Body>" +
                "</soap:Envelope>";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
     // VERY IMPORTANT
        headers.setContentType(MediaType.valueOf("application/soap+xml;charset=UTF-8"));
        headers.setAccept(List.of(MediaType.APPLICATION_XML));

        // Add these (critical for CloudFront)
        headers.add("User-Agent", "Mozilla/5.0");
        headers.add("Accept-Encoding", "gzip,deflate");
        headers.add("Connection", "keep-alive");

        HttpEntity<String> request = new HttpEntity<>(soapRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                URL,
                HttpMethod.POST,
                request,
                String.class
        );

        return response.getBody();
    }
}