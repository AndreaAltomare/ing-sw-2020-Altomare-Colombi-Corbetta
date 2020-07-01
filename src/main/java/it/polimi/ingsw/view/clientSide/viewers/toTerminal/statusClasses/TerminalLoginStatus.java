package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.Executer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SetNicknameExecuter;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.messages.ViewMessage;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.TerminalStatusViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.Scanner;

public class TerminalLoginStatus extends TerminalStatusViewer {

    StatusViewer parentViewer;

    public TerminalLoginStatus(StatusViewer parentViewer){ this.parentViewer = parentViewer; }

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
