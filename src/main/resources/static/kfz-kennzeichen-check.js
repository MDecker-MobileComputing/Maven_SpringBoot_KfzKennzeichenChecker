document.addEventListener( "DOMContentLoaded", function() {

    const ulGueltige = document.getElementById( "ulGueltigeKennzeichen" );

    addLink( ulGueltige, "B X 12"   );
    addLink( ulGueltige, "BA X 12"  );
    addLink( ulGueltige, "BAD X 12" );

    addLink( ulGueltige, "HH X 12"  );
    addLink( ulGueltige, "HH XY 12" );

    addLink( ulGueltige, "KA X 1"    );
    addLink( ulGueltige, "KA X 12"   );
    addLink( ulGueltige, "KA X 123"  );
    addLink( ulGueltige, "KA X 1234" );


    const ulUngueltig = document.getElementById( "ulUngueltigeKennzeichen" );

    // falsche Anzahl Komponenten
    addLink( ulUngueltig, "B"        );
    addLink( ulUngueltig, "B 12"     );
    addLink( ulUngueltig, "B 12 X Y" );

    // eine Komponente zu lang
    addLink( ulUngueltig, "ABCD X 12"  );
    addLink( ulUngueltig, "B XYZ 12"   );
    addLink( ulUngueltig, "B XY 12345" );

    // insgesamt mehr als 8 Zeichen
    addLink( ulUngueltig, "BAD XY 1234" );

    // nicht existierendes Unterscheidungszeichen
    addLink( ulUngueltig, "AX AB 42" );
});


/**
 * Diese Funktion fügt unter einem übergebenen ul-Element einen Link zu einer
 * KFZ-Kennzeichen-Überprüfung hinzu.
 *
 * @param {*} ulElement ul-Element, unter dem der Link eingefügt werden soll
 *                      (entweder ul-Element für gültige oder ungültige Kennzeichen)
 *
 * @param {*} kfzKennzeichen KFZ-Kennzeichen, für dessen Überprüfung ein Link
 *                           hinzugefügt werden soll
 */
function addLink( ulElement, kfzKennzeichen ) {

    const liElement = document.createElement( "li" );

    const aElement = document.createElement( "a" );
    aElement.textContent = kfzKennzeichen;
    aElement.href        = "/kfzkennzeichen/v1/check?kennzeichen=" + encodeURIComponent(kfzKennzeichen);
    aElement.target      = "_blank";

    liElement.appendChild(aElement);

    ulElement.appendChild(liElement);
}
