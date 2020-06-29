package it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.interfaces.SpecificSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalStatusViewer;
import it.polimi.ingsw.view.exceptions.NotFoundException;

public abstract class WTerminalSubTurnViewer implements SpecificSubTurnViewer {

    public abstract void show();

    /**
     * Prints the Name, Epithet and Description of all the player's God
     */
    protected void showCardsDetails ( int STARTING_SPACE ) {
        ViewCard viewCard;

        for (ViewPlayer viewPlayer : ViewPlayer.getPlayerList()) {
            System.out.println();
            System.out.println();
            try {
                viewCard = viewPlayer.getCard();
                //todo:maybe add god's symbol
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.printf("Name: %s\n", viewCard.getName());
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.printf("Epithet: %s\n", viewCard.getEpiteth());
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.printf("Description: %s\n", viewCard.getDescription());
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
