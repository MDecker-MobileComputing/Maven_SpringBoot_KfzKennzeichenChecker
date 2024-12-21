package de.eldecker.dhbw.spring.kennzeichenchecker.model.extern;


/**
 * Objekt als Deserialisierungsziel für Antwort von REST-API für Abfrage 
 * Unterscheidungskennzeichen.
 * 
 * @param erfolgreich {@code true} gdw. ein Unterscheidungszeichen gefunden wurde
 * 
 * @param fehlermeldung wenn {@code erfolgreich=false}, denn ist dieser String nicht leer und
 *                      enthält eine Beschreibung des Fehlers
 *                      
 * @param unterscheidungszeichen Wenn {@code erfolgreich=false}, dann sind alle Felder leer,
 *                               ansonsten ist das Ergebnis enthalten
 */
public record RestErgebnisRecord( boolean erfolgreich, 
                                 String fehlermeldung,
                                 Unterscheidungszeichen unterscheidungszeichen ) {
}