package it.polimi.ingsw.view;

/**
 * Interface made to connect the view with the comunication layer
 *
 * Being this the entry point of the View Package, I'll put here the idea driving the development of this part:
 * "divide et impera" so one day soon we'll say "veni, vidi, vici"
 *
 * @author giorgio
 */
public interface ViewManager {

    //Funzione da chiamare con il messaggio ricevuto (quindi dalla comunicazione)
    //Controlla se il messaggio è da girare ai dati o alla gestione dello stato
    public void recivedMessage(String payload);

    //Funzione che invia una richiesta di dati (Chiamato dal DataStorager)
    //Invia la ricchiesta request e aspetta una risposta.
    public String sendRequest(String reqest);

    //Funzione che invia un evento in modo asincrono.
    //Ritorna true se l'invio ha avuto successto, false altrimenti.
    public boolean sendEvent(String event);
}
