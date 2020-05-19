package it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces;

import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificStatusViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;

public abstract class CLIStatusViewer implements SpecificStatusViewer {

    private CLIViewer myCLIViewer;

    public void setMyCLIViewer( CLIViewer myCLIViewer ) {
        this.myCLIViewer = myCLIViewer;
    }

    public CLIViewer getMyCLIViewer() {
        return myCLIViewer;
    }

    public abstract void show();

    /**
     * Methods that implements a for cycle to print on standard output
     * a chosen String for a chosen number of times
     *
     * @param string string to print
     * @param repeatsNumber number of times to print the chosen string
     */
    public void printRepeatString(String string, int repeatsNumber) {
        for ( int i = 0; i < repeatsNumber; i++) {
            System.out.printf("%s", string);
        }
    }

}
