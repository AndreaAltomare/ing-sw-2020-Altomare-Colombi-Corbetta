package it.polimi.ingsw.view.clientSide.viewers.toCLI.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewCard;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.CardSelectionExecuter;
import it.polimi.ingsw.view.clientSide.viewers.cardSelection.CardSelection;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.ANSIStyle;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.enumeration.CLIGodSymbols;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.GodSymbols;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLIPrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toCLI.interfaces.CLISubTurnViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class CLISelectMyCardPhase extends CLISubTurnViewer {

    private CardSelectionExecuter cardSelectionExecuter;
    private CardSelection cardSelection;

    private final int GOD_SYMBOL_SPACE = GodSymbols.getMaxRepresentationLength() + 2;
    private final int GOD_NAME_SPACE = GodSymbols.getMaxNameLength() + 2;



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
        }

        return selectedCard;
    }

    /**
     * Prints a numbered list with Gods Symbols and god
     */
    private void printCardList() {
        CLIGodSymbols cliGodSymbols;
        int cardNumber = 1;
        String upString;
        String middleString;
        String downString;

        System.out.println();
        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
        System.out.println("Card List:");
        for (ViewCard viewCard : cardSelection.getCardList()) {

            try {
                cliGodSymbols = CLIGodSymbols.searchGodSymbol(viewCard.getName());

                System.out.println();
                // first line
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                upString = "  ";
                upString = upString + CLIPrintFunction.increaseLengthWithSpace(cliGodSymbols.getUpRepresentation(), cliGodSymbols.getRepresentationLength(), GOD_SYMBOL_SPACE);
                upString = upString + CLIPrintFunction.increaseLengthWithSpace("", 0, GOD_NAME_SPACE);
                System.out.println(upString);
                // second line
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                middleString = cardNumber + ".";
                middleString = middleString + CLIPrintFunction.increaseLengthWithSpace(cliGodSymbols.getMiddleRepresentation(), cliGodSymbols.getRepresentationLength(), GOD_SYMBOL_SPACE);
                middleString = middleString + CLIPrintFunction.increaseLengthWithSpace(viewCard.getName(), viewCard.getName().length(), GOD_NAME_SPACE);
                System.out.println(middleString);
                // third line
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                downString = "  ";
                downString = downString + CLIPrintFunction.increaseLengthWithSpace(cliGodSymbols.getDownRepresentation(), cliGodSymbols.getRepresentationLength(), GOD_SYMBOL_SPACE);
                downString = downString + CLIPrintFunction.increaseLengthWithSpace("", 0, GOD_NAME_SPACE);
                System.out.println(downString);

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
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                System.out.printf(WRITE_PART_STYLE + "Name:" + ANSIStyle.RESET +  "%s\n\n", seeCard.getName());
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                System.out.printf(WRITE_PART_STYLE + "Epithet:" + ANSIStyle.RESET + "%s\n\n", seeCard.getEpiteth());
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                System.out.printf(WRITE_PART_STYLE + "Description:" + ANSIStyle.RESET + "%s\n\n", seeCard.getDescription());

                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                System.out.println( SELECTION_REQUEST );
                CLIPrintFunction.printRepeatString(ANSIStyle.RESET, " ", CLIPrintFunction.STARTING_SPACE);
                System.out.print( CLIPrintFunction.WRITE_MARK + INPUT_CHOOSE );
                try {
                    response = new Scanner( System.in ).nextInt();
                } catch ( InputMismatchException e) {
                    response = -1;
                }
                if ( response != 1) {
                    if ( response != 0) {
                        correctResponse = false;
                        CLIPrintFunction.printError(WRONG_VALUE_MESSAGE);
                    } else {
                        correctResponse = true;
                        seeCard = null;
                    }
                } else {
                    correctResponse = true;
                }
            } while ( !correctResponse );
        } catch (IndexOutOfBoundsException ignored) {
        }
        System.out.println();

        return seeCard;
    }

    @Override
    public void show() {
        final String ALL_RIGHT_MESSAGE = "Your card request is correctly send";
        String wrongSetCardMessage = "Your card isn't correctly set";
        ViewCard selectedCard;

        CLIPrintFunction.printRepeatString(ANSIStyle.RESET, "\n", 2);
        selectedCard = this.showSelectInterface();
        try {
            cardSelectionExecuter.setNameCard(selectedCard);
        } catch (WrongParametersException e) {
            if (!e.getMessage().equals("")) {
                wrongSetCardMessage = e.getMessage();
            }
            CLIPrintFunction.printError(wrongSetCardMessage);
        }

        try {
            cardSelectionExecuter.doIt();
            CLIPrintFunction.printCheck(ALL_RIGHT_MESSAGE);
        } catch (CannotSendEventException e) {
            CLIPrintFunction.printError(e.getErrorMessage());
        }
        System.out.println();
    }

}
