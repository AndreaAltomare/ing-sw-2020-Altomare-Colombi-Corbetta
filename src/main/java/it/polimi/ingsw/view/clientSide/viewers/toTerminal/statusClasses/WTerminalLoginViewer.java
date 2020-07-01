package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SetNicknameExecuter;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.LoginViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.Scanner;

/**
 * Class that represents the <code>WTerminalStatusViewer</code> Login on the Windows Terminal
 * using methods of <code>PrintFunction</code>
 *
 * @see WTerminalStatusViewer
 * @see PrintFunction
 * @author Marco
 */
public class WTerminalLoginViewer extends WTerminalStatusViewer {

    private LoginViewer loginViewer;

    final String FIRST_PART = "Please, insert your";
    final String SECOND_PART = "Nickname:";

    /**
     * Constructor to set correct <code>StatusViewer</code>
     *
     * @see LoginViewer
     * @param loginViewer  <code>StatusViewer</code> linked at this class
     */
    public WTerminalLoginViewer(LoginViewer loginViewer) {
        this.loginViewer = loginViewer;
    }

    /**
     * Print the nickname's request using some internal constants to print it on a block moved by a human
      */
    private void showNicknameRequest() {

        final int BLOCK_LENGTH; //after initialization it becomes constant

        if ( FIRST_PART.length() > SECOND_PART.length() ) {
            BLOCK_LENGTH = FIRST_PART.length() + 3*2;
        } else {
            BLOCK_LENGTH = SECOND_PART.length() + 3*2;
        }


        // upper edge of block
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE + 3);
        PrintFunction.printRepeatString("_", BLOCK_LENGTH);
        System.out.println();

        // head and block
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE - 1);
        System.out.print( "'O " );

        System.out.print("|");
        PrintFunction.printRepeatString(" ", BLOCK_LENGTH);
        System.out.print("|");
        System.out.println();

        // chest and request's first part
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print( "/|" );

        System.out.print("|");
        PrintFunction.printAtTheMiddle(FIRST_PART, BLOCK_LENGTH);
        System.out.print("|");
        System.out.println();

        // leg, body and request's second part
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE - 4);
        System.out.print( "_/\\/ |");

        System.out.print("|");
        PrintFunction.printAtTheMiddle(SECOND_PART, BLOCK_LENGTH);
        System.out.print("|");
        System.out.println();

        // other leg and block's down edge
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE -2);
        System.out.print("/   ");

        System.out.print("|");
        PrintFunction.printRepeatString("_", BLOCK_LENGTH);
        System.out.print("|");
        System.out.println();

        // foot
        PrintFunction.printRepeatString(" ",PrintFunction.STARTING_SPACE -4);
        System.out.print( "_/    ");
        if ( ((BLOCK_LENGTH - SECOND_PART.length()) %2) == 0) {
            PrintFunction.printRepeatString(" ", ((BLOCK_LENGTH - SECOND_PART.length()) /2) - 2);
        } else {
            PrintFunction.printRepeatString(" ", ((BLOCK_LENGTH - SECOND_PART.length()) /2) - 1);
        }

    }

    /**
     * Method which prints a little signal to notify on standard output where it is possible to write the nickname
     * then returns the read value if its length is 11 or minus, or its first eight char if it isn't
     *
     * @return <code>String</code> which is read if its length is 11 or minus. its first eleventh chars if it isn't
     */
    private String getNicknameResponse() {
        final int maxLength = 11;
        String response;
        Scanner input = new Scanner(System.in);

        System.out.printf(  ">>(Max %d):", maxLength);

        response = input.nextLine();
        if (response.length() > maxLength) {
            response = response.substring(0,maxLength);
        }

        return response;
    }

    /**
     * Checks if the <code>String</code> is a possible value to set in the executer,
     * then it notifies with a message on standard output if the nickname is correct or not
     *
     * @param response <code>String</code> to represent the nickname
     * @return response is correctly set to executer ? true : false
     */
    private boolean checkNicknameResponse(String response) {
        final String CORRECT_MESSAGE = "Your nickname request is correctly send";
        String wrongParameterMessage = "Nickname chosen is not valid, please change it";
        boolean approvedResponse = false;
        SetNicknameExecuter setNicknameExecuter = (SetNicknameExecuter) loginViewer.getMyExecuters().get("NickName");

        try {
            setNicknameExecuter.setNickname(response);
            approvedResponse = true;
            setNicknameExecuter.doIt();
            PrintFunction.printCheck(CORRECT_MESSAGE);
        } catch (WrongParametersException e) {
            if (!e.getMessage().equals("") ) {
                wrongParameterMessage = e.getMessage();
            }
            PrintFunction.printError(wrongParameterMessage);
        } catch (CannotSendEventException e) {
            PrintFunction.printError(e.getErrorMessage());
        }  System.out.println();

        return approvedResponse;
    }

    /**
     * Uses the private methods of this class to print the nickname's request,
     * to read the nickname and to check it, the if the nickname is't correctly set to executer
     * recalls all private methods in the same order
     */
    @Override
    public void show() {
        String nickname;
        boolean end = false;

        while (!end) {
            PrintFunction.printRepeatString("\n", 2);
            this.showNicknameRequest();
            nickname = this.getNicknameResponse();
            end = this.checkNicknameResponse(nickname);
            PrintFunction.printRepeatString("\n", 2);
        }
    }
}