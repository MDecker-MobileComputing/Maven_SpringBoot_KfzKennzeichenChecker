package de.eldecker.dhbw.spring.kennzeichenchecker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Klasse mit Einstiegspunkt des Programms.
 */
@SpringBootApplication
public class KfzKennzeichencheckerApplication {

	public static void main( String[] args ) {
	    
		SpringApplication.run( KfzKennzeichencheckerApplication.class, args );
	}

}
