package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.ResumingExecuter;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.RequestResumingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.CLISymbols;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;
import it.polimi.ingsw.view.exceptions.AlreadySetException;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class that represents the <code>CLIStatusViewer</code> ResumeGame on the CLI
 * using methods of <code>CLIPrintFunction</code> and <code>ANSIStyle</code>
 *
 * @see CLIStatusViewer
 * @see CLIPrintFunction
 * @see ANSIStyle
 * @author Marco
 */
public class CLIResumeGameViewer extends CLIStatusViewer {

    private RequestResumingViewer requestResumingViewer;

    /**
     * Constructor to set correct <code>StatusViewer</code>
     *
     * @see RequestResumingViewer
     * @param requestResumingViewer  <code>StatusViewer</code> linked at this class
     */
    public CLIResumeGameViewer(RequestResumingViewer requestResumingViewer) {
        this.requestResumingViewer = requestResumingViewer;
    }

    /**
     * Prints a little image to represent the phase
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
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE + 1 + MACHINERY_LENGTH + 3);
        System.out.println( MACHINERY_COLOR + "_" + ANSIStyle.RESET);

        // arm's upper part of machinery
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE + 1 + MACHINERY_LENGTH + 2);
        System.out.println( MACHINERY_COLOR + "//\\" + ANSIStyle.RESET);

        // machinery and cabin's upper part, then machinery's arm
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE + 1 );
        CLIPrintFunction.printRepeatString(MACHINERY_BACK_COLOR, " ", MACHINERY_LENGTH - CAB_LENGTH);
        CLIPrintFunction.printAtTheMiddle(CAB_BACK_COLOR + WORKER_COLOR, "o", 1, CAB_LENGTH);
        System.out.println(MACHINERY_COLOR + " //  \\_" + ANSIStyle.RESET);

        // machinery and cabin's down part, then machinery's arm, demolition ball and block's third level
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE + 1 );
        CLIPrintFunction.printRepeatString(MACHINERY_BACK_COLOR, " ", MACHINERY_LENGTH - CAB_LENGTH);
        CLIPrintFunction.printAtTheMiddle(CAB_BACK_COLOR + WORKER_COLOR, " |\\", 3, CAB_LENGTH);
        System.out.print(MACHINERY_COLOR + "//   (_) " + ANSIStyle.RESET);
        System.out.println(BLOCK_COLOR + CLISymbols.BLOCK.getMiddleRepresentation() + ANSIStyle.RESET);

        // machinery's down part, then machinery's arm, block's second level
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE + 1 );
        CLIPrintFunction.printRepeatString(MACHINERY_BACK_COLOR, " ", MACHINERY_LENGTH );
        System.out.print(MACHINERY_COLOR + "/" + ANSIStyle.RESET);
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", 7);
        System.out.println(BLOCK_COLOR + CLISymbols.BLOCK.getMiddleRepresentation() + ANSIStyle.RESET + " \"");

        // machinery's track, block's first level
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE );
        System.out.print(MACHINERY_COLOR + "(" + ANSIStyle.RESET);
        CLIPrintFunction.printRepeatString(MACHINERY_COLOR, "_", MACHINERY_LENGTH );
        System.out.print(MACHINERY_COLOR + ")" + ANSIStyle.RESET);
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", 6);
        System.out.println(BLOCK_COLOR + CLISymbols.BLOCK.getMiddleRepresentation() + ANSIStyle.RESET + " \"");
    }

    /**
     * Prints and check the restart request: returns true and do the correct command if the command selected is correct,
     * prints a wrong message and returns false if it isn't. Notifies the result with a message printed
     *
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
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.println(RESTART_REQUEST);

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", LINE_BETWEEN_STRING);

        // show command
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.println("1: " + RESTART_COMMAND);
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", LINE_BETWEEN_STRING);

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.println("2: " + CONTINUE_COMMAND);
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", LINE_BETWEEN_STRING);

        // read command
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.print(CLIPrintFunction.WRITE_MARK);
        try {
            command = new Scanner(System.in).nextInt();
            switch (command) {
                case 1:
                    try {
                        resumingExecuter.setResume(false);
                    } catch (AlreadySetException ignored) {
                    }
                    try {
                        commandSelected = true;
                        resumingExecuter.doIt();
                    } catch (CannotSendEventException e) {
                        CLIPrintFunction.printError(e.getErrorMessage());
                    }
                    break;
                case 2:
                    try {
                        resumingExecuter.setResume(true);
                    } catch (AlreadySetException ignored) {
                    }
                    try {
                        commandSelected = true;
                        resumingExecuter.doIt();
                    } catch (CannotSendEventException e) {
                        CLIPrintFunction.printError(e.getErrorMessage());
                    }
                    break;
                default:
                    CLIPrintFunction.printError(WRONG_COMMAND_MESSAGE);
            }
        } catch (InputMismatchException e) {
            CLIPrintFunction.printError(WRONG_COMMAND_MESSAGE);
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
