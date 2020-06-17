package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.board.placeables.PlaceableType;
import it.polimi.ingsw.model.card.adversaryMove.AdversaryMoveChecker;
import it.polimi.ingsw.model.card.build.BuildChecker;
import it.polimi.ingsw.model.card.build.BuildExecutor;
import it.polimi.ingsw.model.card.build.MyConstruction;
import it.polimi.ingsw.model.card.move.MoveChecker;
import it.polimi.ingsw.model.card.move.MoveExecutor;
import it.polimi.ingsw.model.card.move.MyMove;
import it.polimi.ingsw.model.card.win.WinChecker;
import it.polimi.ingsw.model.exceptions.LoseException;
import it.polimi.ingsw.model.exceptions.OutOfBoardException;
import it.polimi.ingsw.model.move.FloorDirection;
import it.polimi.ingsw.model.move.LevelDirection;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.worker.Worker;

import java.util.ArrayList;
import java.util.List;

public class CardParser {

    public static List<MoveChecker> getMoveCheckers(GodPower godPower) {
        List<MoveChecker> checkers = new ArrayList<>();

        /* ########################### DEFAULT RULES ########################### */
        MoveChecker defaultEffect = (move, worker, startingPosition, movesLeft, parentCard) -> {
            /* check if the Player can make a move in the first place */
            if(movesLeft <= 0)
                return false;

            /* cannot move into the same position */
            if(MyMove.isSameCell(move.getSelectedCell(), worker.position()))
                return false;

            /* cannot move beyond adjoining cells */
            if(MyMove.beyondAdjacentCells(move.getSelectedCell(), worker.position()))
                return false;

            /* cannot go more than one level up*/
            if(MyMove.tooHighCell(move.getSelectedCell(), worker.position()))
                return false;

            /* cannot go if there is a Dome */
            if(MyMove.domedCell(move.getSelectedCell()))
                return false;

            return true; // everything ok
        };
        checkers.add(defaultEffect);

        if(!godPower.isActiveOnMyMovement()) {
            MoveChecker effect = (move, worker, startingPosition, movesLeft, parentCard) -> {
                /* cannot go if there is another Worker */
                if(MyMove.occupiedCell(move.getSelectedCell()))
                    return false;
                return true; // everything ok
            };
            checkers.add(effect);
        }
        else {
            /* ################### OPPONENT SPACE ######################## */
            if(!godPower.isMoveIntoOpponentSpace()) {
                checkers.add((move, worker, startingPosition, movesLeft, parentCard) -> {
                    if (MyMove.occupiedCell(move.getSelectedCell()))
                        return false;
                    return true;
                });
            }
            else{
                checkers.add((move, worker, startingPosition, movesLeft, parentCard) -> {
                    boolean checkResult = true;
                    // perform additional controls only in cas the selectedCell is already occupied
                    // todo: add a control to check that the worker is opponent's worker
                    if(MyMove.occupiedCell(move.getSelectedCell())) {
                        // check that the worker is opponent's worker
                        if (move.getSelectedCell().getWorker().getOwner().equals(worker.getOwner()))
                            return false;

                        // just apollo can have ANY as a FloorDirection (it means to move the worker into the vacant Cell)
                        // todo: apollo (just to check, REMOVE THIS COMMENT)
                        if (godPower.getForceOpponentInto() != FloorDirection.ANY) {
                            // todo: minotaur (just to check, REMOVE THIS COMMENT)
                            // check if the opponent's Worker can be moved into the right cell (as with Minotaur Card's power)
                            Cell nextOpponentCell;
                            try {
                                nextOpponentCell = MyMove.calculateNextCell(move);
                            } catch (OutOfBoardException ex) {
                                return false;
                            }
                            checkResult = MyMove.checkNextCell(nextOpponentCell); // check if opponent's Worker can be forced into the next calculated Cell
                            if (checkResult == false)
                                return false;
                        }
                    }

                    return true; // everything ok
                });
            }


            /* ###################### STARTING SPACE DENIED ####################### */
            /* cannot move back into the initial space */
            // todo: artemis (just to check, REMOVE THIS COMMENT)
            if(godPower.isStartingSpaceDenied()) {
                checkers.add((move, worker, startingPosition, movesLeft, parentCard) -> {
                    /* Check if a movement has already occurred with the selected Worker */
                    if(parentCard.hasExecutedMovement()) {
                        if (MyMove.isSameCell(move.getSelectedCell(), startingPosition))
                            return false;
                    }
                    else { /* Otherwise, just register the starting position */
                        parentCard.getMyMove().setStartingPosition(worker.position());
                    }
                    return true; // everything ok
                });
            }


            /* ########################## OTHER #################################*/
            /* check for denied move Direction when performing Construction before Movement */
            // todo: prometheus (just to check, REMOVE THIS COMMENT)
            if(godPower.getHotLastMoveDirection() != LevelDirection.NONE) {
                checkers.add((move, worker, startingPosition, movesLeft, parentCard) -> {
                    if(parentCard.getMyConstruction().getConstructionLeft() <= 1)
                        if(move.getLevelDirection() == godPower.getHotLastMoveDirection())
                            return false;

                    return true; // everything ok
                });
            }
        }

        return checkers;
    }

