package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.CardSelectionExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.GodSymbols;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.WTerminalGamePreparationViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.InputMismatchException;
import java.util.Scanner;

//todo: rivedere intera classe per sistemare uso degli executer
public class WTerminalSelectMyCardPhase extends WTerminalSubTurnViewer {

    private WTerminalGamePreparationViewer myWTerminalStatusViewer = null;
    private ViewSubTurn viewSubTurn = ViewSubTurn.SELECTCARD;
    private CardSelectionExecuter cardSelectionExecuter = new CardSelectionExecuter();
    private CardSelection cardSelection;

    private final int STARTING_SPACE = 7;
    private final int GOD_SYMBOL_SPACE = GodSymbols.getMaxRepresentationLength() + 2;
    private final int GOD_NAME_SPACE = GodSymbols.getMaxNameLength() + 2;


    public WTerminalSelectMyCardPhase(CardSelection cardSelection) {
        this.cardSelection = cardSelection;
    }

    /**
     * Uses private methods to show a list with god cards to the player as long as he chooses his god card
     */
    private ViewCard showSelectInterface() {
        ViewCard selectedCard = null;

        if ( cardSelection.getCardList().size() != 1) {
            while (selectedCard == null) {
                this.printCardList();
                selectedCard = this.showCardAndSel(this.showGodRequest());
            }
        } else {
            selectedCard = this.printLastCard();
        }

        return selectedCard;
    }

    /**
     * Prints a numbered list with Gods Symbols and god
     */
    private void printCardList() {
        GodSymbols godSymbols;
        int cardNumber = 1;

        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.println("Card List:");
        for (ViewCard viewCard : cardSelection.getCardList()) {

            try {
                godSymbols = GodSymbols.searchGodSymbol(viewCard.getName());

                System.out.println();
                // first line
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.print(" ");
                PrintFunction.printAtTheMiddle( godSymbols.getUpRepresentation(), GOD_SYMBOL_SPACE );
                PrintFunction.printRepeatString(" ", GOD_NAME_SPACE);
                System.out.println();
                // second line
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.print(" ");
                PrintFunction.printAtTheMiddle( godSymbols.getMiddleRepresentation(), GOD_SYMBOL_SPACE );
                PrintFunction.printRepeatString(" ", GOD_NAME_SPACE);
                System.out.println();
                // third line
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.printf("%d", cardNumber);
                PrintFunction.printAtTheMiddle( godSymbols.getDownRepresentation(), GOD_SYMBOL_SPACE );
                PrintFunction.printAtTheMiddle( viewCard.getName(), GOD_NAME_SPACE);
                System.out.println();

                cardNumber++;

            } catch (NotFoundException e) {
                e.printStackTrace();
            }

        }
        System.out.println();

    }

    /**
     * Shows a request to see god's description and checks it
     * @return
     */
    private int showGodRequest() {
        int numberSelected;

        do {
            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println("Please, insert the number of god which you want to see:");
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.print(">>");
            try {
                numberSelected = new Scanner(System.in).nextInt();
                if ( (numberSelected <= 0 || numberSelected > cardSelection.getCardList().size()) ) {
                    PrintFunction.printRepeatString(" ", STARTING_SPACE );
                    System.out.println(">< The chosen number isn't in the list, please change it");
                }
            } catch (InputMismatchException e) {
                numberSelected = -1;
                PrintFunction.printRepeatString(" ", STARTING_SPACE );
                System.out.println(">< The chosen value isn't correct, please change it");
            }
        } while ( (numberSelected <= 0 || numberSelected > cardSelection.getCardList().size() ));
        System.out.println();

        return numberSelected;
    }

    /**
     * Shows card's specifics and the request of card's selection as long as player's response is accepted
     * @param godCardNumber
     */
    private ViewCard showCardAndSel( int godCardNumber) {
        final String selectionRequest = "Do you want to use this card?";
        final String inputChoose = ">>( 0: No/ 1: Yes ):";
        boolean correctResponse;
        int response;
        ViewCard seeCard = null;

        try {
            // show card
            System.out.println();
            seeCard = cardSelection.getCardList().get(godCardNumber - 1);
            do {
                //todo:maybe add god's symbol
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.printf("Name: %s\n\n", seeCard.getName());
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.printf("Epithet: %s\n\n", seeCard.getEpiteth());
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.printf("Description: %s\n\n", seeCard.getDescription());

                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.println( selectionRequest );
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.print( inputChoose );
                try {
                    response = new Scanner( System.in ).nextInt();
                } catch ( InputMismatchException e) {
                    response = -1;
                }
                if ( response != 1) {
                    if ( response != 0) {
                        correctResponse = false;
                        PrintFunction.printRepeatString(" ", STARTING_SPACE);
                        System.out.println( ">< The chosen value isn't correct, please change it");
                    } else {
                        correctResponse = true;
                        seeCard = null;
                    }
                } else {
                    correctResponse = true;
                }
            } while ( !correctResponse );
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        System.out.println();

        return seeCard;
    }

    private ViewCard printLastCard() {
        final String lastCardMessage = "This is your card";
        ViewCard seeCard = null;


        try {
            // show card
            System.out.println();
            seeCard = cardSelection.getCardList().get(1);
            //todo:maybe add god's symbol
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.printf("Name: %s\n\n", seeCard.getName());
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.printf("Epithet: %s\n\n", seeCard.getEpiteth());
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.printf("Description: %s\n\n", seeCard.getDescription());

            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println(lastCardMessage);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        System.out.println();

        return seeCard;
    }

    /**
     * Prints a waiting message to players who can't choose
     */
    private void showWaitMessage() {
        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.println("A player is choosing his card, please wait");
        System.out.println();
        //todo: maybe to do an little animation like in CLIWaitingStatus
    }

    @Override
    public void show() {
        ViewCard selectedCard;
        System.out.println();
        System.out.println();
        if ( true ) {//if ( viewSubTurn.isMyTurn() ) { todo:check it
            selectedCard = this.showSelectInterface();
            try {
                cardSelectionExecuter.setNameCard(selectedCard);
            } catch (WrongParametersException e) {
                e.printStackTrace();
            }

            try {
                cardSelectionExecuter.doIt();
            } catch (CannotSendEventException e) {
                e.printStackTrace();
            }
        } else {
            this.showWaitMessage();
        }
        System.out.println();

    }

    @Override
    public ViewSubTurn getSubTurn() {
        return viewSubTurn;
    }

    /**
     * Overloading of WTerminalSubTurnViewer's setMyWTerminalStatusViewer to set the correct WTerminalStatusViewer
     * @param myWTerminalStatusViewer
     */
    public void setMyWTerminalStatusViewer( WTerminalGamePreparationViewer myWTerminalStatusViewer) {
        this.myWTerminalStatusViewer = myWTerminalStatusViewer;
    }
}
