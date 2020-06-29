package it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses;

import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.ResumingExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewStatus;
import it.polimi.ingsw.view.clientSide.viewers.statusViewers.RequestResumingViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;
import it.polimi.ingsw.view.exceptions.AlreadySetException;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class WTerminalResumeGameViewer extends WTerminalStatusViewer {

    private RequestResumingViewer requestResumingViewer;

    private final int STARTING_SPACE = 7;

    public WTerminalResumeGameViewer( RequestResumingViewer requestResumingViewer) {
        this.requestResumingViewer = requestResumingViewer;
    }

    /**
     * Prints a little image to represent the phase
     * example //todo: add image
     */
    private void showRestartImage() {
        // todo: add print code image
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
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.println(RESTART_REQUEST);

        PrintFunction.printRepeatString("\n", LINE_BETWEEN_STRING);

        // show command
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.println("1: " + RESTART_COMMAND);
        PrintFunction.printRepeatString("\n", LINE_BETWEEN_STRING);

        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.println("2: " + CONTINUE_COMMAND);
        PrintFunction.printRepeatString("\n", LINE_BETWEEN_STRING);

        // read command
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
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
                        resumingExecuter.doIt();
                        commandSelected = true;
                    } catch (CannotSendEventException e) {
                        PrintFunction.printRepeatString("\n", LINE_BETWEEN_STRING);
                        PrintFunction.printRepeatString(" ", STARTING_SPACE);
                        System.out.printf(">< " + "%s", e.toString());
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
                        PrintFunction.printRepeatString("\n", LINE_BETWEEN_STRING);
                        PrintFunction.printRepeatString(" ", STARTING_SPACE);
                        System.out.printf(">< " + "%s", e.toString());
                    }
                    break;
                default:
                    PrintFunction.printRepeatString("\n", LINE_BETWEEN_STRING);
                    PrintFunction.printRepeatString(" ", STARTING_SPACE);
                    System.out.println(">< " + WRONG_COMMAND_MESSAGE);
            }
        } catch (InputMismatchException e) {
            PrintFunction.printRepeatString("\n", LINE_BETWEEN_STRING);
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println(">< " + WRONG_COMMAND_MESSAGE);
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
