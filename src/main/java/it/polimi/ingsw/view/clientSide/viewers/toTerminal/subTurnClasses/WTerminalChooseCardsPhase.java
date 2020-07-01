package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.CardsChoosingExecuter;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.GodSymbols;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.Symbols;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.SymbolsLevel;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Class that represents the <code>WTerminalSubTurnViewer</code> FirstPlayer on the Windows Terminal
 * using methods of <code>PrintFunction</code>, <code>GodSymbols</code>, <code>Symbols</code> and <code>SymbolsLevel</code>
 *
 * @see WTerminalSubTurnViewer
 * @see PrintFunction
 * @see GodSymbols
 * @see Symbols
 * @see SymbolsLevel
 * @author Marco
 */
public class WTerminalChooseCardsPhase extends WTerminalSubTurnViewer {

    private CardsChoosingExecuter cardsChoosingExecuter;
    private CardSelection cardSelection;
    private List<ViewCard> selectedCards= new ArrayList<>();

    private final int SELECTION_SPACE = Symbols.SELECTION_LEFT.getUpRepresentation().length() + 2;
    private final int GOD_SYMBOL_SPACE = GodSymbols.getMaxRepresentationLength() + 2;
    private final int GOD_NAME_SPACE = GodSymbols.getMaxNameLength() + 2;


    /**
     * Constructor to set the correctly <code>CardSelection</code> and his executor
     *
     * @see CardSelection
     * @param cardSelection <code>CardSelection</code> linked at this class
     */
    public WTerminalChooseCardsPhase(CardSelection cardSelection) {
        this.cardSelection = cardSelection;
        this.cardsChoosingExecuter = (CardsChoosingExecuter) cardSelection.getExecuter();
    }

    /**
     * Uses private methods to show a list with god cards to the player as long as he chooses a god card for all the player
     */
    private void showChooseInterface() {

        while (selectedCards.size() < ViewPlayer.getNumberOfPlayers() ) {
            this.printCardList();
            this.showCardSelected( this.showGodRequest() );
        }
    }

    /**
     * Prints a numbered list with Gods' Symbols and gods
     *
     * @see GodSymbols
     */
    private void printCardList() {
        GodSymbols godSymbols;
        int cardNumber = 1;

        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.println("Card List:");
        for (ViewCard viewCard : cardSelection.getCardList()) {

            try {
                godSymbols = GodSymbols.searchGodSymbol(viewCard.getName());

                System.out.println();
                // first line
                PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
                PrintFunction.printAtTheMiddle( this.checkLeftSelect(viewCard, SymbolsLevel.UP), SELECTION_SPACE );
                System.out.print(" ");
                PrintFunction.printAtTheMiddle( godSymbols.getUpRepresentation(), GOD_SYMBOL_SPACE );
                PrintFunction.printRepeatString(" ", GOD_NAME_SPACE);
                PrintFunction.printAtTheMiddle( this.checkRightSelect(viewCard, SymbolsLevel.UP), SELECTION_SPACE);
                System.out.println();
                // second line
                PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
                PrintFunction.printAtTheMiddle( this.checkLeftSelect(viewCard, SymbolsLevel.MIDDLE), SELECTION_SPACE );
                System.out.print(" ");
                PrintFunction.printAtTheMiddle( godSymbols.getMiddleRepresentation(), GOD_SYMBOL_SPACE );
                PrintFunction.printRepeatString(" ", GOD_NAME_SPACE);
                PrintFunction.printAtTheMiddle( this.checkRightSelect(viewCard, SymbolsLevel.MIDDLE), SELECTION_SPACE);
                System.out.println();
                // third line
                PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
                PrintFunction.printAtTheMiddle( this.checkLeftSelect(viewCard, SymbolsLevel.DOWN), SELECTION_SPACE );
                System.out.printf("%d", cardNumber);
                PrintFunction.printAtTheMiddle( godSymbols.getDownRepresentation(), GOD_SYMBOL_SPACE );
                PrintFunction.printAtTheMiddle( viewCard.getName(), GOD_NAME_SPACE);
                PrintFunction.printAtTheMiddle( this.checkRightSelect(viewCard, SymbolsLevel.DOWN), SELECTION_SPACE);
                System.out.println();

                cardNumber++;

            } catch (NotFoundException | NullPointerException ignored) {
            }

        }
        System.out.println();

    }

    /**
     * checks if the card is selected, then returns the left selection symbol at the chosen level if it is,
     * else a empty <code>String</code>
     *
     * @param viewCard <code>ViewCard</code> to control
     * @param symbolsLevel <code>SymbolsLevel</code> parameter to add correct part of selection left Symbol
     * @return <code>String</code> with left selection symbol at chosen level if viewCard is in selected List, else a empty <code>String</code>
     */
    private String checkLeftSelect(ViewCard viewCard, SymbolsLevel symbolsLevel) {
         String selectString = "";

         if ( !selectedCards.isEmpty() ) {
             switch (symbolsLevel) {
                 case UP:
                     if (selectedCards.contains(viewCard)) {
                        selectString = Symbols.SELECTION_LEFT.getUpRepresentation();
                     }
                     break;
                 case MIDDLE:
                     if (selectedCards.contains(viewCard)) {
                         selectString = Symbols.SELECTION_LEFT.getMiddleRepresentation();
                     }
                     break;
                 case DOWN:
                     if (selectedCards.contains(viewCard)) {
                         selectString = Symbols.SELECTION_LEFT.getDownRepresentation();
                     }
                     break;
                 default:
             }
         }

         return selectString;
    }

