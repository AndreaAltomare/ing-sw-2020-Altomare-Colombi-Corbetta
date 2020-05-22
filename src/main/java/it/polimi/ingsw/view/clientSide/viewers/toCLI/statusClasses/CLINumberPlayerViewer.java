package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.SetPlayerNumberExecuter;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.NumberPlayerViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.PrintFunction;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CLINumberPlayerViewer extends CLIStatusViewer {

    private NumberPlayerViewer numberPlayerViewer;

    final int START_SPACE = 5;
    final int DISTANCE_IN_BLOCK = 2;
    final String WORDS_FIRST_BLOCK = "How many";
    final String WORDS_SECOND_BLOCK = "Players?";


    /**
     * Constructor to set correct StatusViewer
     * @param numberPlayerViewer
     */
    public CLINumberPlayerViewer( NumberPlayerViewer numberPlayerViewer) {
        this.numberPlayerViewer = numberPlayerViewer;
    }

    /**
     * Prints request's first part to ask the number of players
     * writing it in a block with a worker ( prepares second block's upper edge too)
     * example image
     *       ____________
     *   \O/|            |
     *    \ |  Request1  |
     *     \|____________|__
     */
    private void showFirstRequestPart(){

        // block's upper edge
        PrintFunction.printRepeatString(" ", START_SPACE + 1);
        PrintFunction.printRepeatString("_", WORDS_FIRST_BLOCK.length() + 2*DISTANCE_IN_BLOCK);
        System.out.println();

        //head and arm
        PrintFunction.printRepeatString(" ", START_SPACE - 3);
        System.out.print("\\O/|");
        PrintFunction.printRepeatString(" ", WORDS_FIRST_BLOCK.length() + 2*DISTANCE_IN_BLOCK);
        System.out.println("|");

        //request's first part
        PrintFunction.printRepeatString(" ", START_SPACE - 2);
        System.out.print("\\ |");
        PrintFunction.printAtTheMiddle(WORDS_FIRST_BLOCK, WORDS_FIRST_BLOCK.length() + 2*DISTANCE_IN_BLOCK);
        System.out.println("|");

        // leg and block's down edge and second block's upper edge
        PrintFunction.printRepeatString(" ", START_SPACE - 1);
        System.out.print("\\|");
        PrintFunction.printRepeatString("_", WORDS_FIRST_BLOCK.length() + 2*DISTANCE_IN_BLOCK);
        System.out.print("|");
        PrintFunction.printRepeatString("_", WORDS_SECOND_BLOCK.length() - 1 - (WORDS_FIRST_BLOCK.length() - 3));
        System.out.println();

    }

    /**
     * Prints request's second part to ask the number of players
     * writing it in a block with two worker
     * example image
     *
     *         |            |
     *       O |  Request2  | O
     *       |\|____________|/|
     *      / \              / \
     */
    private void showSecondRequestPart() {


        // free part of block over second request's part
        PrintFunction.printRepeatString(" ", START_SPACE + 3);
        System.out.print("|");
        PrintFunction.printRepeatString(" ", WORDS_SECOND_BLOCK.length() + 2*DISTANCE_IN_BLOCK);
        System.out.println("|");

        // heads and second words
        PrintFunction.printRepeatString(" ", START_SPACE);
        System.out.print(" O |");
        PrintFunction.printAtTheMiddle(WORDS_SECOND_BLOCK, WORDS_SECOND_BLOCK.length() + 2*DISTANCE_IN_BLOCK);
        System.out.println("| O ");

        // bodies
        PrintFunction.printRepeatString(" ", START_SPACE);
        System.out.print(" |\\|");
        PrintFunction.printRepeatString("_", WORDS_SECOND_BLOCK.length() + 2*DISTANCE_IN_BLOCK);
        System.out.println("|/| ");

        // legs
        PrintFunction.printRepeatString(" ", START_SPACE);
        System.out.print("/ \\");
        PrintFunction.printRepeatString(" ", WORDS_SECOND_BLOCK.length() + 2*DISTANCE_IN_BLOCK + 2);
        System.out.println("/ \\");

    }


    /**
     * Prints a sYmbol to notify where it is possible to write tre response, reads it and returns it if is a integer,
     * if it isn't return -1
     *
     * @return the number of player writing or -1 if the response isn't a integer number
     */
    private int getNumberOfPlayersResponse() {

        final String SPECIFIC_REQUEST = "Number(2/3):";

        int response; //set -1 when the response isn't an int
        Scanner input = new Scanner( System.in );

        PrintFunction.printRepeatString(" ", START_SPACE + 2 +DISTANCE_IN_BLOCK - (SPECIFIC_REQUEST.length() + 1)/2);
        System.out.print( ">>" + SPECIFIC_REQUEST );
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
        boolean approvedResponse = false;
        SetPlayerNumberExecuter setPlayerNumberExecuter = (SetPlayerNumberExecuter) numberPlayerViewer.getMyExecuters().get("NumberPlayers");

        if (response < 0) {
            System.out.println( "\n\t" +
                                ">< The insert value isn't an acceptable positive integer number,\n" +
                                "\t   please change it");
        } else {
            try {
                setPlayerNumberExecuter.setNumberOfPlayers( response );
                setPlayerNumberExecuter.doIt();
                System.out.println( "\n\t" +
                                    "  /" + "\n\t" +
                                    "\\/ The number of players is correctly set");
                approvedResponse = true;
            } catch (WrongParametersException e) {
                System.out.println( "\n\t" +
                                    ">< Number of player chosen is not valid, please change it");
               } catch (CannotSendEventException e) {
                System.out.printf(  "\n\t" +
                                    ">< %s" +
                                    "\n", e.toString());
               }
        }
        System.out.println();


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
            this.showFirstRequestPart();
            this.showSecondRequestPart();
            players = this.getNumberOfPlayersResponse();
            end = this.checkNumberOfPlayersResponse(players);
        }

    }
}
