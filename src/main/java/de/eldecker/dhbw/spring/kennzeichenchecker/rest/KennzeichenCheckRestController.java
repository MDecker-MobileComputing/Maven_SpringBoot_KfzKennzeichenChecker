package de.eldecker.dhbw.spring.kennzeichenchecker.rest;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.eldecker.dhbw.spring.kennzeichenchecker.logik.KfzKennzeichenChecker;
import de.eldecker.dhbw.spring.kennzeichenchecker.model.CheckErgebnis;


/**
 * Klasse definiert REST-Endpunkte.
 */
@RestController
@RequestMapping ( "/kfzkennzeichen/v1" )
public class KennzeichenCheckRestController {

    /** Bean mit Logik für eigentlichen Check. */
    private KfzKennzeichenChecker _kennzeichenChecker;
    
    
    /**
     * Konstruktor für Dependency Injection.
     */
    @Autowired
    public KennzeichenCheckRestController( KfzKennzeichenChecker kfzKennzeichenChecker ) {
        
        _kennzeichenChecker = kfzKennzeichenChecker;
    }
    
    
    /**
     * REST-Endpunkt für Überprüfung eines KFZ-Kennzeichens.
     * 
     * @param kennzeichen URL-Parameter für KFZ-Kennzeichen, das überprüft werden soll
     * 
     * @return Antwort für HTTP-Request; HTTP-Status-Code ist immer OK
     */
    @GetMapping( "/check" )
    public ResponseEntity<CheckErgebnis> getAnzahl( @RequestParam String kennzeichen ) {

        final CheckErgebnis erg = _kennzeichenChecker.check( kennzeichen );
        
        return ResponseEntity.status( OK ).body( erg );
    }    
    
}