    public static MoveExecutor getMoveExecutor(GodPower godPower) {
        MoveExecutor executor;
        if(!godPower.isActiveOnMyMovement()) {
            executor = (move, worker, parentCard) -> {
                /* default movement execution */
                return worker.place(move.getSelectedCell());
            };
        }
        else {
            executor = (move, worker, parentCard) -> {
                boolean placed = true;
                /* special rules when performing a Movement */
                // if the Cell is occupied, force the opponent's Worker into another Cell otherwise perform the Movement
                if(MyMove.occupiedCell(move.getSelectedCell())) {
                    // todo: apollo (just to check, REMOVE THIS COMMENT)
                    if (godPower.getForceOpponentInto() == FloorDirection.ANY) {
                        Worker opponentWorker = move.getSelectedCell().removeWorker(); // get opponent's Worker from its Cell
                        Cell myWorkerCurrentPosition = worker.position();
                        placed = worker.place(move.getSelectedCell()); // place my Worker into the selected Cell
                        String oppWorkerId = opponentWorker.getWorkerId();
                        int initialX = opponentWorker.position().getX(); // todo: check if this works correctly
                        int initialY = opponentWorker.position().getY(); // todo: check if this works correctly
                        MyMove.forceMove(opponentWorker, myWorkerCurrentPosition); // force movement for opponent's worker
                        int finalX = opponentWorker.position().getX();
                        int finalY = opponentWorker.position().getY();
                        parentCard.getMyMove().setOpponentForcedMove(new MyMove.WorkerMoved(oppWorkerId, initialX, initialY, finalX, finalY));
                    }
                    else if(godPower.getForceOpponentInto() == FloorDirection.SAME) { // todo: minotaur (just to check, REMOVE THIS COMMENT)
                        Worker opponentWorker = move.getSelectedCell().getWorker(); // get opponent's Worker
                        String oppWorkerId = opponentWorker.getWorkerId();
                        int initialX = opponentWorker.position().getX(); // todo: check if this works correctly
                        int initialY = opponentWorker.position().getY(); // todo: check if this works correctly
                        MyMove.forceMove(opponentWorker, MyMove.calculateNextCell(move)); // force movement for opponent's worker
                        int finalX = opponentWorker.position().getX();
                        int finalY = opponentWorker.position().getY();
                        parentCard.getMyMove().setOpponentForcedMove(new MyMove.WorkerMoved(oppWorkerId, initialX, initialY, finalX, finalY));
                        placed = worker.place(move.getSelectedCell()); // place my Worker into the selected Cell
                    }
                }
                else {
                    // todo: artemis (just to check, REMOVE THIS COMMENT)
                    placed = worker.place(move.getSelectedCell());
                }
                return placed;
            };
        }

        return executor;
    }










