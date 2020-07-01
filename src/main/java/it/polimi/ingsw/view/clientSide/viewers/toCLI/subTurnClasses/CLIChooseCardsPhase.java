package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewPlayer;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.CardsChoosingExecuter;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.*;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class CLIChooseCardsPhase extends CLISubTurnViewer {

    private CardsChoosingExecuter cardsChoosingExecuter;
    private CardSelection cardSelection;
    private List<ViewCard> selectedCards= new ArrayList<>();

    private final int SELECTION_SPACE = 3;
    private final int GOD_SYMBOL_SPACE = CLIGodSymbols.getMaxRepresentationLength() + 2;
    private final int GOD_NAME_SPACE = CLIGodSymbols.getMaxNameLength() + 2;
    private final String SELECTION_COLOR = ANSIStyle.GREY.getEscape();
    private final String SELECTION_BACK_COLOR = ANSIStyle.BACK_GREY.getEscape();
    private final String SELECTION_WRITE_COLOR = ANSIStyle.BLUE.getEscape();


    public CLIChooseCardsPhase(CardSelection cardSelection) {
        this.cardSelection = cardSelection;
        cardsChoosingExecuter = (CardsChoosingExecuter) cardSelection.getExecuter();
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
        CLIGodSymbols cliGodSymbols;
        List<ViewCard> cardList = cardSelection.getCardList();
        int cardNumber = 1;
        String upString;
        String middleString;
        String downString;

        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.println("Card List:");
        for (ViewCard viewCard : cardList) {

            try {
                cliGodSymbols = CLIGodSymbols.searchGodSymbol(viewCard.getName());

                System.out.println();
                // first line
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                upString = this.checkLeftSelect(viewCard, false) + "  ";
                upString = upString + CLIPrintFunction.increaseLengthWithSpace(cliGodSymbols.getUpRepresentation(), cliGodSymbols.getRepresentationLength(), GOD_SYMBOL_SPACE);
                upString = upString + CLIPrintFunction.increaseLengthWithSpace("", 0, GOD_NAME_SPACE);
                upString = upString + this.checkRightSelect(viewCard, false);
                System.out.println(upString);
                // second line
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                middleString = this.checkLeftSelect(viewCard, true) + "  ";
                middleString = middleString + CLIPrintFunction.increaseLengthWithSpace(cliGodSymbols.getMiddleRepresentation(), cliGodSymbols.getRepresentationLength(), GOD_SYMBOL_SPACE);
                middleString = middleString + CLIPrintFunction.increaseLengthWithSpace("", 0, GOD_NAME_SPACE);
                middleString = middleString + this.checkRightSelect(viewCard, true);
                System.out.println(middleString);
                // third line
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                downString = this.checkLeftSelect(viewCard, false) + cardNumber + ".";
                downString = downString + CLIPrintFunction.increaseLengthWithSpace(cliGodSymbols.getDownRepresentation(), cliGodSymbols.getRepresentationLength(), GOD_SYMBOL_SPACE);
                downString = downString + CLIPrintFunction.increaseLengthWithSpace(viewCard.getName(), viewCard.getName().length(), GOD_NAME_SPACE);
                downString = downString + this.checkRightSelect(viewCard, false);
                System.out.println(downString);

                cardNumber++;

            } catch (NotFoundException | NullPointerException ignored) {
            }

        }
        System.out.println();

    }

    /**
     * checks if the card is selected, then returns the selected style if it is and the selection left symbol
     * if addSelectionSymbol == true or a string with space if it isn't. Returns a string of space without color if the card isn't selected
     * @param viewCard
     * @param addSelectionSymbol
     * @return
     */
    private String checkLeftSelect(ViewCard viewCard, boolean addSelectionSymbol) {
        String selectString = CLIPrintFunction.increaseLengthWithSpace(" ", 1, SELECTION_SPACE);

        if ( !selectedCards.isEmpty() ) {
            if( selectedCards.contains(viewCard)) {
                if (addSelectionSymbol) {
                    selectString = SELECTION_COLOR + UnicodeSymbol.SELECTION_LEFT.getEscape() + ANSIStyle.RESET;
                    selectString = CLIPrintFunction.increaseLengthWithSpace(selectString, 1, SELECTION_SPACE);
                }
                selectString = selectString + SELECTION_BACK_COLOR + SELECTION_WRITE_COLOR;
            }
        }

        return selectString;
    }

    /**
     * checks if the card is selected and returns ANSIStyle.RESET with a space string or a string with right selected symbol
     * if the card is selected and addSelectionSymbol == true
     * @param viewCard
     * @param addSelectionSymbol
     * @return
     */
    private String checkRightSelect(ViewCard viewCard, boolean addSelectionSymbol) {
        String selectString = CLIPrintFunction.increaseLengthWithSpace(" ", 1, SELECTION_SPACE);

        if ( !selectedCards.isEmpty() ) {
            if (selectedCards.contains(viewCard)) {
                if (addSelectionSymbol) {
                    selectString = SELECTION_COLOR + UnicodeSymbol.SELECTION_RIGHT.getEscape() + ANSIStyle.RESET;
                    selectString = CLIPrintFunction.increaseLengthWithSpace(selectString, 1, SELECTION_SPACE);
                }
            }
        }

        selectString = ANSIStyle.RESET + selectString;

        return selectString;
    }

    /**
     * Shows a request to see god's description and checks it
     * @return number of god selected
     */
    private int showGodRequest() {
        final String REQUEST_MESSAGE = "Please, insert the number of god which you want to see:";
        final String WRONG_NUMBER_MESSAGE = "The chosen number isn't in the list, please change it";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct, please change it";
        int numberSelected;

        System.out.println();
        do {
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
            System.out.println(REQUEST_MESSAGE);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
            System.out.print(CLIPrintFunction.WRITE_MARK);
            try {
                numberSelected = new Scanner(System.in).nextInt();
                if ( (numberSelected <= 0 || numberSelected > cardSelection.getCardList().size()) ) {
                    CLIPrintFunction.printError(WRONG_NUMBER_MESSAGE);
                }
            } catch (InputMismatchException e) {
                numberSelected = -1;
                CLIPrintFunction.printError(WRONG_VALUE_MESSAGE);
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
        final String WRITE_PART_STYLE = ANSIStyle.BLUE.getEscape() + ANSIStyle.BACK_GREY.getEscape();

        ViewCard seeCard;
        boolean response;

        try {
            // show card
            System.out.println();
            seeCard = cardSelection.getCardList().get(godCardNumber - 1);
            do {
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                System.out.printf(WRITE_PART_STYLE + "Name:" + ANSIStyle.RESET +  "%s\n\n", seeCard.getName());
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                System.out.printf(WRITE_PART_STYLE + "Epithet:" + ANSIStyle.RESET + "%s\n\n", seeCard.getEpiteth());
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                System.out.printf(WRITE_PART_STYLE + "Description:" + ANSIStyle.RESET + "%s\n\n", seeCard.getDescription());
                response = this.SelOrDeselRequest(seeCard);
            } while ( !response );
        } catch (IndexOutOfBoundsException ignored) {
        }
        System.out.println();
    }

    /**
     * Prints the request of selection or deselection and returns true if the response is correct, false if it isn't
     * @param viewCard
     * @return
     */
    private boolean SelOrDeselRequest( ViewCard viewCard) {
        final String SELECTION_REQUEST = "Do you want to select it?";
        final String DESELECTION_REQUEST = "Do you want to deselect it?";
        final String INPUT_CHOOSE = "( 0: No/ 1: Yes ):";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct, please change it";
        boolean response;
        int intResponse;

        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        if ( selectedCards.contains(viewCard) ) {
            System.out.println( DESELECTION_REQUEST );
        } else {
            System.out.println( SELECTION_REQUEST );
        }

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.print( CLIPrintFunction.WRITE_MARK + INPUT_CHOOSE );
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
                CLIPrintFunction.printError(WRONG_VALUE_MESSAGE);
            }
        } else {
            response = true;
        }
        System.out.println();

        return response;

    }

    /**
     * Understands and shows the correct phase for each player using some private methods, then uses executur to send
     * message when there are
     */
    @Override
    public void show() {
        final String ALL_RIGHT_MESSAGE = "The cards' list is correctly send";
        String wrongSendSetMessage = "The card request isn't correctly set";


        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
        this.showChooseInterface();
        for (ViewCard viewCard : selectedCards) {
            try {
                cardsChoosingExecuter.add(viewCard);
            } catch (WrongParametersException e) {
                if (!e.getMessage().equals("")) {
                    CLIPrintFunction.printError(wrongSendSetMessage);
                }
            }
        }
        try {
            cardsChoosingExecuter.doIt();
            CLIPrintFunction.printCheck(ALL_RIGHT_MESSAGE);
        } catch (CannotSendEventException e) {
            CLIPrintFunction.printError(e.getErrorMessage());
        }
        System.out.println();
    }

}
