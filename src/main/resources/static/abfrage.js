
let inputKfzKennzeichen = null;

document.addEventListener( "DOMContentLoaded", function() {

    inputKfzKennzeichen = document.getElementById( "kennzeichen" );
    if ( inputKfzKennzeichen === null ) {

        console.error( "Eingabefeld \"kfzKennzeichen\" wurde nicht gefunden." );
        return;
    }
});


/**
 * Event-Handler für Button.
 */
function checkKennzeichen() {

    const eingabe = inputKfzKennzeichen.value;

    const pfad = "kfzkennzeichen/v1/check?kennzeichen=" + eingabe;

    fetch( pfad )
        .then( response => {

            if ( !response.ok ) {

                throw new Error( "Netzwerkfehler: " + response.statusText );
            }

            return response.json();
        })
        .then(data => {

            const { kfzKennzeichen, istOkay, fehlermeldung } = data;
            if ( istOkay ) {

            alert( "Kennzeichen \"" + kfzKennzeichen + "\" ist gültig." );

            } else {

                alert( "Kennzeichen \"" + kfzKennzeichen + "\" ist ungültig: " + fehlermeldung );
            }
        })
        .catch( error => {

            alert( error );
        });
}
