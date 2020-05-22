package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SetNicknameExecuter;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.LoginViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.Scanner;

public class CLILoginViewer extends CLIStatusViewer {

    private LoginViewer loginViewer;

    final int STARTING_SPACE = 7;
    final String FIRST_PART = "Please, insert your";
    final String SECOND_PART = "Nickname:";

    /**
     * Constructor to set correct StatusViewer
     * @param loginViewer
     */
    public CLILoginViewer(LoginViewer loginViewer) {
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
        this.printRepeatString(" ", STARTING_SPACE + 3);
        this.printRepeatString("_", BLOCK_LENGTH);
        System.out.println();

        // head and block
        this.printRepeatString(" ", STARTING_SPACE - 1);
        System.out.print( "'O " );

        System.out.print("|");
        this.printRepeatString(" ", BLOCK_LENGTH);
        System.out.print("|");
        System.out.println();

        // chest and request's first part
        this.printRepeatString(" ", STARTING_SPACE);
        System.out.print( "/|" );

        System.out.print("|");
        if ( ((BLOCK_LENGTH - FIRST_PART.length()) %2) == 0) {
            this.printRepeatString(" ", (BLOCK_LENGTH - FIRST_PART.length()) /2);
        } else {
            this.printRepeatString(" ", ((BLOCK_LENGTH - FIRST_PART.length()) /2) + 1);
        }
        System.out.printf("%s", FIRST_PART);
        this.printRepeatString(" ", (BLOCK_LENGTH - FIRST_PART.length()) /2);
        System.out.print("|");
        System.out.println();

        // leg, body and request's second part
        this.printRepeatString(" ", STARTING_SPACE - 4);
        System.out.print( "_/\\/ |");

        System.out.print("|");
        if ( ((BLOCK_LENGTH - SECOND_PART.length()) %2) == 0) {
            this.printRepeatString(" ", (BLOCK_LENGTH - SECOND_PART.length()) /2);
        } else {
            this.printRepeatString(" ", ((BLOCK_LENGTH - SECOND_PART.length()) /2) + 1);
        }
        System.out.printf("%s", SECOND_PART);
        this.printRepeatString(" ", (BLOCK_LENGTH - SECOND_PART.length()) /2);
        System.out.print("|");
        System.out.println();

        // other leg and block's down edge
        this.printRepeatString(" ", STARTING_SPACE -2);
        System.out.print("/   ");

        System.out.print("|");
        this.printRepeatString("_", BLOCK_LENGTH);
        System.out.print("|");
        System.out.println();

        // foot
        this.printRepeatString(" ",STARTING_SPACE -4);
        System.out.print( "_/    ");
        if ( ((BLOCK_LENGTH - SECOND_PART.length()) %2) == 0) {
            this.printRepeatString(" ", ((BLOCK_LENGTH - SECOND_PART.length()) /2) - 2);
        } else {
            this.printRepeatString(" ", ((BLOCK_LENGTH - SECOND_PART.length()) /2) - 1);
        }

    }

    /**
     * Print a little signal to notify on standard output where it is possible to write the nickname
     * then return the read value
     * @return the string which is read
     */
    private String getNicknameResponse() {
        String response;
        Scanner input = new Scanner(System.in);

        System.out.print(  ">>");

        response = input.nextLine();

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