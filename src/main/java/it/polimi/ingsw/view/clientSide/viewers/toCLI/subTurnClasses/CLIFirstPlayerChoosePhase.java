package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.FirstPlayerExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.FirstPlayerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.UnicodeSymbol;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CLIFirstPlayerChoosePhase extends CLISubTurnViewer {

    private FirstPlayerViewer firstPlayerViewer;

    private final int STARTING_SPACE = 7;
    private final String ERROR_COLOR_AND_SYMBOL = ANSIStyle.RED.getEscape() + UnicodeSymbol.X_MARK.getEscape();
    private final String CORRECT_COLOR_AND_SYMBOL = ANSIStyle.GREEN.getEscape() + UnicodeSymbol.CHECK_MARK.getEscape();
    private final String WRITE_MARK = ANSIStyle.UNDERSCORE.getEscape() + UnicodeSymbol.PENCIL.getEscape() + ANSIStyle.RESET;

    public CLIFirstPlayerChoosePhase( FirstPlayerViewer firstPlayerViewer) {
        this.firstPlayerViewer = firstPlayerViewer;
    }

    /**
     * prints the request to choose the first player and check the response. Uses the executer of firstPlayerViewer
     * to sets and sends chosen player's name and returns true if the response is correct, or prints an error message
     * and returns false if it isn't
     * @return correct response ? true : false
     */
    private boolean firstPLayerRequest() {
        final String FIRST_PLAYER_REQUEST = "Choose the starting player:";
        final String WRONG_NUMBER_MESSAGE = "The chosen player doesn't exist";
        final String WRONG_INPUT_TYPE_MESSAGE = "The written value isn't correct, please choose a number of the list";
        final int SPACE_BETWEEN_LINES = 1;

        FirstPlayerExecuter firstPlayerExecuter = (FirstPlayerExecuter) firstPlayerViewer.getMySubTurn().getExecuter();
        List<String> playerStringList;
        boolean chosen = false;
        int playerNumber;

        // ask request
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.println(FIRST_PLAYER_REQUEST);
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", SPACE_BETWEEN_LINES);

        // print player list
        playerStringList = firstPlayerExecuter.getPlayerList();
        playerNumber = 1; // start add number to players from number 1
        for (String player : playerStringList) {
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.printf( "%d: " + player + "\n", playerNumber );
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", SPACE_BETWEEN_LINES);
        }

        // read response
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.print(WRITE_MARK);
        try {
            playerNumber = new Scanner(System.in).nextInt();
            if ( playerNumber > 0 && playerNumber <= playerStringList.size() ) {
                firstPlayerExecuter.set( playerStringList.get(playerNumber - 1 ) );
                try {
                    firstPlayerExecuter.doIt();
                    chosen = true;
                } catch (CannotSendEventException e) {
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", SPACE_BETWEEN_LINES);
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                    System.out.printf(ERROR_COLOR_AND_SYMBOL + "%s" + ANSIStyle.RESET, e.toString());
                }
            } else {
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", SPACE_BETWEEN_LINES);
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE );
                System.out.print(ERROR_COLOR_AND_SYMBOL + WRONG_NUMBER_MESSAGE + ANSIStyle.RESET);
            }
        } catch (InputMismatchException e) {
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", SPACE_BETWEEN_LINES);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE );
            System.out.print(ERROR_COLOR_AND_SYMBOL + WRONG_INPUT_TYPE_MESSAGE + ANSIStyle.RESET);
        }

        return  chosen;
    }

    /**
     * Uses some private methods to (maybe print a first player choose image and ) write and read the fist player request
     */
    @Override
    public void show() {
        boolean chosen = false;

        while (!chosen) {

            //CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
            //todo: maybe add private method to see an image
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
            chosen = this.firstPLayerRequest();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);

        }

    }

}
