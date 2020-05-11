package it.polimi.ingsw.view.clientSide.viewers.toTerminal;

import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.Viewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;

public class TerminalViewer extends Viewer {
    @Override
    public void refresh() { }

    public TerminalViewer(){
        super();
        Viewer.registerViewer(this);
    }

    @Override
    public void run() {
        System.out.println("Thread started");
        while(true){
            synchronized (super.wakers) {
                try {
                    super.wakers.wait();
                } catch (InterruptedException e) {
                    return;
                }
            }
            synchronized (super.myViewerQueue){
                while(!super.myViewerQueue.isEmpty()){
                    ViewerQueuedEvent queued = super.myViewerQueue.get(0);
                    super.myViewerQueue.remove(0);
                    if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.EXIT) return;
                    if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.SET_STATUS){
                        System.out.println(ViewStatus.getActual().toString());
                        ((StatusViewer)queued.getPayload()).toTerminal().print();
                    }
                    if (queued.getType()== ViewerQueuedEvent.ViewerQueuedEventType.MESSAGE){
                        System.out.println("[Message: " + ((ViewMessage)queued.getPayload()).getMessageType() + "]\t" + ((ViewMessage)queued.getPayload()).getPayload());
                    }
                }
            }
        }
    }
}
