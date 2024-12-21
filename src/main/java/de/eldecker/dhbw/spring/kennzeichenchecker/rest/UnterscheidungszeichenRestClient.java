package de.eldecker.dhbw.spring.kennzeichenchecker.rest;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import de.eldecker.dhbw.spring.kennzeichenchecker.model.extern.RestErgebnisRecord;


/**
 * Diese Bean implementiert den REST-Client, mit dem von einer externen
 * REST-API die Gültigkeit eines Unterscheidungszeichen (ein bis drei
 * Buchstaben ganz am Anfang eines deutschen KFZ-Kennzeichens) abgefragt
 * werden kann. 
 * <br><br>
 * 
 * Microservice/REST-API für Abfrage Unterscheidungszeichen:
 * <a href="https://github.com/MDecker-MobileComputing/Maven_SpringBoot_KfzUnterscheidungszeichen">siehe hier</a>
 */
@Service
public class UnterscheidungszeichenRestClient {
	
	private static final Logger LOG = LoggerFactory.getLogger( UnterscheidungszeichenRestClient.class );

    /** Objekt für REST-Calls. */
    private final RestClient _restClient;
    
    /**
     * Konstruktor für Erzeugung {@code RestClient}-Objekt mit
     * Basis-URL {@code http://localhost:8080}.
     */
    @Autowired
    public UnterscheidungszeichenRestClient ( RestClient.Builder restClientBuilder ) {

        _restClient = restClientBuilder.baseUrl( "http://localhost:8080/unterscheidungszeichen/" )
                                       .build();
    }
    
    
    /**
     * Unterscheidungszeichen (z.B. "KA" für "Karlsruhe") von
     *  
     * @param unterscheidungszeichen Unterscheidungszeichen, das überprüft werden soll; sollte
     *                               schon normalisiert sein.
     *                               
     * @return Optional enthält bei erfolgreicher Anfrage Antwort von externem REST-Service.
     */
    public Optional<RestErgebnisRecord> holeUnterscheidungszeichen( String unterscheidungszeichen ) {
    	
        final String pfad = "v1/suche/" + unterscheidungszeichen;
        LOG.info( "Pfad für REST-Request: {}", pfad );
        
        try {
        
            ResponseEntity<RestErgebnisRecord> ergebnisEntity = 
            											_restClient.get()
                                                                   .uri( pfad )
                                                                   .retrieve()
                                                                   .toEntity( RestErgebnisRecord.class );
            
            RestErgebnisRecord ergebnisRecord = ergebnisEntity.getBody();
            
            return Optional.of( ergebnisRecord );
            
        }
        catch ( RestClientResponseException  ex ) {
        	
        	LOG.error( "Exception bei REST-Abfrage: " + ex.getMessage() );
        	return Optional.empty();
        }
    }
	
}