    public static List<BuildChecker> getBuildCheckers(GodPower godPower) {
        List<BuildChecker> checkers = new ArrayList<>();

        /* ########################## DEFAULT RULES ######################## */
        BuildChecker defaultEffect = (move, worker, lastMove, constructionLeft, parentCard) -> {
            /* check if the Player can make a construction in the first place */
            if(constructionLeft <= 0)
                return false;

            /* cannot build into the same position */
            if(MyConstruction.isSameCell(move.getSelectedCell(), worker.position()))
                return false;

            /* cannot build beyond adjoining cells */
            if(MyConstruction.beyondAdjacentCells(move.getSelectedCell(), worker.position()))
                return false;

            /* Workers can build at any level height, by default rules */

            /* cannot build if there is another Worker */
            if(MyConstruction.occupiedCell(move.getSelectedCell()))
                return false;

            /* cannot build if there is a Dome */
            if(MyConstruction.domedCell(move.getSelectedCell()))
                return false;

            return true; // everything ok
        };
        checkers.add(defaultEffect);


        /* ####################### SPECIAL RULES ######################## */
        if(!godPower.isActiveOnMyConstruction()) {
            checkers.add((move, worker, lastMove, constructionLeft, parentCard) -> {
                /* cannot build a Dome at any level */
                if(move.getBlockType() == PlaceableType.DOME && move.getSelectedCell().getLevel() < 3)
                    return false;
                return true; // everything ok
            });
        }
        else {
            /* cannot build a Dome at any level */
            // todo: atlas (just to check, REMOVE THIS COMMENT)
            if(!godPower.isDomeAtAnyLevel())
                checkers.add((move, worker, lastMove, constructionLeft, parentCard) -> {
                    if(move.getBlockType() == PlaceableType.DOME && move.getSelectedCell().getLevel() < 3)
                        return false;
                    return true; // everything ok
                });


            /* cannot build on the same space (for additional-time constructions) */
            // todo: demeter (just to check, REMOVE THIS COMMENT)
            if(godPower.isSameSpaceDenied())
                checkers.add((move, worker, lastMove, constructionLeft, parentCard) -> {
                    if(parentCard.hasExecutedConstruction() && move.getSelectedCell().equals(lastMove.getSelectedCell())) // if(lastMove != null && constructionLeft == 1 && move.getSelectedCell().equals(lastMove.getSelectedCell()))
                        return false;
                    return true; // everything ok
                });


            /* force build on the same space (for additional-time constructions) */
            // todo: hephaestus (just to check, REMOVE THIS COMMENT)
            if(godPower.isForceConstructionOnSameSpace())
                checkers.add((move, worker, lastMove, constructionLeft, parentCard) -> {
                    if(parentCard.hasExecutedConstruction() && (!move.getSelectedCell().equals(lastMove.getSelectedCell()) || move.getBlockType() == PlaceableType.DOME))
                        return false;
                    return true; // everything ok
                });
        }

        return checkers;
    }





    public static BuildExecutor getBuildExecutor(GodPower godPower) {
        BuildExecutor executor = (move, worker) -> {
            /* default construction execution */
            if(move.getBlockType() == PlaceableType.BLOCK)
                return move.getSelectedCell().buildBlock();
            else if(move.getBlockType() == PlaceableType.DOME)
                return move.getSelectedCell().buildDome();
            else
                return false;
        };
        return executor;
    }







    public static List<WinChecker> getWinCheckers(GodPower godPower) {
        List<WinChecker> checkers = new ArrayList<>();


        /* ##################### DEFAULT RULES ###################### */
        WinChecker defaultEffect = (move, worker) -> {
            /* Default rules: a player win if and only if its Worker moves up on top of level 3 */
            /* check if the Worker was on a lower level before the move (by checking its last move was on UP Direction) */
            if(move.getLevelDirection() != LevelDirection.UP)
                return false;

            /* check if it is now onto a level 3 position */
            if(worker.position().getLevel() != 3)
                return false;

            return true; // it's a win
        };
        checkers.add(defaultEffect);


        /* ####################### SPECIAL RULES ######################### */
        if(godPower.isNewVictoryCondition()) {
            checkers.add((move, worker) -> {
                /* check for new Victory condition first */
                if(move.getLevelDirection() == godPower.getHotLastMoveDirection())
                    // todo: pan (just to check, REMOVE THIS COMMENT)
                    if(move.getLevelDepth() <= godPower.getHotLevelDepth())
                        return true;

                return false;
            });
        }


        return checkers;
    }





    public static List<AdversaryMoveChecker> getAdversaryMoveCheckers(GodPower godPower) {
        List<AdversaryMoveChecker> checkers = new ArrayList<>();

        /* move can be denied only if the God's power has to be applied to opponent's move */
        if(godPower.isActiveOnOpponentMovement()) {
            AdversaryMoveChecker specialEffect = (move, worker, parentCard) -> {
                Move myLastMove = parentCard.getMyMove().getLastMove();

                /* check if my last move was one of the Hot Last Moves checked by my God's power:
                 * in this case, check the opponent's move
                 */
                // todo: athena (just to check, REMOVE THIS COMMENT)
                if(myLastMove != null && myLastMove.getLevelDirection() == godPower.getHotLastMoveDirection())
                    if(move.getLevelDirection() == godPower.getOpponentDeniedDirection()) {
                        if(godPower.isMustObey()) {
                            // if the God's power must be obeyed, and it's not, trigger a Lose Condition
                            throw new LoseException(worker.getOwner(),"Player " + worker.getOwner().getNickname() + "has lost! (By not respecting Opponent Card's power)");
                        }
                        else {
                            return false;
                        }
                    }

                return true; // everything ok
            };
            checkers.add(specialEffect);
        }
        else {
            AdversaryMoveChecker defaultEffect = (move, worker, parentCard) -> true;
            checkers.add(defaultEffect);
        }

        return checkers;
    }
}
