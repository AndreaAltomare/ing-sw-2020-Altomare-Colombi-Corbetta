package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SetPlayerNumberExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.NumberPlayerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.UnicodeSymbol;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CLINumberPlayerViewer extends CLIStatusViewer {

    private NumberPlayerViewer numberPlayerViewer;

    final int STARTING_SPACE = 7;
    final String WORDS_FIRST_BLOCK = "How many";
    final String WORDS_SECOND_BLOCK = "Players?";
    final int FIELD_LENGTH = (WORDS_FIRST_BLOCK.length() / 2) + WORDS_SECOND_BLOCK.length() + 3;
    final String STYLE_PLAYER_LEFT = ANSIStyle.RED.getEscape();
    final String STYLE_PLAYER_RIGHT = ANSIStyle.PURPLE.getEscape();
    final String FIELD_BACK_COLOR = ANSIStyle.BACK_GREEN.getEscape();
    final String ERROR_COLOR_AND_SYMBOL = ANSIStyle.RED.getEscape() + UnicodeSymbol.X_MARK.getEscape();
    final String CORRECT_COLOR_AND_SYMBOL = ANSIStyle.GREEN.getEscape() + UnicodeSymbol.CHECK_MARK.getEscape();
    final String WRITE_MARK = ANSIStyle.UNDERSCORE.getEscape() + UnicodeSymbol.PENCIL.getEscape() + ANSIStyle.RESET;



    /**
     * Constructor to set correct StatusViewer
     * @param numberPlayerViewer
     */
    public CLINumberPlayerViewer(NumberPlayerViewer numberPlayerViewer) {
        this.numberPlayerViewer = numberPlayerViewer;
    }

    /**
     * Prints number of player request with a cute image
     * example
     */
    private void showRequest() {

        // heads and left block's upper edge
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.print(STYLE_PLAYER_LEFT + "  O  " + ANSIStyle.RESET);

        System.out.print(" ");
        CLIPrintFunction.printRepeatString(STYLE_PLAYER_LEFT, "_", WORDS_FIRST_BLOCK.length());
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", FIELD_LENGTH - WORDS_FIRST_BLOCK.length() - 1);

        System.out.print(STYLE_PLAYER_RIGHT + "  O  " + ANSIStyle.RESET);
        System.out.println();

        // chest and request's first part and seond block's upper part
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.print(STYLE_PLAYER_LEFT + " /|\\/" + ANSIStyle.RESET);

        System.out.print(STYLE_PLAYER_LEFT + "|" +
                            ANSIStyle.UNDERSCORE.getEscape() +  WORDS_FIRST_BLOCK + ANSIStyle.RESET +
                            STYLE_PLAYER_LEFT + "|" + ANSIStyle.RESET);
        CLIPrintFunction.printRepeatString(STYLE_PLAYER_RIGHT, "_", FIELD_LENGTH - WORDS_FIRST_BLOCK.length() - 3);
        System.out.print(" ");

        System.out.print(STYLE_PLAYER_RIGHT + " /|\\ " + ANSIStyle.RESET);
        System.out.println();

        // last body's part and request's second part
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.print(STYLE_PLAYER_LEFT + "| |  " + ANSIStyle.RESET);

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", WORDS_FIRST_BLOCK.length()/2 + 1);
        System.out.print(STYLE_PLAYER_RIGHT + "|" +
                            ANSIStyle.UNDERSCORE.getEscape() + WORDS_SECOND_BLOCK + ANSIStyle.RESET +
                            STYLE_PLAYER_RIGHT + "|"+ ANSIStyle.RESET);

        System.out.print(STYLE_PLAYER_RIGHT + "/ | |" + ANSIStyle.RESET);
        System.out.println();

        // field
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE + 5);
        CLIPrintFunction.printRepeatString(FIELD_BACK_COLOR, " ", FIELD_LENGTH);
        System.out.println();


    }

    /**
     * Prints a symbol to notify where it is possible to write tre response, reads it and returns it if is a integer,
     * if it isn't return -1
     *
     * @return the number of player writing or -1 if the response isn't a integer number
     */
    private int getNumberOfPlayersResponse() {

        final String SPECIFIC_REQUEST = ANSIStyle.UNDERSCORE.getEscape() + UnicodeSymbol.PENCIL.getEscape() +
                                        ANSIStyle.RESET + "(2/3):";

        int response; //set -1 when the response isn't an int
        Scanner input = new Scanner( System.in );

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE + 5 + FIELD_LENGTH/2 - 7);
        System.out.print( SPECIFIC_REQUEST );
        try {
            response = input.nextInt();
        } catch (InputMismatchException e) {
            response = -1;
        }

        return response;
    }

    /**
     * Checks the number of payers, sends it with the correct executer, prints the result of check with
     * a message on standard output and return if the number is correct
     *
     * @param response number of player chosen
     * @return boolean value ( true => correct number, false wrong number )
     */
    private boolean checkNumberOfPlayersResponse(int response ) {
        final String CORRECT_MESSAGE = "The number of players is correctly set";
        final String WRONG_PARAMETER_MESSAGE = "The insert value isn't an acceptable positive integer number, please change it";
        final String WRONG_NUMBER_MESSAGE = "Number of player chosen is not valid, please change it";

        boolean approvedResponse = false;
        SetPlayerNumberExecuter setPlayerNumberExecuter = (SetPlayerNumberExecuter) numberPlayerViewer.getMyExecuters().get("NumberPlayers");

        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        if (response < 0) {
            System.out.print(ERROR_COLOR_AND_SYMBOL + WRONG_PARAMETER_MESSAGE);
        } else {
            try {
                setPlayerNumberExecuter.setNumberOfPlayers( response );
                setPlayerNumberExecuter.doIt();
                System.out.print(CORRECT_COLOR_AND_SYMBOL + CORRECT_MESSAGE);
                approvedResponse = true;
            } catch (WrongParametersException e) {
                System.out.print(ERROR_COLOR_AND_SYMBOL + WRONG_NUMBER_MESSAGE);
            } catch (CannotSendEventException e) {
                System.out.printf(ERROR_COLOR_AND_SYMBOL + "%s", e.toString());
            }
        }
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 3);


        return approvedResponse;
    }

    /**
     * Uses the private methods of this class to ask, read and check the number of player.
     * If the written value is wrong, this methods repeats the call of private methods so
     * the player can choose another value
     */
    @Override
    public void show() {
        int players;
        boolean end = false;

        while (!end) {
            System.out.println();
            System.out.println();
            this.showRequest();
            players = this.getNumberOfPlayersResponse();
            end = this.checkNumberOfPlayersResponse(players);
        }

    }
}
