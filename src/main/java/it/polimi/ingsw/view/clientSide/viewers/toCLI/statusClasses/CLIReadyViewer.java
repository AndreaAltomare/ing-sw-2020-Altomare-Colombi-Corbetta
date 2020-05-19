package it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.StatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIStatusViewer;

public class CLIReadyViewer extends CLIStatusViewer {

    final int SCALE_HIGH = 2;
    final int STARTING_SPACE = 3 + 2*SCALE_HIGH;
    final int COLUMNS_THICKNESS = 2;
    final int COLUMNS_HIGH = 4;
    final String UP_TITLE = " _    .   .  . ___  _   _  . .  . .";
    final String MIDDLE_TITLE = "|_   /_\\  |\\ |  |  | | |_| | |\\ | |";
    final String DOWN_TITLE = " _| /   \\ | \\|  |  |_| |\\  | | \\| |";
    final int COLUMNS_NUMBER = ((UP_TITLE.length() - 2) / (2 * COLUMNS_THICKNESS) / 2) + 1;
    final int PEDESTAL_LENGTH = 4 * COLUMNS_THICKNESS * COLUMNS_NUMBER;

    private StatusViewer statusViewer;

    public CLIReadyViewer(StatusViewer statusViewer) {
        this.statusViewer = statusViewer;
    }

    /**
     * Method that use the private methods of this class to print the Title's image
     * example
     */
    @Override
    public void show() {
        this.showTitlePart(UP_TITLE);
        this.showTitlePart(MIDDLE_TITLE);
        this.showTitlePart(DOWN_TITLE);
        this.showPedestal();
        this.showColumns();
        this.showStaircase();
    }

    /**
     * Prints the a part of title String selected using the constant STARTING_SPACE and
     * PEDESTAL_LENGTH to print the string at the correct distant of terminal edge and
     * at the middle of the pedestral
     * @param titlePart part of title's string to print
     */
    private void showTitlePart( String titlePart) {

        this.printRepeatString(" ", STARTING_SPACE + 1);
        if (((PEDESTAL_LENGTH - UP_TITLE.length()) % 2) == 0) {
            this.printRepeatString(" ", (PEDESTAL_LENGTH - 2 - UP_TITLE.length()) / 2);
        } else {
            this.printRepeatString(" ", ((PEDESTAL_LENGTH - 2 - UP_TITLE.length()) / 2) + 1);
        }
        System.out.println(titlePart);
    }

    /**
     * Prints the pedestal under the title using the constant STARTING_SPACE
     * to print it at the correct distant from Terminal's edge
     * example image
     *      _________
     *      \_______/
     */
    private void showPedestal() {

        this.printRepeatString(" ", STARTING_SPACE);
        this.printRepeatString("_", PEDESTAL_LENGTH);
        System.out.println();

        this.printRepeatString(" ", STARTING_SPACE);
        System.out.print("\\");
        this.printRepeatString("_", PEDESTAL_LENGTH - 2);
        System.out.print("/");
        System.out.println();
    }

    /**
     * Prints the columns under the pedestal using the constants COLUMNS_HIGH and COLUMNS_THICKNESS
     * to print the correct numbers and the correct thickness of columns.
     * The lower level of the columns is different because it has the ground
     * example image
     *
     *       []  []
     *     __[]__[]__
     */
    private void showColumns() {

        for (int i = 0; i < (COLUMNS_HIGH - 1); i++) {
            this.printRepeatString(" ", STARTING_SPACE + 2);
            for (int j = 0; j < COLUMNS_NUMBER; j++) {
                this.printRepeatString("[]", COLUMNS_THICKNESS);
                this.printRepeatString(" ", COLUMNS_THICKNESS * 2);
            }
            System.out.println();
        }

        //last level of columns
        this.printRepeatString(" ", STARTING_SPACE);
        System.out.print("__");
        for (int j = 0; j < (COLUMNS_NUMBER - 1); j++) {
            this.printRepeatString("[]", COLUMNS_THICKNESS);
            this.printRepeatString("_", COLUMNS_THICKNESS * 2);
        }
        this.printRepeatString("[]", COLUMNS_THICKNESS);
        System.out.print("__");
        System.out.println();

    }


    /**
     * Prints the Staircase under the columns using the constant STARTING_SPACE and
     * PEDESTAL_LENGTH to center the StairCase.
     * The up part of the first step is printed by showColumns()
     */
    private void showStaircase() {

        for (int i = 0; i < SCALE_HIGH; i++) {
        this.printRepeatString(" ", STARTING_SPACE - 2*i - 2);
        System.out.print("_|");
        this.printRepeatString("_", PEDESTAL_LENGTH + 4*i);
        System.out.print("|_");
        System.out.println();
        }

    }

}
