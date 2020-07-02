package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.ResumingExecuter;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.RequestResumingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;
import it.polimi.ingsw.view.exceptions.AlreadySetException;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class that represents the <code>WTerminalStatusViewer</code> ResumeGame on the Windows Terminal
 * using methods of <code>PrintFunction</code>
 *
 * @see WTerminalStatusViewer
 * @see PrintFunction
 * @author Marco
 */
public class WTerminalResumeGameViewer extends WTerminalStatusViewer {

    private RequestResumingViewer requestResumingViewer;

    /**
     * Constructor to set correct <code>StatusViewer</code>
     *
     * @see RequestResumingViewer
     * @param requestResumingViewer  <code>StatusViewer</code> linked at this class
     */
    public WTerminalResumeGameViewer( RequestResumingViewer requestResumingViewer) {
        this.requestResumingViewer = requestResumingViewer;
    }

    /**
     * Prints a little image to represent the phase
     */
    private void showRestartImage() {
        final int MACHINERY_LENGTH = 6;

        PrintFunction.printRepeatString("\n", 1);
        // arm's top of machinery
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE + MACHINERY_LENGTH + 3);
        System.out.println("_");

        // arm's upper part of machinery
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE + MACHINERY_LENGTH + 2);
        System.out.println( "//\\" );

        // machine's upper part, machinery's arm and third block's upper part
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE + 1);
        PrintFunction.printRepeatString("_", MACHINERY_LENGTH - 2);
        PrintFunction.printRepeatString(" ", 2);
        System.out.println("//  \\_    __");

        // machinery's down part, cabin,  then arm's machinery, demolition ball and block's third level
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print("/");
        PrintFunction.printRepeatString(" ", MACHINERY_LENGTH - 4);
        System.out.print("|_\\//   (_)");
        System.out.println(" _|__|");

        // machinery's down part, then arm's machinery, block's second level
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print("\\");
        PrintFunction.printRepeatString("_", MACHINERY_LENGTH - 2);
        System.out.print("//");
        PrintFunction.printRepeatString(" ", 5);
        System.out.println(" _|__|  \"");

        // machinery's track, block's first level
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print("(");
        PrintFunction.printRepeatString("_", MACHINERY_LENGTH - 1);
        System.out.print(")");
        PrintFunction.printRepeatString(" ", 5);
        System.out.println("|__|  \"");
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
        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.println(RESTART_REQUEST);

        PrintFunction.printRepeatString("\n", LINE_BETWEEN_STRING);

        // show command
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.println("1: " + RESTART_COMMAND);
        PrintFunction.printRepeatString("\n", LINE_BETWEEN_STRING);

        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.println("2: " + CONTINUE_COMMAND);
        PrintFunction.printRepeatString("\n", LINE_BETWEEN_STRING);

        // read command
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print(">>");
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
                        PrintFunction.printError(e.getErrorMessage());
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
                        PrintFunction.printError(e.getErrorMessage());
                    }
                    break;
                default:
                    PrintFunction.printError(WRONG_COMMAND_MESSAGE);
            }
        } catch (InputMismatchException e) {
            PrintFunction.printError(WRONG_COMMAND_MESSAGE);
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

            PrintFunction.printRepeatString("\n", 2);
            this.showRestartImage();
            PrintFunction.printRepeatString("\n", 1);
            endResume = this.restartRequest();
            PrintFunction.printRepeatString("\n", 2);

        }

    }

}
