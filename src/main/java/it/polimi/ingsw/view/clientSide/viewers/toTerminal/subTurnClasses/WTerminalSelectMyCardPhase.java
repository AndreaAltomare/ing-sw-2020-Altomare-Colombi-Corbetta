package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.CardSelectionExecuter;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.GodSymbols;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class WTerminalSelectMyCardPhase extends WTerminalSubTurnViewer {

    private CardSelectionExecuter cardSelectionExecuter;
    private CardSelection cardSelection;

    private final int GOD_SYMBOL_SPACE = GodSymbols.getMaxRepresentationLength() + 2;
    private final int GOD_NAME_SPACE = GodSymbols.getMaxNameLength() + 2;


    public WTerminalSelectMyCardPhase(CardSelection cardSelection) {
        this.cardSelection = cardSelection;
        this.cardSelectionExecuter = (CardSelectionExecuter) cardSelection.getExecuter();
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
        }

        return selectedCard;
    }

    /**
     * Prints a numbered list with Gods Symbols and god
     */
    private void printCardList() {
        GodSymbols godSymbols;
        int cardNumber = 1;

        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.println("Card List:");
        for (ViewCard viewCard : cardSelection.getCardList()) {

            try {
                godSymbols = GodSymbols.searchGodSymbol(viewCard.getName());

                System.out.println();
                // first line
                PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
                System.out.print(" ");
                PrintFunction.printAtTheMiddle( godSymbols.getUpRepresentation(), GOD_SYMBOL_SPACE );
                PrintFunction.printRepeatString(" ", GOD_NAME_SPACE);
                System.out.println();
                // second line
                PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
                System.out.print(" ");
                PrintFunction.printAtTheMiddle( godSymbols.getMiddleRepresentation(), GOD_SYMBOL_SPACE );
                PrintFunction.printRepeatString(" ", GOD_NAME_SPACE);
                System.out.println();
                // third line
                PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
                System.out.printf("%d", cardNumber);
                PrintFunction.printAtTheMiddle( godSymbols.getDownRepresentation(), GOD_SYMBOL_SPACE );
                PrintFunction.printAtTheMiddle( viewCard.getName(), GOD_NAME_SPACE);
                System.out.println();

                cardNumber++;

            } catch (NotFoundException ignored) {
            }

        }
        System.out.println();

    }

    /**
     * Shows a request to see god's description and checks it
     * @return
     */
    private int showGodRequest() {
        final String REQUEST_MESSAGE = "Please, insert the number of god which you want to see:";
        final String WRONG_NUMBER_MESSAGE = "The chosen number isn't in the list, please change it";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct, please change it";
        int numberSelected;

        do {
            System.out.println();
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.println(REQUEST_MESSAGE);
            PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
            System.out.print(">>");
            try {
                numberSelected = new Scanner(System.in).nextInt();
                if ( (numberSelected <= 0 || numberSelected > cardSelection.getCardList().size()) ) {
                    PrintFunction.printError(WRONG_NUMBER_MESSAGE);
                }
            } catch (InputMismatchException e) {
                numberSelected = -1;
                PrintFunction.printError(WRONG_VALUE_MESSAGE);
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
        final String SELECTION_REQUEST = "Do you want to use this card?";
        final String INPUT_CHOOSE = "( 0: No/ 1: Yes ):";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct, please change it";
        boolean correctResponse;
        int response;
        ViewCard seeCard = null;

        try {
            // show card
            System.out.println();
            seeCard = cardSelection.getCardList().get(godCardNumber - 1);
            do {
                PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
                System.out.printf("Name: %s\n\n", seeCard.getName());
                PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
                System.out.printf("Epithet: %s\n\n", seeCard.getEpiteth());
                PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
                System.out.printf("Description: %s\n\n", seeCard.getDescription());

                PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
                System.out.println( SELECTION_REQUEST );
                PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
                System.out.print( INPUT_CHOOSE );
                try {
                    response = new Scanner( System.in ).nextInt();
                } catch ( InputMismatchException e) {
                    response = -1;
                }
                if ( response != 1) {
                    if ( response != 0) {
                        correctResponse = false;
                        PrintFunction.printError(WRONG_VALUE_MESSAGE);
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

    @Override
    public void show() {
        final String ALL_RIGHT_MESSAGE = "Your card request is correctly send";
        String wrongSetCardMessage = "Your card isn't correctly set";
        ViewCard selectedCard;

        PrintFunction.printRepeatString("\n" ,2);
        selectedCard = this.showSelectInterface();
        try {
            cardSelectionExecuter.setNameCard(selectedCard);
        } catch (WrongParametersException e) {
            if (!e.getMessage().equals("")) {
                wrongSetCardMessage = e.getMessage();
            }
            PrintFunction.printError(wrongSetCardMessage);
        }

        try {
            cardSelectionExecuter.doIt();
            PrintFunction.printCheck(ALL_RIGHT_MESSAGE);
        } catch (CannotSendEventException e) {
            PrintFunction.printError(e.getErrorMessage());
        }System.out.println();

    }

}
