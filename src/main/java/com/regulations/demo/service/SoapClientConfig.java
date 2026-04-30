package com.regulations.demo.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

@Configuration
public class SoapClientConfig {

	@Bean
	public SaajSoapMessageFactory messageFactory() throws Exception {
	    SaajSoapMessageFactory factory = new SaajSoapMessageFactory();

	    factory.setSoapVersion(org.springframework.ws.soap.SoapVersion.SOAP_12);

	    return factory;
	}
	
    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // 👇 IMPORTANT: your generated package
        marshaller.setClassesToBeBound(
                com.eurlex.client.SearchRequest.class,
                com.eurlex.client.SearchResults.class,
                com.eurlex.client.ObjectFactory.class
        );

        try {
            marshaller.afterPropertiesSet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller marshaller,SaajSoapMessageFactory messageFactory,
                                                 Wss4jSecurityInterceptor securityInterceptor) {

        WebServiceTemplate template = new WebServiceTemplate();
        template.setMarshaller(marshaller);
        template.setUnmarshaller(marshaller);
        template.setMessageFactory(messageFactory);
        // Endpoint
        template.setDefaultUri("https://eur-lex.europa.eu/eurlex-ws");

        // Timeouts (good for prod)
        HttpComponentsMessageSender sender = new HttpComponentsMessageSender();
        sender.setConnectionTimeout(5000);
        sender.setReadTimeout(10000);
        template.setMessageSender(sender);

        // Attach WS-Security
        template.setInterceptors(new org.springframework.ws.client.support.interceptor.ClientInterceptor[]{
                securityInterceptor
        });

        return template;
    }
}