    /**
     * checks if the card is selected, then returns the right selection symbol right at the chosen level if it is,
     * else a empty <code>String</code>
     *
     * @param viewCard <code>ViewCard</code> to control
     * @param symbolsLevel <code>SymbolsLevel</code> parameter to add correct part of selection right Symbol
     * @return <code>String</code> with right selection symbol at chosen level if viewCard is in selected List, else a empty <code>String</code>
     */
    private String checkRightSelect(ViewCard viewCard, SymbolsLevel symbolsLevel) {
        String selectString = "";

        if ( !selectedCards.isEmpty() ) {
            switch (symbolsLevel) {
                case UP:
                    if (selectedCards.contains(viewCard)) {
                        selectString = Symbols.SELECTION_RIGHT.getUpRepresentation();
                    }
                    break;
                case MIDDLE:
                    if (selectedCards.contains(viewCard)) {
                        selectString = Symbols.SELECTION_RIGHT.getMiddleRepresentation();
                    }
                    break;
                case DOWN:
                    if (selectedCards.contains(viewCard)) {
                        selectString = Symbols.SELECTION_RIGHT.getDownRepresentation();
                    }
                    break;
                default:
            }
        }

        return selectString;
    }

    /**
     * Shows a request to see god's description and checks it
     *
     * @return number of god selected
     */
    private int showGodRequest() {
        final String REQUEST_MESSAGE = "Please, insert the number of god which you want to see:";
        final String WRONG_NUMBER_MESSAGE = "The chosen number isn't in the list, please change it";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct, please change it";
        int numberSelected;

        System.out.println();
        do {
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
     * Shows card's specifics and the request of selection or deselection when it is deselected or not as long as
     * player's response is accepted
     *
     * @param godCardNumber number of the <code>ViewCard</code> chosen
     */
    private void showCardSelected(int godCardNumber) {
        ViewCard seeCard;
        boolean response;

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
                response = this.SelOrDeselRequest(seeCard);
            } while ( !response );
        } catch (IndexOutOfBoundsException ignored) {
        }
        System.out.println();
    }

    /**
     * Prints the request of selection or deselection and returns true if the response is correct, false if it isn't
     *
     * @param viewCard <code>ViewCard</code> chosen
     * @return true if the card is correctly sel/desel, else false
     */
    private boolean SelOrDeselRequest( ViewCard viewCard) {
        final String SELECTION_REQUEST = "Do you want to select it?";
        final String DESELECTION_REQUEST = "Do you want to deselect it?";
        final String INPUT_CHOOSE = "( 0: No/ 1: Yes ):";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct, please change it";
        boolean response;
        int intResponse;

        System.out.println();
        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        if ( selectedCards.contains(viewCard) ) {
            System.out.println( DESELECTION_REQUEST );
        } else {
            System.out.println( SELECTION_REQUEST );
        }

        PrintFunction.printRepeatString(" ", PrintFunction.STARTING_SPACE);
        System.out.print( INPUT_CHOOSE );
        try {
            intResponse = new Scanner( System.in ).nextInt();
        } catch ( InputMismatchException e) {
            intResponse = -1;
        }
        if ( intResponse != 0) {
            if ( intResponse == 1) {
                response = true;
                if ( selectedCards.contains(viewCard) ) {
                    selectedCards.remove(viewCard);
                } else {
                    selectedCards.add(viewCard);
                }
            } else {
                response = false;
                PrintFunction.printError(WRONG_VALUE_MESSAGE);
            }
        } else {
            response = true;
        }
        System.out.println();

        return response;

    }

    /**
     * Uses private methods to menage the phase, the call the executor and notify the result of the operation with a printed message
     */
    @Override
    public void show() {
        final String ALL_RIGHT_MESSAGE = "The cards' list is correctly send";
        String wrongSendSetMessage = "The card request isn't correctly set";

        PrintFunction.printRepeatString("\n", 2);
        this.showChooseInterface();
        for (ViewCard viewCard : selectedCards) {
            try {
                cardsChoosingExecuter.add(viewCard);
            } catch (WrongParametersException e) {
                if (!e.getMessage().equals("")) {
                    PrintFunction.printError(wrongSendSetMessage);
                }
            }
        }
        try {
            cardsChoosingExecuter.doIt();
            PrintFunction.printCheck(ALL_RIGHT_MESSAGE);
        } catch (CannotSendEventException e) {
            PrintFunction.printError(e.getErrorMessage());
        }
        System.out.println();
    }

}
