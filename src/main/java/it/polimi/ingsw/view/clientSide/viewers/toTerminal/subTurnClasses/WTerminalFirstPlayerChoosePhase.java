package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.FirstPlayerExecuter;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.FirstPlayerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Class that represents the <code>WTerminalSubTurnViewer</code> FirstPlayer on the Windows Terminal
 * using methods of <code>PrintFunction</code>
 *
 * @see WTerminalSubTurnViewer
 * @see PrintFunction
 * @author Marco
 */
public class WTerminalFirstPlayerChoosePhase extends WTerminalSubTurnViewer {

    private FirstPlayerViewer firstPlayerViewer;

    /**
     * Constructor to set the correctly <code>SubTurnViewer</code>
     *
     * @see FirstPlayerViewer
     * @param firstPlayerViewer <code>SubTurnViewer</code> linked at this class
     */
    public WTerminalFirstPlayerChoosePhase( FirstPlayerViewer firstPlayerViewer ) {
        this.firstPlayerViewer = firstPlayerViewer;
    }

    /**
     * prints the request to choose the first player and check the response. Uses the executer of firstPlayerViewer
     * to sets and sends chosen player's name and returns true if the response is correct, or prints an error message
     * and returns false if it isn't
     *
     * @return correct response and set ? true : false
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
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.println(FIRST_PLAYER_REQUEST);
        PrintFunction.printRepeatString("\n", SPACE_BETWEEN_LINES);

        // print player list
        playerStringList = firstPlayerExecuter.getPlayerList();
        playerNumber = 1; // start add number to players from number 1
        for (String player : playerStringList) {
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.printf( "%d: " + player + "\n", playerNumber );
            PrintFunction.printRepeatString("\n", SPACE_BETWEEN_LINES);
            playerNumber++;
        }

        // read response
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print(">>");
        try {
            playerNumber = new Scanner(System.in).nextInt();
            if ( playerNumber > 0 && playerNumber <= playerStringList.size() ) {
                firstPlayerExecuter.set( playerStringList.get(playerNumber - 1 ) );
                try {
                    chosen = true;
                    firstPlayerExecuter.doIt();
                } catch (CannotSendEventException e) {
                    PrintFunction.printError(e.getErrorMessage());
                }
            } else {
                PrintFunction.printError(WRONG_NUMBER_MESSAGE);
            }
        } catch (InputMismatchException e) {
            PrintFunction.printError(WRONG_INPUT_TYPE_MESSAGE);
        }


        return  chosen;

    }

    /**
     * Uses some private methods to write and read the fist player request
     */
    @Override
    public void show() {
        boolean chosen = false;

        while (!chosen) {

            PrintFunction.printRepeatString("\n", 2);
            chosen = this.firstPLayerRequest();
            PrintFunction.printRepeatString("\n", 2);

        }

    }

}
