package it.polimi.ingsw.view.clientSide.viewers.toTerminal;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.exceptions.CheckQueueException;
import it.polimi.ingsw.view.exceptions.EmptyQueueException;

public class TerminalViewer extends Viewer {
    @Override
    public void refresh() { }

    public TerminalViewer(){
        super();
        Viewer.registerViewer(this);
    }

    protected void enqueue(ViewerQueuedEvent event){
        if(event.getType()==ViewerQueuedEvent.ViewerQueuedEventType.MESSAGE){
            new Thread(){
                String mySt;

                public Thread setSt(String st){
                    mySt = st;
                    return this;
                }

                public void run(){
                    System.out.println(mySt);
                }
            }.setSt((new String("[Message: " + ((ViewMessage)event.getPayload()).getMessageType() + "]\t" + ((ViewMessage)event.getPayload()).getPayload()))).start();
        }else{
            super.enqueue(event);
        }
    }

    @Override
    public void run() {
        ViewerQueuedEvent queued;
        while(true){
            waitNextEvent();

            while(true){
                try {
                    queued = getNextEvent();
                } catch (EmptyQueueException e) {
                    break;
                }
                if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.EXIT) return;
                if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.SET_STATUS){
                    System.out.println(ViewStatus.getActual().toString());
                    ((StatusViewer)queued.getPayload()).toTerminal().setMyTerminalViewer(this);
                    try {
                        ((StatusViewer)queued.getPayload()).toTerminal().print();
                    } catch (CheckQueueException ignore) {
                    }
                }
                if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.MESSAGE){
                    System.out.println();
                }
            }
        }
    }
}
