package de.eldecker.dhbw.spring.kennzeichenchecker.logik;


import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import de.eldecker.dhbw.spring.kennzeichenchecker.model.CheckErgebnis;


/**
 * Bean-Klasse mit Logik für Check von KFZ-Kennzeichen.
 * <br><br>
 * 
 * KFZ-Kennzeichen besteht aus:
 * <ul>
 * <li>Unterscheidungszeichen: ein bis drei Buchstaben</li>
 * <li>Erkennungszeichen: ein oder zwei Buchstaben gefolgt von einer bis zu vierstelligen Zahl</li> 
 * </ul>
 * Insgesamt dürfen es aber nur bis zu 8 Zeichen sein.
 */
@Service
public class KfzKennzeichenChecker {

    /**
     * Regulärer Ausdruck für Überprüfung von erster Komponente KFZ-Kennzeichen
     * (Unterscheidungszeichen): besteht sie aus ein bis drei Großbuchstaben?
     */
    private static final Pattern REGEXP_1 = Pattern.compile( "^[A-Z]{1,3}$" );
    
    /**
     * Regulärer Ausdruck für Überprüfung von zweiter Komponente KFZ-Kennzeichen
     * (Buchstaben von Erkennungsnummer): besteht sie aus ein oder zwei Großbuchstaben.  
     */
    private static final Pattern REGEXP_2 = Pattern.compile( "^[A-Z]{1,2}$" );
    
    /**
     * Regulärer Ausdruck für Überprüfung dritte Komponente KFZ-Kennzeichen:
     * (Zahlen von Erkennungsnummer): besteht sie aus einem bis vier Ziffern.
     */
    private static final Pattern REGEXP_3 = Pattern.compile( "^[0-9][0-9]{0,3}$" );    

    
    /**
     * Methode zum eigentlichen Überprüfen von KFZ-Kennzeichen.
     * 
     * @param kfzKennzeichen KFZ-Kennzeichen, das zu überprüfen ist.
     * 
     * @return Ergebnis der Prüfung, enthält auch normalisierte Form
     *         von {@code kfzKennzeichen} und ggf. die Fehlermeldung.
     */
    public CheckErgebnis check( String kfzKennzeichen ) {
        
        String kfzKennzeichenNormal = kfzKennzeichen.trim().toUpperCase();
        
        String[] komponentenArray = kfzKennzeichenNormal.split( " " );                        
        kfzKennzeichenNormal = joinKomponenten( komponentenArray );
        
        if ( komponentenArray.length != 3 ) {
            
            return new CheckErgebnis( kfzKennzeichenNormal, 
                                      false, 
                                      "KFZ-Kennzeichen besteht nicht aus genau drei Komponenten" );
        }
                        
        final String unterscheidungszeichen     = komponentenArray[ 0 ];
        final String erkennungsnummerBuchstaben = komponentenArray[ 1 ];
        final String erkennungsnummerZahlen     = komponentenArray[ 2 ];
        
        
        // Check Komponente 1 (Unterscheidungszeichen)
        if ( REGEXP_1.matcher( unterscheidungszeichen ).matches() == false ) {
            
            return new CheckErgebnis( kfzKennzeichenNormal, 
                                      false, 
                                      "Die erste Komponente besteht nicht aus ein bis drei Buchstaben" );
        }
        
        // TODO: Check gegen Microservice
        
        // Check Komponente 2 (Buchstaben von Erkennungsnummer)
        if ( REGEXP_2.matcher( erkennungsnummerBuchstaben ).matches() == false ) {
            
            return new CheckErgebnis( kfzKennzeichenNormal, 
                                      false, 
                                      "Die zweite Komponente KFZ-Kennzeichen besteht nicht aus ein oder zwei Buchstaben" );            
        }
        
        // Check Komponente 3 (Zahl von Erkennungsnummer)
        if ( REGEXP_3.matcher(erkennungsnummerZahlen).matches() == false ) {

            return new CheckErgebnis( kfzKennzeichenNormal, 
                                      false, 
                                      "Die dritte Komponente KFZ-Kennzeichen besteht nicht aus ein bis vier Ziffern" );                        
        }
        
        int summeZeichen = unterscheidungszeichen.length() + erkennungsnummerBuchstaben.length() + erkennungsnummerZahlen.length();
        if ( summeZeichen > 8 ) {

            return new CheckErgebnis( kfzKennzeichenNormal, 
                                      false, 
                                      "KFZ-Kennzeichen hat mehr als 8 Zeichen/Ziffern" );                                    
        }
        
        // alle Checks bestanden, also KFZ-Kennzeichen okay
        return new CheckErgebnis( kfzKennzeichenNormal, true, "" );
    }
    
        
    /**
     * Komponenten aus String-Array mit Leerzeichen dazwischen konkatenieren.
     * 
     * @param komponentenArray String-Array, der konkateniert werden soll
     * 
     * @return Konkatenierter String, z.B. "KA X 1234" für die drei
     *         Komponenten "KA", "X" und "1234"
     */
    private String joinKomponenten( String[] komponentenArray ) {
                
        StringBuilder ergebnisBuilder = new StringBuilder();
        
        for ( String komponente : komponentenArray ) {
            
            ergebnisBuilder.append( komponente ).append( " ");
        }
        
        return ergebnisBuilder.toString();
    }
    
}
