package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SetNicknameExecuter;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.LoginViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.Scanner;

public class CLILoginViewer extends CLIStatusViewer {

    private LoginViewer loginViewer;

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
        final int DISTANT_FROM_EDGE = 3;
        final String FIRST_PART = "Please, insert your";
        final String SECOND_PART = "Nickname:";
        final String DROP_COLOR = ANSIStyle.DIFFERENT_BLUE.getEscape();
        final String HUMAN_COLOR = ANSIStyle.RESET;
        final String BLOCK_BACK_COLOR = ANSIStyle.BACK_GREY.getEscape();
        final String REQUEST_COLOR = ANSIStyle.BLUE.getEscape();
        final int BLOCK_LENGTH; //after initialization it becomes constant

        if ( FIRST_PART.length() > SECOND_PART.length() ) {
            BLOCK_LENGTH = FIRST_PART.length() + DISTANT_FROM_EDGE*2;
        } else {
            BLOCK_LENGTH = SECOND_PART.length() + DISTANT_FROM_EDGE*2;
        }


        // head and upper block's part
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE - 1);
        System.out.print( DROP_COLOR + "'" );
        System.out.print( HUMAN_COLOR + "O " );

        CLIPrintFunction.printRepeatString(BLOCK_BACK_COLOR, " ", BLOCK_LENGTH);
        System.out.println();

        // chest and request's first part
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.print( HUMAN_COLOR + "/|" );

        CLIPrintFunction.printAtTheMiddle(BLOCK_BACK_COLOR + REQUEST_COLOR,
                                            FIRST_PART,
                                            FIRST_PART.length(),
                                            BLOCK_LENGTH);
        System.out.println();

        // leg, body and request's second part
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE - 4);
        System.out.print( HUMAN_COLOR + "_/\\/ |");

        CLIPrintFunction.printAtTheMiddle(BLOCK_BACK_COLOR + REQUEST_COLOR,
                                                SECOND_PART,
                                                SECOND_PART.length(),
                                                BLOCK_LENGTH);
        System.out.println();

        // other leg and block's down edge
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE - 2);
        System.out.print( HUMAN_COLOR + "/   ");

        CLIPrintFunction.printRepeatString( BLOCK_BACK_COLOR, " ", BLOCK_LENGTH);
        System.out.println();

        // foot
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE - 4);
        System.out.print( HUMAN_COLOR + "_/    ");
        if ( ((BLOCK_LENGTH - SECOND_PART.length()) %2) == 0) {
            CLIPrintFunction.printRepeatString( ANSIStyle.RESET, " ", ((BLOCK_LENGTH - SECOND_PART.length()) /2) - 2);
        } else {
            CLIPrintFunction.printRepeatString( ANSIStyle.RESET, " ", ((BLOCK_LENGTH - SECOND_PART.length()) /2) - 1);
        }

    }

    /**
     * Prints a little signal to notify on standard output where it is possible to write the nickname
     * then returns the read value if its length is 8 or minus, or its first eight char if it isn't
     *
     * @return the string which is read if its length is 8 or minus. its first eight char if it isn't
     */
    private String getNicknameResponse() {
        final int maxLength = 11;
        String response;
        Scanner input = new Scanner(System.in);

        System.out.printf(  CLIPrintFunction.WRITE_MARK + "(Max %d):", maxLength);

        response = input.nextLine();
        if (response.length() > maxLength) {
            response = response.substring(0, maxLength);
        }

        return response;
    }

    /**
     * Checks if the string is a possible value and sends it with the correct executer,
     * then it notifies with a message on standard output if the nickname is correct or not
     *
     * @param response nickname chosen by player
     * @return boolean value ( true => correct nickname, false => wrong nickname
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
            CLIPrintFunction.printCheck(CORRECT_MESSAGE);
        } catch (WrongParametersException e) {
            if (!e.getMessage().equals("") ) {
                wrongParameterMessage = e.getMessage();
            }
            CLIPrintFunction.printError(wrongParameterMessage);
        } catch (CannotSendEventException e) {
            CLIPrintFunction.printError(e.getErrorMessage());
        }

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
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
            this.showNicknameRequest();
            nickname = this.getNicknameResponse();
            end = this.checkNicknameResponse(nickname);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
        }
    }
}
