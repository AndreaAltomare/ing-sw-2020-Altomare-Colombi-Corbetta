package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SetNicknameExecuter;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.LoginViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.CheckQueueException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.Scanner;

/**
 * Class to represent the <code>TerminalStatusViewer</code> for the ViewStatus LOGIN .
 */
public class TerminalLoginStatus extends TerminalStatusViewer {

    StatusViewer parentViewer;


    /**
     * Constructor.
     *
     * @param parentViewer (the StatusViewer to which this refers).
     */
    public TerminalLoginStatus(StatusViewer parentViewer){ this.parentViewer = parentViewer; }

    /**
     * Method to print the status representation.
     */
    @Override
    public void print() {
        String name;
        System.out.println("[Terminal]:\tlogin");
        System.out.println("\tInsert your nickname:");
        name = new Scanner(System.in).nextLine();
        Executer executer = parentViewer.getMyExecuters().get("NickName");
        SetNicknameExecuter setNicknameExecuter;
        if(new SetNicknameExecuter().isSameType(executer)) {
            setNicknameExecuter = (SetNicknameExecuter) executer;
        }else{
            throw new Error();
        }
        try {
            setNicknameExecuter.setNickname(name);
            setNicknameExecuter.doIt();
        } catch (WrongParametersException | CannotSendEventException e) {
            ViewMessage.populateAndSend(e.getMessage(), ViewMessage.MessageType.EXECUTER_ERROR_MESSAGE);
        }
    }
}
