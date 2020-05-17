package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SetPlayerNumberExecuter;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.Scanner;

public class TerminalNumberPlayerViewer extends TerminalStatusViewer {

    StatusViewer parentViewer;

    public TerminalNumberPlayerViewer(StatusViewer parentViewer){ this.parentViewer = parentViewer; }

    @Override
    public void print() {
        int num;
        String tmp;
        System.out.println("[Terminal]:\tNumberPlayer");
        while(true){
            System.out.println("\tInsert the number of players:");
            tmp =  new Scanner(System.in).nextLine();
            try{
                num = Integer.parseInt(tmp);
                break;
            }catch (Exception e){
                System.out.println("\tInvalid input");
            }
        }
        Executer executer = parentViewer.getMyExecuters().get("NumberPlayers");
        SetPlayerNumberExecuter setPlayerNumberExecuter;
        if(new SetPlayerNumberExecuter().isSameType(executer)) {
            setPlayerNumberExecuter = (SetPlayerNumberExecuter) executer;
        }else{
            throw new Error();
        }
        try {
            setPlayerNumberExecuter.setNumberOfPlayers(num);
            setPlayerNumberExecuter.doIt();
        } catch (WrongParametersException | CannotSendEventException e) {
            ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
        }
    }
}
