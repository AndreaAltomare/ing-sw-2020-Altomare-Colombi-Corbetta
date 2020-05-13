package it.polimi.ingsw.view.clientSide;

import it.polimi.ingsw.controller.events.NextStatusEvent;
import it.polimi.ingsw.controller.events.RequirePlayersNumberEvent;
import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.interfaces.ViewSender;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toGUI.GUIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.TerminalViewer;

import java.util.EventObject;

public class ViewTester implements ViewSender {

    private Object lock = new Object();
    private View view = new View(null, null);

    private void myWait(){
        synchronized (lock){
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initialisation(){
        new TerminalViewer().start();
        new GUIViewer().start();
        new CLIViewer();


        Executer.setSender(this);
    }

    private void end(){
        Viewer.exitAll();
    }

    private void myMain(){
        Object obj = new Object();

        initialisation();
        System.out.println("Hello World");

        ViewStatus.init();

        synchronized (obj) {
            try {
                obj.wait(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        view.update((NextStatusEvent)new NextStatusEvent("vai alla login"));
        myWait();
        view.update((RequirePlayersNumberEvent) new RequirePlayersNumberEvent());
        myWait();
        view.update((NextStatusEvent)new NextStatusEvent("vai alla wait"));

        synchronized (obj) {
            try {
                obj.wait(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ViewMessage.populateAndSend("test async", ViewMessage.MessageType.FROM_SERVER_MESSAGE);
        synchronized (obj) {
            try {
                obj.wait(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        end();
    }

    public static void main(String[] args) {
        new ViewTester().myMain();
    }

    @Override
    public void send(EventObject event) {
        synchronized (lock){
            lock.notifyAll();
        }
    }

    //Per Model:
    /*
    public static void setChallenger(Object o){}
    public static void setCardsInGame (Object o){}
    public static void setStartPlayer(Object o){}
    public static void setPlayerCard(Object a, Object b){}
    public static int getBoardXSize(){return 0;}
    public static int getBoardYSize(){ return 0; }
    public static List<String> getPlayers(){return null;}
    public static Map<String, List<String>> getWorkers(){return null;}
    public static boolean placeWorker (int x, int y, Object o){return true;}
    public static int WORKERS_PER_PLAYER = 0;
    */
}
