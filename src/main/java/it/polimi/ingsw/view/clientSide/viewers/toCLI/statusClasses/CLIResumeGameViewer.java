package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.ResumingExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.RequestResumingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.CLISymbols;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.UnicodeSymbol;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.exceptions.AlreadySetException;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CLIResumeGameViewer extends CLIStatusViewer {

    private RequestResumingViewer requestResumingViewer;

    private final int STARTING_SPACE = 7;
    private final String ERROR_COLOR_AND_SYMBOL = ANSIStyle.RED.getEscape() + UnicodeSymbol.X_MARK.getEscape();
    private final String CORRECT_COLOR_AND_SYMBOL = ANSIStyle.GREEN.getEscape() + UnicodeSymbol.CHECK_MARK.getEscape();
    private final String WRITE_MARK = ANSIStyle.UNDERSCORE.getEscape() + UnicodeSymbol.PENCIL.getEscape() + ANSIStyle.RESET;


    public CLIResumeGameViewer(RequestResumingViewer requestResumingViewer) {
        this.requestResumingViewer = requestResumingViewer;
    }

    /**
     * Prints a little image to represent the phase
     * example //todo: add image
     */
    private void showRestartImage() {
        final String MACHINERY_BACK_COLOR = ANSIStyle.BACK_GREY.getEscape();
        final String MACHINERY_COLOR = ANSIStyle.GREY.getEscape();
        final String CAB_BACK_COLOR = ANSIStyle.BACK_DIFFERENT_BLUE.getEscape();
        final String WORKER_COLOR = "";
        final String BLOCK_COLOR = ANSIStyle.GREY.getEscape();
        final int CAB_LENGTH = 3;
        final int MACHINERY_LENGTH = CAB_LENGTH + 2;

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 1);
        // arm's top of machinery
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE + 1 + MACHINERY_LENGTH + 3);
        System.out.println( MACHINERY_COLOR + "_" + ANSIStyle.RESET);

        // arm's upper part of machinery
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE + 1 + MACHINERY_LENGTH + 2);
        System.out.println( MACHINERY_COLOR + "//\\" + ANSIStyle.RESET);

        // machinery and cabin's upper part, then arm's machinery
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE + 1 );
        CLIPrintFunction.printRepeatString(MACHINERY_BACK_COLOR, " ", MACHINERY_LENGTH - CAB_LENGTH);
        CLIPrintFunction.printAtTheMiddle(CAB_BACK_COLOR + WORKER_COLOR, "o", 1, CAB_LENGTH);
        System.out.println(MACHINERY_COLOR + " //  \\_" + ANSIStyle.RESET);

        // machinery and cabin's down part, then arm's machinery, demolition ball and block's third level
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE + 1 );
        CLIPrintFunction.printRepeatString(MACHINERY_BACK_COLOR, " ", MACHINERY_LENGTH - CAB_LENGTH);
        CLIPrintFunction.printAtTheMiddle(CAB_BACK_COLOR + WORKER_COLOR, " |\\", 3, CAB_LENGTH);
        System.out.print(MACHINERY_COLOR + "//   (_) " + ANSIStyle.RESET);
        System.out.println(BLOCK_COLOR + CLISymbols.BLOCK.getMiddleRepresentation() + ANSIStyle.RESET);

        // machinery's down part, then arm's machinery, block's second level
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE + 1 );
        CLIPrintFunction.printRepeatString(MACHINERY_BACK_COLOR, " ", MACHINERY_LENGTH );
        System.out.print(MACHINERY_COLOR + "/" + ANSIStyle.RESET);
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", 7);
        System.out.println(BLOCK_COLOR + CLISymbols.BLOCK.getMiddleRepresentation() + ANSIStyle.RESET + " \"");

        // machinery's track, block's first level
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE );
        System.out.print(MACHINERY_COLOR + "(" + ANSIStyle.RESET);
        CLIPrintFunction.printRepeatString(MACHINERY_COLOR, "_", MACHINERY_LENGTH );
        System.out.print(MACHINERY_COLOR + ")" + ANSIStyle.RESET);
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", 6);
        System.out.println(BLOCK_COLOR + CLISymbols.BLOCK.getMiddleRepresentation() + ANSIStyle.RESET + " \"");
    }

    /**
     * Prints and check the restart request: returns true and do the correct command if the command selected is correct,
     * prints a wrong message and returns false if it isn't
     * @return true if command == 1 || command == 2, else false
     */
    private boolean restartRequest() {
        final String RESTART_REQUEST = "There is a saved game, do you want delete it?";
        final String RESTART_COMMAND = "Yes, start another game!";
        final String CONTINUE_COMMAND = "No, continue saved game!";
        final String WRONG_COMMAND_MESSAGE = "The chosen command doesn't exist";
        final int LINE_BETWEEN_STRING = 1;

        ResumingExecuter resumingExecuter = (ResumingExecuter) requestResumingViewer.getMyExecuters().get("Resume");
        boolean commandSelected = false;
        int command;

        // ask request
        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.println(RESTART_REQUEST);

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", LINE_BETWEEN_STRING);

        // show command
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.println("1: " + RESTART_COMMAND);
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", LINE_BETWEEN_STRING);

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.println("2: " + CONTINUE_COMMAND);
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", LINE_BETWEEN_STRING);

        // read command
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.print(WRITE_MARK);
        try {
            command = new Scanner(System.in).nextInt();
            switch (command) {
                case 1:
                    try {
                        resumingExecuter.setResume(false);
                    } catch (AlreadySetException ignored) {
                    }
                    try {
                        resumingExecuter.doIt();
                        commandSelected = true;
                    } catch (CannotSendEventException e) {
                        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", LINE_BETWEEN_STRING);
                        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                        System.out.printf(ERROR_COLOR_AND_SYMBOL + "%s" + ANSIStyle.RESET, e.toString());
                    }
                    break;
                case 2:
                    try {
                        resumingExecuter.setResume(true);
                    } catch (AlreadySetException ignored) {
                    }
                    try {
                        resumingExecuter.doIt();
                        commandSelected = true;
                    } catch (CannotSendEventException e) {
                        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", LINE_BETWEEN_STRING);
                        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                        System.out.printf(ERROR_COLOR_AND_SYMBOL + "%s" + ANSIStyle.RESET, e.toString());
                    }
                    break;
                default:
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", LINE_BETWEEN_STRING);
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                    System.out.print(ERROR_COLOR_AND_SYMBOL + WRONG_COMMAND_MESSAGE + ANSIStyle.RESET);
            }
        } catch (InputMismatchException e) {
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", LINE_BETWEEN_STRING);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.print(ERROR_COLOR_AND_SYMBOL + WRONG_COMMAND_MESSAGE + ANSIStyle.RESET);
        }

        return  commandSelected;

    }

    /**
     * Uses private methods to print a resume image and the resume request until the asked request is accepted
     */
    @Override
    public void show() {
        boolean endResume = false;

        while ( !endResume ) {

            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
            this.showRestartImage();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 1);
            endResume = this.restartRequest();
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);

        }

    }

}
