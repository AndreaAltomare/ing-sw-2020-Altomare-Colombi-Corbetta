package it.polimi.ingsw;

import it.polimi.ingsw.connection.ConnectionSettings;
import it.polimi.ingsw.connection.client.ClientConnection;
import it.polimi.ingsw.storage.ResourceManager;
import it.polimi.ingsw.view.clientSide.View;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.GUIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.WTerminalViewer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Client-side Application.
 *
 * @see <a href="https://github.com/emanueledelsozzo/ingsoft-prova-finale-2020/blob/master/ese_Socket_Serialization/TrisDistributedMVC/src/main/java/it/polimi/ingsw/ClientApp.java">github.com/emanueledelsozzo/.../ClientApp.java</a>
 * @author AndreaAltomare
 */
public class ClientApp {
    /* Arguments constants */
    private static final String IP_ARG = "-ip";
    private static final String PORT_ARG = "-port";
    private static final String GUI_ARG = "--gui";
    private static final String CLI_ARG = "--cli";
    private static final String TERMINAL_ARG = "--terminal";
    private static final String BAD_ARGS = "Bad arguments.";

    /**
     * Starts Client-side application.
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

        /* Initialize information to properly work with resources */
        try {
            ResourceManager.initializeResources(ClientApp.class);
        }
        catch (URISyntaxException ex) {
            System.out.println("Something went wrong when analyzing the application path.\nApplication is closing...");
            System.exit(1);
        }
        catch (IOException ex) {
            System.out.println("Something went wrong when getting the canonical path for the application.\nApplication is closing...");
            System.exit(1);
        }

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
            connectionSettings = null;
            try {
                connectionSettings = ResourceManager.clientConnectionSettings();
            }
            catch (Exception ex) {
                System.out.println("Bad configuration format.\nClient is going to be initialized with default connection settings.\n\n");
            }
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


        /* Instantiate View and ClientConnection (Connection-Layer) */
        client = new ClientConnection(ip, port);
        view = new View(input, client, viewer);
        /* Add observers */
        client.addMVEventsListener(view);
        view.addObserver(client);
        /* Run View */
        view.run();
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
        else if(arguments.contains(TERMINAL_ARG)) {
            //return new TerminalViewer();
            return  new WTerminalViewer();
        }
        else
            return null;
    }
}
