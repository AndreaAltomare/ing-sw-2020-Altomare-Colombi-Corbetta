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

    private final static boolean addWait = true;

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

    private void initialization(){
        //new TerminalViewer().start();
        new GUIViewer().start();
        new CLIViewer().start();


        Executer.setSender(this);
    }

    private void end(){
        Viewer.exitAll();
    }

    private void myMain() {

        Object obj = new Object();

        initialization();
        System.out.println("Hello World");

        ViewStatus.init();

        if(addWait){
            synchronized (obj) {
                try {
                    obj.wait(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        view.update((NextStatusEvent)new NextStatusEvent("vai alla login"));
        myWait();
        view.update((RequirePlayersNumberEvent) new RequirePlayersNumberEvent());
        myWait();
        view.update((NextStatusEvent)new NextStatusEvent("vai alla wait"));

        if(addWait) {
            synchronized (obj) {
                try {
                    obj.wait(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ViewMessage.populateAndSend("test async", ViewMessage.MessageType.FROM_SERVER_MESSAGE);
        if(addWait){
            synchronized (obj) {
                try {
                    obj.wait(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        view.update((NextStatusEvent)new NextStatusEvent("newGame"));
        view.update((NextStatusEvent)new NextStatusEvent("gamePreparation"));
        view.update((NextStatusEvent)new NextStatusEvent("Playing"));
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

}
