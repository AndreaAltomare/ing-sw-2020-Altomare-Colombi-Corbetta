package it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewWorker;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.CLIViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.exceptions.NotFoundException;

public abstract class CLISubTurnViewer implements SpecificSubTurnViewer {

    public abstract void show();

    public abstract ViewSubTurn getSubTurn();

    public void setMyCLIStatusViewer(CLIStatusViewer myCLIStatusViewer) {
        /*System.out.printf("\n\t[CLI ERROR: you can't set CLIStatusViewer %s to CLISubTurnViewer %s!]\n",
                                    myCLIStatusViewer.getViewStatus().toString(),
                                    this.getSubTurn().toString());*/ //todo:maybe add the control of CLIStatus in this method
    }

    //TODO: maybe move it in CLIPlayingViewer
    /**
     * Prints the Name, Epithet and Description of all the player's God with the color of player
     */
    protected void showCardsDetails (int STARTING_SPACE) {
        String detailsStyle;
        ViewCard viewCard;
        ViewWorker[] workers;

        for ( ViewPlayer viewPlayer : ViewPlayer.getPlayerList() ) {
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
            try {
                viewCard = viewPlayer.getCard();
                try {
                    workers = viewPlayer.getWorkers();
                    detailsStyle = CLIViewer.getWorkerCLIColor( workers[0].getColor() );
                } catch ( NotFoundException e ) {
                    detailsStyle = "";
                }
                detailsStyle = ANSIStyle.REVERSE.getEscape() + detailsStyle;

                //todo:maybe add god's symbol
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                System.out.printf(detailsStyle + "Name:" + ANSIStyle.RESET + " %s\n\n", viewCard.getName());
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                System.out.printf(detailsStyle + "Epithet:"  + ANSIStyle.RESET + " %s\n\n", viewCard.getEpiteth());
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                System.out.printf(detailsStyle + "Description:" + ANSIStyle.RESET + " %s\n\n", viewCard.getDescription());
            } catch (NotFoundException e) {
                ;
            }
        }

    }

}
