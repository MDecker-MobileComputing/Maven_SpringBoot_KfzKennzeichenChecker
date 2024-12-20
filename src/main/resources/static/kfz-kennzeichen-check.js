/*
 * Für jedes <a>-Element auf der Seite wird das Attribut "target" auf "_blank" gesetzt.
 */
document.addEventListener( "DOMContentLoaded", function() {

    const links = document.querySelectorAll( "a" );

    links.forEach( link => {
        link.setAttribute( "target", "_blank" );
    });
});