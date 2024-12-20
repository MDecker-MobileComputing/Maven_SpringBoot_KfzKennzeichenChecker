package de.eldecker.dhbw.spring.kennzeichenchecker.model;


/**
 * Record-Klasse für REST-Response.
 * 
 * @param kfzKennzeichen KFZ-Kennzeichen, das überprüft wurde, normiert
 * 
 * @param istOkay {@code true} gdw. {@code kfzKennzeichen} ein gültiges KFZ-Kennzeichen
 *                in Deutschland ist
 *                
 * @param fehlermeldung Wenn {@code istOkay=false}, dann enthält dieses Attribut
 *                      die Fehlermeldung
 */
public record CheckErgebnis( String kfzKennzeichen,
                             boolean istOkay,
                             String fehlermeldung) {
}
