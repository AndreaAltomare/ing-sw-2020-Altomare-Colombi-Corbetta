package it.polimi.ingsw.view.clientSide.viewers.toTerminal.subTurnClasses;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.*;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.BuildBlockExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.executers.executerClasses.TurnStatusChangeExecuter;
import it.polimi.ingsw.view.clientSide.viewCore.status.ViewSubTurn;
import it.polimi.ingsw.view.clientSide.viewers.subTurnViewers.BuildViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.enumeration.Symbols;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.WTerminalSubTurnViewer;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.interfaces.PrintFunction;
import it.polimi.ingsw.view.clientSide.viewers.toTerminal.statusClasses.WTerminalPlayingViewer;
import it.polimi.ingsw.view.exceptions.CannotSendEventException;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.exceptions.WrongParametersException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class WTerminalBuildPhase extends WTerminalSubTurnViewer {

    private WTerminalPlayingViewer myWTerminalStatusViewer = null;
    private BuildViewer buildViewer;

    private final int STARTING_SPACE = 7;
    private final int DIRECTION_SPACE = 15;
    private final int PLACEABLE_SPACE = 7;

    public WTerminalBuildPhase(BuildViewer buildViewer) {
        this.buildViewer = buildViewer;
    }


    /**
     * Prints the Name, Epithet and Description of all the player's God
     */
    private void showCardsDetails () {
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

    /**
     * Prints the request to build using a private method to choose block's type, checks the response,
     * notifies the construction to Viewer and returns true if it is all correct,
     * or false if it isn't ( notifies to player when the input isn't correct)
     * @return
     */
    private boolean showBuildRequest() {
        int directionResponse;
        ViewCell selectedCell;
        String pLaceableToBuild;
        boolean correctResponse = false;
        BuildBlockExecuter buildBlockExecuter = (BuildBlockExecuter) buildViewer.getMySubTurn().getExecuter();

        pLaceableToBuild = this.typeBlockRequest();

        if ( pLaceableToBuild != null) {
            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println("Please, select a direction to build:");

            System.out.println();
            //first line
            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            PrintFunction.printAtTheMiddle("7: North-West", DIRECTION_SPACE);
            PrintFunction.printAtTheMiddle("8: North", DIRECTION_SPACE);
            PrintFunction.printAtTheMiddle("9: North-Est", DIRECTION_SPACE);
            System.out.println();
            //second line
            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            PrintFunction.printAtTheMiddle("4: West", DIRECTION_SPACE);
            PrintFunction.printRepeatString(" ", DIRECTION_SPACE);
            PrintFunction.printAtTheMiddle("6: Est", DIRECTION_SPACE);
            System.out.println();
            //third line
            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            PrintFunction.printAtTheMiddle("1: South-West", DIRECTION_SPACE);
            PrintFunction.printAtTheMiddle("2: South", DIRECTION_SPACE);
            PrintFunction.printAtTheMiddle("3: South-Est", DIRECTION_SPACE);
            System.out.println();

            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.print(">>");
            try {
                directionResponse = new Scanner(System.in).nextInt();
                selectedCell = this.calculateNextCell(directionResponse);
                if (selectedCell != null) {
                    buildBlockExecuter.setWorkerId(ViewWorker.getSelected());
                    buildBlockExecuter.setCell(selectedCell);
                    buildBlockExecuter.setPlaceable(pLaceableToBuild);
                    buildBlockExecuter.doIt();
                    correctResponse = true;
                    //todo: a little CLI control if it isn't necessary cancel it and all its helper methods
                    if (this.myWTerminalStatusViewer != null) {
                        myWTerminalStatusViewer.setBuildAfterMoveTrue();
                    }
                } else {
                    System.out.println();
                    PrintFunction.printRepeatString(" ", STARTING_SPACE);
                    System.out.print(">< The selected direction doesn't exist\n");
                }
            } catch (InputMismatchException e) {
                System.out.println();
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.print(">< The chosen value isn't correct\n");
            } catch (NotFoundException e) {
                System.out.println();
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.print(">< The cell in selected direction doesn't exist\n");
            } catch (WrongParametersException | CannotSendEventException e) {
                e.printStackTrace();
            }
        }

        return correctResponse;
    }

    /**
     * Prints the request to choose the block's type, checks the response and returns a String representing th block's type
     * if it is correct, or null and an error message to player if it isn't
     * @return
     */
    private String typeBlockRequest() {
        String blockType = null;
        int typeSelected;

        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.println("Please, select what type of block you want to choose:");

        System.out.println();
        //block: first line
        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE + 2);
        PrintFunction.printAtTheMiddle( Symbols.BLOCK.getUpRepresentation() , PLACEABLE_SPACE);
        //block: second line
        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.print("1:");
        PrintFunction.printAtTheMiddle( Symbols.BLOCK.getMiddleRepresentation() , PLACEABLE_SPACE);

        System.out.println();

        //dome: first line
        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE + 2);
        PrintFunction.printAtTheMiddle( Symbols.DOME.getUpRepresentation() , PLACEABLE_SPACE);
        //block: second line
        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.print("2:");
        PrintFunction.printAtTheMiddle( Symbols.DOME.getMiddleRepresentation() , PLACEABLE_SPACE);
        System.out.println();

        System.out.println();
        PrintFunction.printRepeatString(" ", STARTING_SPACE);
        System.out.print(">>");
        try {
            typeSelected = new Scanner(System.in).nextInt();
            switch ( typeSelected ) {
                case 1:
                    blockType = "BLOCK";
                    break;
                case 2:
                    blockType = "DOME";
                    break;
                default:
                    System.out.println();
                    PrintFunction.printRepeatString(" ", STARTING_SPACE);
                    System.out.print(">< The chosen block's type doesn't exist\n");
            }
        } catch (InputMismatchException e) {
            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.print(">< The chosen value isn't correct\n");
        }

        return  blockType;
    }

    /**
     * Calculates the cell at chosen direction and returns it if it is possible, returns null if the direction isn't correct
     * and trow a NotFoundException if the cell isn't on the board ( or the select worker isn't on the board )
     * @param direction
     * @return
     * @throws NotFoundException
     */
    private ViewCell calculateNextCell(int direction ) throws NotFoundException {
        ViewCell selectedCell = null;
        int workerRow;
        int workerColumn;

        workerRow = ViewWorker.getSelected().getPosition().getX();
        workerColumn = ViewWorker.getSelected().getPosition().getY();

        switch (direction) {
            case 7:
                selectedCell = ViewBoard.getBoard().getCellAt(workerRow - 1, workerColumn - 1);
                break;
            case 8:
                selectedCell = ViewBoard.getBoard().getCellAt(workerRow - 1, workerColumn );
                break;
            case 9:
                selectedCell = ViewBoard.getBoard().getCellAt(workerRow - 1, workerColumn + 1);
                break;
            case 4:
                selectedCell = ViewBoard.getBoard().getCellAt( workerRow , workerColumn - 1);
                break;
            case 6:
                selectedCell = ViewBoard.getBoard().getCellAt( workerRow , workerColumn + 1);
                break;
            case 1:
                selectedCell = ViewBoard.getBoard().getCellAt( workerRow + 1, workerColumn - 1);
                break;
            case 2:
                selectedCell = ViewBoard.getBoard().getCellAt( workerRow + 1, workerColumn );
                break;
            case 3:
                selectedCell = ViewBoard.getBoard().getCellAt( workerRow + 1, workerColumn + 1);
                break;
            case 5:
            default:
                ;
        }

        return selectedCell;

    }

    /**
     * Tries to change subTurn in MOVE and returns true if it can, or false if it can't
     * @return
     */
    private boolean toMovePhase() {
        boolean changePhase = false;
        TurnStatusChangeExecuter turnStatusChangeExecuter = new TurnStatusChangeExecuter();

        try {
            turnStatusChangeExecuter.setStatusId( ViewSubTurn.MOVE );
            turnStatusChangeExecuter.doIt();
            changePhase = true;
        } catch (WrongParametersException | CannotSendEventException e) {
            e.printStackTrace();
        }

        return changePhase;

    }

    /**
     * Tries to close player's turn and returns true if it can, or false if it can't
     * @return
     */
    private boolean endTurn() {
        boolean endTurn = false;

        //todo: little check, if it isn't necessary cancel it and all its structure
        if ( myWTerminalStatusViewer.isMove() && myWTerminalStatusViewer.isBuild()) {
            //todo: change the end's turn way after testing
            endTurn = this.toMovePhase();
        } else {
            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.print(">< It isn't possible to end turn, you must still ");
            if ( !myWTerminalStatusViewer.isMove() ) {
                System.out.println("MOVE!");
            } else {
                System.out.println("BUILD!");
            }
        }

        return endTurn;
    }

    /**
     * Prints the board and a request of command and uses some private methods to execute them as long as
     * a construction, a change status or a endTurn is correctly executed
     */
    @Override
    public void show() {
        boolean endBuild = false;
        int actionSelected;

        while ( !endBuild ) {
            System.out.println();
            System.out.println();

/*            //todo: valutarlo
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/
            ViewBoard.getBoard().toWTerminal();

            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println("Please, select a command:");
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println("1: Print all gods' details");
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println("2: Build");
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println("3: Go to Move Phase");
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.println("4: End Turn");

            System.out.println();
            PrintFunction.printRepeatString(" ", STARTING_SPACE);
            System.out.print(">>");
            try {
                actionSelected = new Scanner(System.in).nextInt();
                switch (actionSelected) {
                    case 1:
                        this.showCardsDetails();
                        break;
                    case 2:
                        endBuild = this.showBuildRequest();
                        break;
                    case 3:
                        endBuild = this.toMovePhase();
                        break;
                    case 4:
                        endBuild = this.endTurn();
                        break;
                    default:
                        System.out.println();
                        PrintFunction.printRepeatString(" ", STARTING_SPACE);
                        System.out.println(">< The chosen command doesn't exist, please change it");
                }
            } catch (InputMismatchException e) {
                System.out.println();
                PrintFunction.printRepeatString(" ", STARTING_SPACE);
                System.out.println(">< The chosen command doesn't exist, please change it");
            }
        }
    }

    @Override
    public ViewSubTurn getSubTurn() {
        return buildViewer.getMySubTurn();
    }

    /**
     * Overloading of WTerminalSubTurnViewer's setMyWTerminalStatusViewer to set the correct WTerminalStatusViewer
     * @param myWTerminalStatusViewer
     */
    public void setMyWTerminalStatusViewer( WTerminalPlayingViewer myWTerminalStatusViewer) {
        this.myWTerminalStatusViewer = myWTerminalStatusViewer;
    }
}
