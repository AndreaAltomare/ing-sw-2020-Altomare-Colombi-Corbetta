package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SetNicknameExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.LoginViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.Scanner;

public class WTerminalLoginViewer extends WTerminalStatusViewer {

    private LoginViewer loginViewer;

    final int STARTING_SPACE = 7;
    final String FIRST_PART = "Please, insert your";
    final String SECOND_PART = "Nickname:";

    /**
     * Constructor to set correct StatusViewer
     * @param loginViewer
     */
    public WTerminalLoginViewer(LoginViewer loginViewer) {
        this.loginViewer = loginViewer;
    }

    /**
     * Print the nickname's request using some internal constants to print it on a block moved by a human
     * example image
     *          ___________
     *     'O  |          |
     *      /|| Request |
     *  _/\/||__________|
     *   _/
     */
    private void showNicknameRequest() {

        final int BLOCK_LENGTH; //after initialization it becomes constant

        if ( FIRST_PART.length() > SECOND_PART.length() ) {
            BLOCK_LENGTH = FIRST_PART.length() + 3*2;
        } else {
            BLOCK_LENGTH = SECOND_PART.length() + 3*2;
        }


        // upper edge of block
        PrintFunction.printRepeatString(" ", STARTING_SPACE + 3);
        PrintFunction.printRepeatString("_", BLOCK_LENGTH);
        System.out.println();

        // head and block
        PrintFunction.printRepeatString(" ", STARTING_SPACE - 1);
        System.out.print( "'O " );

        System.out.print("|");
        PrintFunction.printRepeatString(" ", BLOCK_LENGTH);
        System.out.print("|");
        System.out.println();

        // chest and request's first part
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.print( "/|" );

        System.out.print("|");
        PrintFunction.printAtTheMiddle(FIRST_PART, BLOCK_LENGTH);
        System.out.print("|");
        System.out.println();

        // leg, body and request's second part
        PrintFunction.printRepeatString(" ", STARTING_SPACE - 4);
        System.out.print( "_/\\/ |");

        System.out.print("|");
        PrintFunction.printAtTheMiddle(SECOND_PART, BLOCK_LENGTH);
        System.out.print("|");
        System.out.println();

        // other leg and block's down edge
        PrintFunction.printRepeatString(" ", STARTING_SPACE -2);
        System.out.print("/   ");

        System.out.print("|");
        PrintFunction.printRepeatString("_", BLOCK_LENGTH);
        System.out.print("|");
        System.out.println();

        // foot
        PrintFunction.printRepeatString(" ",STARTING_SPACE -4);
        System.out.print( "_/    ");
        if ( ((BLOCK_LENGTH - SECOND_PART.length()) %2) == 0) {
            PrintFunction.printRepeatString(" ", ((BLOCK_LENGTH - SECOND_PART.length()) /2) - 2);
        } else {
            PrintFunction.printRepeatString(" ", ((BLOCK_LENGTH - SECOND_PART.length()) /2) - 1);
        }

    }

    /**
     * Prints a little signal to notify on standard output where it is possible to write the nickname
     * then returns the read value if its length is 8 or minus, or its first eight char if it isn't
     * @return the string which is read if its length is 8 or minus. its first eight char if it isn't
     */
    private String getNicknameResponse() {
        String response;
        Scanner input = new Scanner(System.in);

        System.out.print(  ">>(Max 8):");

        response = input.nextLine();
        if (response.length() > 8) {
            response = response.substring(0,7);
        }

        return response;
    }

    /**
     * Checks if the string is a possible value and sends it with the correct executer,
     * then it notifies with a message on standard output if the nickname is correct or not
     * @param response nickname chosen by player
     * @return boolean value ( true => correct nickname, false => wrong nickname
     */
    private boolean checkNicknameResponse(String response) {
        boolean approvedResponse = false;
        SetNicknameExecuter setNicknameExecuter = (SetNicknameExecuter) loginViewer.getMyExecuters().get("NickName");

        try {
            setNicknameExecuter.setNickname(response);
            setNicknameExecuter.doIt();
            System.out.println( "\n\t" +
                                "  /" + "\n\t" +
                                "\\/ Your Nickname is correctly set" + "\n");
            approvedResponse = true;
        } catch (WrongParametersException e) {
            System.out.println( "\n\t" +
                                ">< Nickname chosen is not valid, please change it");
        } catch (CannotSendEventException e) {
            System.out.printf(" \n\t" +
                                ">< %s" +"\n", e.toString());
        }
        System.out.println();

        return approvedResponse;
    }

    /**
     * Uses the private methods of this class to print the nickname's request,
     * to read the nickname and to check it, the if the nickname is't correct
     * recalls all private methods in the same order
     */
    @Override
    public void show() {
        String nickname;
        boolean end = false;

        while (!end) {
            System.out.println();
            System.out.println();
            this.showNicknameRequest();
            nickname = this.getNicknameResponse();
            end = this.checkNicknameResponse(nickname);
        }
    }
}