package de.eldecker.dhbw.spring.kennzeichenchecker.rest;

import static de.eldecker.dhbw.spring.kennzeichenchecker.model.extern.UZKategorieEnum.NICHT_DEFINIERT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import de.eldecker.dhbw.spring.kennzeichenchecker.model.extern.RestErgebnisRecord;
import de.eldecker.dhbw.spring.kennzeichenchecker.model.extern.Unterscheidungszeichen;


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
@EnableCaching
@EnableScheduling 
@CacheConfig( cacheNames = "unterscheidungszeichenCache" )
public class UnterscheidungszeichenRestClient {
    
    private static final Logger LOG = LoggerFactory.getLogger( UnterscheidungszeichenRestClient.class );
    
    /** Leeres Unterscheidungszeichenobjekt, wird im Fehlerfall benötigt. */
    private static final Unterscheidungszeichen UNTERSCHEIDUNGSZEICHEN_EMPTY =
                                                        new Unterscheidungszeichen( "", "", NICHT_DEFINIERT );

    /** Objekt für REST-Calls. */
    private final RestClient _restClient;
    

    /**
     * Konstruktor für Erzeugung {@code RestClient}-Objekt mit
     * Basis-URL {@code http://localhost:8080}.
     */
    @Autowired
    public UnterscheidungszeichenRestClient ( RestClient.Builder restClientBuilder ) {

        _restClient = restClientBuilder.baseUrl( "http://localhost:8080/unterscheidungszeichen" )
                                       .build();
    }
    
    
    /**
     * Unterscheidungszeichen (z.B. "KA" für "Karlsruhe") von anderer REST-API abfragen.
     *  
     * @param unterscheidungszeichen Unterscheidungszeichen, das überprüft werden soll; 
     *                               sollte schon normalisiert sein.
     *                               
     * @return Antwort von externem REST-Service, wird nicht {@code null} sein
     */
    @Cacheable( value = "unterscheidungszeichenCache" )
    public RestErgebnisRecord holeUnterscheidungszeichen( String unterscheidungszeichen ) {
        
        ResponseEntity<RestErgebnisRecord> ergebnisEntity = null;
        
        final String pfad = "/v1/suche/" + unterscheidungszeichen;
        LOG.info( "Sende jetzt HTTP-Request an folgenden Pfad: {}", pfad );
                
        try {
 
            ergebnisEntity = _restClient.get()
                                        .uri( pfad )
                                        .retrieve()
                                        .toEntity( RestErgebnisRecord.class );        
                                                
            Unterscheidungszeichen uz = ergebnisEntity.getBody().unterscheidungszeichen();
            
            return new RestErgebnisRecord( true, "", uz ); 
        }
        catch ( RestClientResponseException ex ) {
            
            // "By default, when RestClient encounters a 4xx or 5xx status code in the HTTP response, 
            // it raises an exception that’s a subclass of RestClientException."
            // https://www.baeldung.com/spring-boot-restclient#error         
            
            int httpStatusCode = ex.getStatusCode().value();
            switch ( httpStatusCode ) {

                case 400:
                    LOG.error( "Unterscheidungszeichen hat unzulässiges Format." );
                    return new RestErgebnisRecord( 
                                        false, 
                                        "Unterscheidungszeichen \"" + unterscheidungszeichen + "\" hat unzulässiges Format", 
                                        UNTERSCHEIDUNGSZEICHEN_EMPTY );
                case 404:
                    LOG.warn( "Unterscheidungszeichen existiert nicht: " + unterscheidungszeichen );
                    return new RestErgebnisRecord( 
                                        false, 
                                        "Unterscheidungszeichen \"" + unterscheidungszeichen + "\" wurde nicht gefunden", 
                                        UNTERSCHEIDUNGSZEICHEN_EMPTY );                    
                default:
                    LOG.error( "Externer Service hat unerwarteten Fehlercode zurückgeliefert: " + httpStatusCode ); 
                    return new RestErgebnisRecord( 
                                        false, 
                                        "Unerwarteter HTTP-Status-Code " + httpStatusCode + " von Service erhalten.", 
                                        UNTERSCHEIDUNGSZEICHEN_EMPTY );                    
            }
        }
        catch ( ResourceAccessException  ex ) {

            LOG.error( "Externer Service für Unterscheidungszeichen-Abfrage war nicht erreichbar." );
            
            return new RestErgebnisRecord( 
                    false, 
                    "Service für Unterscheidungszeichen war nicht erreichbar",  
                    UNTERSCHEIDUNGSZEICHEN_EMPTY );            
        }
    }
    
    
    /**
     * Diese Methode ist mit {@code Scheduled} annotiert, so dass sie perodisch aufgerufen wird. 
     * Bei jedem Aufruf wird der Cache gelöscht. Die Zeitwerte für die Annotation {@code Scheduled} 
     * sind in Millisekunden.
     */
    @Scheduled( fixedRate = 120_000, initialDelay = 120_000 )
    @CacheEvict( value = "unterscheidungszeichenCache", allEntries = true )
    public void cacheLoeschen() {

        LOG.info( "Der Unterscheidungszeichen-Cache wurde geleert." );
    }    
    
}
