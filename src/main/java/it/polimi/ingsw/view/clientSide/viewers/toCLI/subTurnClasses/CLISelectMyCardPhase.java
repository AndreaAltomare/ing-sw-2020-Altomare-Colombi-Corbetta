package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.CardSelectionExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.CLIGodSymbols;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.GodSymbols;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.UnicodeSymbol;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.statusClasses.CLIGamePreparationViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.InputMismatchException;
import java.util.Scanner;

//todo: rivedere intera classe per sistemare uso degli executer
public class CLISelectMyCardPhase extends CLISubTurnViewer {

    private CardSelectionExecuter cardSelectionExecuter;
    private CardSelection cardSelection;

    private final int STARTING_SPACE = 7;
    private final int GOD_SYMBOL_SPACE = GodSymbols.getMaxRepresentationLength() + 2;
    private final int GOD_NAME_SPACE = GodSymbols.getMaxNameLength() + 2;
    private final String ERROR_COLOR_AND_SYMBOL = ANSIStyle.RED.getEscape() + UnicodeSymbol.X_MARK.getEscape();
    private final String CORRECT_COLOR_AND_SYMBOL = ANSIStyle.GREEN.getEscape() + UnicodeSymbol.CHECK_MARK.getEscape();
    private final String WRITE_MARK = ANSIStyle.UNDERSCORE.getEscape() + UnicodeSymbol.PENCIL.getEscape() + ANSIStyle.RESET;



    public CLISelectMyCardPhase(CardSelection cardSelection) {
        this.cardSelection = cardSelection;
        cardSelectionExecuter = (CardSelectionExecuter) cardSelection.getExecuter();
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
        CLIGodSymbols cliGodSymbols;
        Integer cardNumber = 1;
        String upString;
        String middleString;
        String downString;

        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.println("Card List:");
        for (ViewCard viewCard : cardSelection.getCardList()) {

            try {
                cliGodSymbols = CLIGodSymbols.searchGodSymbol(viewCard.getName());

                System.out.println();
                // first line
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                upString = "  ";
                upString = upString + CLIPrintFunction.increaseLengthWithSpace(cliGodSymbols.getUpRepresentation(), cliGodSymbols.getRepresentationLength(), GOD_SYMBOL_SPACE);
                upString = upString + CLIPrintFunction.increaseLengthWithSpace("", 0, GOD_NAME_SPACE);
                System.out.println(upString);
                // second line
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                middleString = cardNumber.toString() + ".";
                middleString = middleString + CLIPrintFunction.increaseLengthWithSpace(cliGodSymbols.getMiddleRepresentation(), cliGodSymbols.getRepresentationLength(), GOD_SYMBOL_SPACE);
                middleString = middleString + CLIPrintFunction.increaseLengthWithSpace(viewCard.getName(), viewCard.getName().length(), GOD_NAME_SPACE);
                System.out.println(middleString);
                // third line
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                downString = "  ";
                downString = downString + CLIPrintFunction.increaseLengthWithSpace(cliGodSymbols.getDownRepresentation(), cliGodSymbols.getRepresentationLength(), GOD_SYMBOL_SPACE);
                downString = downString + CLIPrintFunction.increaseLengthWithSpace("", 0, GOD_NAME_SPACE);
                System.out.println(downString);

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
        final String REQUEST_MESSAGE = "Please, insert the number of god which you want to see:";
        final String WRONG_NUMBER_MESSAGE = "The chosen number isn't in the list, please change it";
        final String WRONG_VALUE_MESSAGE = "The chosen value isn't correct, please change it";
        int numberSelected;

        System.out.println();
        do {
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println(REQUEST_MESSAGE);
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.print(WRITE_MARK);
            try {
                numberSelected = new Scanner(System.in).nextInt();
                if ( (numberSelected <= 0 || numberSelected > cardSelection.getCardList().size()) ) {
                    CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE );
                    System.out.println(ERROR_COLOR_AND_SYMBOL + WRONG_NUMBER_MESSAGE + ANSIStyle.RESET);
                }
            } catch (InputMismatchException e) {
                numberSelected = -1;
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE );
                System.out.println(ERROR_COLOR_AND_SYMBOL + WRONG_VALUE_MESSAGE + ANSIStyle.RESET);
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
        final String WRITE_PART_STYLE = ANSIStyle.BLUE.getEscape() + ANSIStyle.BACK_GREY.getEscape();
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
                //todo:maybe add god's symbol
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                System.out.printf(WRITE_PART_STYLE + "Name:" + ANSIStyle.RESET +  "%s\n\n", seeCard.getName());
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                System.out.printf(WRITE_PART_STYLE + "Epithet:" + ANSIStyle.RESET + "%s\n\n", seeCard.getEpiteth());
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                System.out.printf(WRITE_PART_STYLE + "Description:" + ANSIStyle.RESET + "%s\n\n", seeCard.getDescription());

                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                System.out.println( SELECTION_REQUEST );
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                System.out.print( WRITE_MARK + INPUT_CHOOSE );
                try {
                    response = new Scanner( System.in ).nextInt();
                } catch ( InputMismatchException e) {
                    response = -1;
                }
                if ( response != 1) {
                    if ( response != 0) {
                        correctResponse = false;
                        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                        System.out.println( ERROR_COLOR_AND_SYMBOL + WRONG_VALUE_MESSAGE + ANSIStyle.RESET);
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
        final String WRITE_PART_STYLE = ANSIStyle.BLUE.getEscape() + ANSIStyle.BACK_GREY.getEscape();
        final String LAST_CARD_MESSAGE = "This is your card";
        ViewCard seeCard = null;


        try {
            // show card
            System.out.println();
            seeCard = cardSelection.getCardList().get(1);
            //todo:maybe add god's symbol
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.printf(WRITE_PART_STYLE + "Name:" + ANSIStyle.RESET +  "%s\n\n", seeCard.getName());
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.printf(WRITE_PART_STYLE + "Epithet:" + ANSIStyle.RESET + "%s\n\n", seeCard.getEpiteth());
            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.printf(WRITE_PART_STYLE + "Description:" + ANSIStyle.RESET + "%s\n\n", seeCard.getDescription());

            CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
            System.out.println(LAST_CARD_MESSAGE);
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
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
        System.out.println("A player is choosing his card, please wait");
        System.out.println();
        //todo: maybe to do an little animation like in CLIWaitingStatus
    }

    @Override
    public void show() {
        final String ALL_RIGHT_MESSAGE = "Your card is correctly set";
        ViewCard selectedCard;

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
        if ( true ) {//if ( viewSubTurn.isMyTurn() ) { todo:check it
            selectedCard = this.showSelectInterface();
            try {
                cardSelectionExecuter.setNameCard(selectedCard);
            } catch (WrongParametersException e) {
                e.printStackTrace();
            }

            try {
                cardSelectionExecuter.doIt();
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", STARTING_SPACE);
                System.out.println(CORRECT_COLOR_AND_SYMBOL + ALL_RIGHT_MESSAGE + ANSIStyle.RESET);
            } catch (CannotSendEventException e) {
                e.printStackTrace();
            }
        } else {
            this.showWaitMessage();
        }
        System.out.println();

    }

}
