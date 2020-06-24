package it.polimi.ingsw;

import it.polimi.ingsw.connection.ConnectionSettings;
import it.polimi.ingsw.connection.client.ClientConnection;
import it.polimi.ingsw.connection.server.ServerConnection;
import it.polimi.ingsw.storage.ResourceManager;
import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.GUIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.TerminalViewer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Client-side Application
 *
 * @see <a href="https://github.com/emanueledelsozzo/ingsoft-prova-finale-2020/blob/master/ese_Socket_Serialization/TrisDistributedMVC/src/main/java/it/polimi/ingsw/ClientApp.java">github.com/emanueledelsozzo/.../ClientApp.java</a>
 * @author AndreaAltomare
 */
public class ClientApp {
    /* Arguments constants */
    private static final String IP_ARG = "-ip";
    private static final String PORT_ARG = "-ip";
    private static final String GUI_ARG = "-gui";
    private static final String CLI_ARG = "-cli";
    private static final String TERMINAL_ARG = "-terminal";
    private static final String BAD_ARGS = "Bad arguments.";

// todo marked resources folder as Resources root
    /**
     * Start Client-side application.
     *
     * @param args (main() arguments)
     */
    public static void main(String[] args) {
        final Scanner input = new Scanner(System.in); // user STDIN [UNIQUE!] reference
        View view;
        ClientConnection client;
        ConnectionSettings connectionSettings;
        final String DEFAULT_IP = "127.0.0.1";
        final int DEFAULT_PORT = 9999; // default port
        String ip = DEFAULT_IP;
        int port = DEFAULT_PORT;
        Viewer viewer;
        /* Argument parsing */
        boolean validArguments = true; // tells if arguments parsing went good

        List<String> argsList = Arrays.asList(args);

        /* GET CUSTOMS CONNECTION SETTINGS */
        if(argsList.contains(IP_ARG) && argsList.contains(PORT_ARG)) {
            try {
                // GET IP
                int ipArgIndex = argsList.indexOf(IP_ARG) + 1;
                ip = argsList.get(ipArgIndex);
                // GET PORT
                int portArgIndex = argsList.indexOf(PORT_ARG) + 1;
                port = Integer.parseInt(argsList.get(portArgIndex));
            }
            catch(Exception ex) {
                //if(View.debugging)
                System.out.println(BAD_ARGS);
                return;
            }
        }
        else {
            /* Retrieve connection configuration settings */
            connectionSettings = ResourceManager.clientConnectionSettings();
            if (connectionSettings != null) {
                ip = connectionSettings.getIp();
                port = connectionSettings.getPort();
            }
        }


        /* GET VIEWER SETTINGS */
        viewer = getViewer(argsList);
        if(viewer == null) {
            //if(View.debugging)
            System.out.println(BAD_ARGS);
            return;
        }

        // TODO: scrivere operazioni di istanziazione della view (magari su un thread diverso), della GUI/CLI, ecc...

        // todo verificare che funzioni con gli argomenti
        // todo verificare che funzioni senza gli argomenti
        // todo verificare che funzioni con i file di configurazione presenti
        // todo verificare che funzioni senza i file di configurazione presenti
//        /* Retrieve connection configuration settings */
//        if(args.length == 2) {
//            ip = new String(args[0]); // not sure if without "new String(...)" statement i'd get a new reference
//            port = Integer.parseInt(args[1]);
//        }
//        else {
//            connectionSettings = ResourceManager.clientConnectionSettings();
//            if (connectionSettings != null) {
//                ip = connectionSettings.getIp();
//                port = connectionSettings.getPort();
//            }
//        }

        /* Instantiate View and ClientConnection (Connection-Layer) */
        client = new ClientConnection(ip, port);
        view = new View(input, client, viewer);
        /* Add observers */ // todo check if this system works (since reading and writing from/to socket is done by threads)
        client.addMVEventsListener(view);
        view.addObserver(client);
        //try {

//        //new TerminalViewer().start();
//        new GUIViewer().start(); // todo mettere in View.java
//        //new WTerminalViewer().start();
//        //new CLIViewer().start();
//
//        Executer.setSender(view);
//        ViewStatus.init();
//
//        new CLIViewer().start();
//        new TerminalViewer().start();

        view.run();


            //client.run();
        //}
        //catch(IOException ex) {
        //    System.err.println(ex.getMessage());
        //}
    }

    /**
     * Gets the Viewer the User asked for through program args.
     *
     * @param arguments List of arguments
     * @return A specific Viewer
     */
    private static Viewer getViewer(List<String> arguments) {
        if(!(arguments.contains(CLI_ARG) || arguments.contains(TERMINAL_ARG)))
            return new GUIViewer();
        else if(arguments.contains(GUI_ARG))
            return new GUIViewer();
        else if(arguments.contains(CLI_ARG))
            return new CLIViewer();
        else if(arguments.contains(TERMINAL_ARG))
            return new TerminalViewer();
        else
            return null;
    }
}
