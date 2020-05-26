package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.CardsChoosingExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.GodSymbols;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.Symbols;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.SymbolsLevel;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.PrintFunction;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

//todo: rivedere intera classe per sistemare uso degli executer
public class CLIChooseCardsPhase extends CLISubTurnViewer {

    private ViewSubTurn viewSubTurn = ViewSubTurn.SELECTCARD;
    private CardsChoosingExecuter cardsChoosingExecuter = new CardsChoosingExecuter( ViewPlayer.getNumberOfPlayers());
    private CardSelection cardSelection;
    private List<ViewCard> selectedCards= new ArrayList<>();

    private final int STARTING_SPACE = 7;
    private final int SELECTION_SPACE = Symbols.SELECTION_LEFT.getUpRepresentation().length() + 2;
    private final int GOD_SYMBOL_SPACE = GodSymbols.getMaxRepresentationLength() + 2;
    private final int GOD_NAME_SPACE = GodSymbols.getMaxNameLength() + 2;


    public CLIChooseCardsPhase(CardSelection cardSelection) {
        this.cardSelection = cardSelection;
    }

    /**
     * Uses private methods to show a list with god cards to the player as long as he chooses a god card for all the player
     */
    private void showChooseInterface() {

        while (selectedCards.size() < ViewPlayer.getNumberOfPlayers() ) {
            this.printCardList();
            this.showCardAndSel( this.showGodRequest() );
        }
    }

    /**
     * Prints a numbered list with Gods' Symbols and gods
     */
    private void printCardList() {
        GodSymbols godSymbols;
        int cardNumber = 1;

        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.println("Card List:");
        for (ViewCard viewCard : cardSelection.getCardList()) {

            try {
                godSymbols = GodSymbols.searchGodSymbol(viewCard.getName());

                System.out.println();
                // first line
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                PrintFunction.printAtTheMiddle( this.checkLeftSelect(viewCard, SymbolsLevel.UP), SELECTION_SPACE );
                System.out.print(" ");
                PrintFunction.printAtTheMiddle( godSymbols.getUpRepresentation(), GOD_SYMBOL_SPACE );
                PrintFunction.printRepeatString(" ", GOD_NAME_SPACE);
                PrintFunction.printAtTheMiddle( this.checkRightSelect(viewCard, SymbolsLevel.UP), SELECTION_SPACE);
                System.out.println();
                // second line
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                PrintFunction.printAtTheMiddle( this.checkLeftSelect(viewCard, SymbolsLevel.MIDDLE), SELECTION_SPACE );
                System.out.print(" ");
                PrintFunction.printAtTheMiddle( godSymbols.getMiddleRepresentation(), GOD_SYMBOL_SPACE );
                PrintFunction.printRepeatString(" ", GOD_NAME_SPACE);
                PrintFunction.printAtTheMiddle( this.checkRightSelect(viewCard, SymbolsLevel.MIDDLE), SELECTION_SPACE);
                System.out.println();
                // third line
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                PrintFunction.printAtTheMiddle( this.checkLeftSelect(viewCard, SymbolsLevel.DOWN), SELECTION_SPACE );
                System.out.printf("%d", cardNumber);
                PrintFunction.printAtTheMiddle( godSymbols.getDownRepresentation(), GOD_SYMBOL_SPACE );
                PrintFunction.printAtTheMiddle( viewCard.getName(), GOD_NAME_SPACE);
                PrintFunction.printAtTheMiddle( this.checkRightSelect(viewCard, SymbolsLevel.DOWN), SELECTION_SPACE);
                System.out.println();

                cardNumber++;

            } catch (NotFoundException e) {
                e.printStackTrace();
            }

        }
        System.out.println();

    }

    /**
     * checks if the card id selected and prints the correct left select symbol
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
     * checks if the card id selected and prints the correct left select symbol
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
     * @return
     */
    private int showGodRequest() {
        int numberSelected;

        System.out.println();
        do {
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
     * Shows card's specifics and the request of selection or deselection when it is deselected or not as long as
     * player's response is accepted
     * @param godCardNumber
     */
    private void showCardAndSel( int godCardNumber) {
        ViewCard seeCard;
        boolean response;

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
                response = this.SelOrDeselRequest(seeCard);
            } while ( !response );
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    /**
     * Prints the request of selection or deselection and returns true if the response is correct, false if it isn't
     * @param viewCard
     * @return
     */
    private boolean SelOrDeselRequest( ViewCard viewCard) {
        final String selectionRequest = "Do you want to select it?";
        final String deselectionRequest = "Do you want to deselect it?";
        final String inputChoose = ">>( 0: No/ 1: Yes ):";
        boolean response;
        int intResponse;

        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        if ( selectedCards.contains(viewCard) ) {
            System.out.println( deselectionRequest );
        } else {
            System.out.println( selectionRequest );
        }

        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.print( inputChoose );
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
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.println( ">< The chosen value isn't correct, please change it");
            }
        } else {
            response = true;
        }
        System.out.println();

        return response;

    }

    /**
     * Prints a waiting message to players who can't choose
     */
    private void showWaitMessage() {
        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.println("A player is choosing the cards, please wait");
        System.out.println();
        //todo: maybe to do an little animation like in CLIWaitingStatus
    }

    /**
     * Understands and shows the correct phase for each player using some private methods, then uses executur to send
     * message when there are
     */
    @Override
    public void show() {

        System.out.println();
        System.out.println();
        if ( true ) {//if ( viewSubTurn.isMyTurn() ) { todo:check it
            this.showChooseInterface();
            for (ViewCard viewCard : selectedCards) {
                try {
                    cardsChoosingExecuter.add(viewCard);
                } catch (WrongParametersException e) {
                    e.printStackTrace();
                }
            }
            try {
                cardsChoosingExecuter.doIt();
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
}
