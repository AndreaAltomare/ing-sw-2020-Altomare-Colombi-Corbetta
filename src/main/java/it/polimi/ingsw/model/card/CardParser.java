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

/**
 * Parser for GodPower objects.
 *
 * <p>Main responsibility of {@code CardParser} is
 * to compose Lists of Lambda expressions out of
 * GodPower objects given in input to its methods.
 *
 * <p>Since GodPower objects are extracted from
 * Cards' configuration files, {@code CardParser}
 * can be meant as an <i>high level</i> parser for
 * those configuration files.
 *
 * <p>GodPower objects are extracted by {@link
 * it.polimi.ingsw.storage.ResourceManager#callGodPower(String)
 * ResourceManager.callGodPower(cardName)} method
 * when a Player chooses his/her Card.
 *
 * @author AndreaAltomare
 */
public class CardParser {

    /**
     * Composes and gets Lambda checkers for Movement moves
     * out of a {@code GodPower} object.
     *
     * @param godPower GodPower object
     * @return A list of Lambda checkers
     */
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

        /* ############################# SPECIAL RULES ############################### */
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
                    // perform additional controls only in cas the selectedCell is already occupied
                    if(MyMove.occupiedCell(move.getSelectedCell())) {
                        // check that the worker is opponent's worker
                        if (move.getSelectedCell().getWorker().getOwner().equals(worker.getOwner()))
                            return false;
                    }

                    return true; // everything ok
                });

                // ANY as a FloorDirection means to move the worker into the vacant Cell
                if (godPower.getForceOpponentInto() != FloorDirection.ANY) {
                    checkers.add((move, worker, startingPosition, movesLeft, parentCard) -> {
                        boolean checkResult = true;

                        if(MyMove.occupiedCell(move.getSelectedCell())) {
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

                        return true;
                    });
                }
            }


            /* ###################### STARTING SPACE DENIED ####################### */
            /* cannot move back into the initial space */
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

    /**
     * Gets a Lambda executor for Movement moves
     * out of a {@code GodPower} object.
     *
     * @param godPower GodPower object
     * @return A Lambda executor
     */
    public static MoveExecutor getMoveExecutor(GodPower godPower) {
        MoveExecutor executor;

        /* ################################# DEFAULT RULES ################################ */
        if(!godPower.isActiveOnMyMovement()) {
            executor = (move, worker, parentCard) -> {
                /* default movement execution */
                return worker.place(move.getSelectedCell());
            };
        }
        else {
            /* ############################ SPECIAL RULES ################################# */
            if (godPower.getForceOpponentInto() == FloorDirection.ANY) {
                executor = (move, worker, parentCard) -> {
                    boolean placed = true;

                    /* special rules when performing a Movement */
                    // if the Cell is occupied, force the opponent's Worker into another Cell otherwise perform the Movement
                    if(MyMove.occupiedCell(move.getSelectedCell())) {
                        Worker opponentWorker = move.getSelectedCell().removeWorker(); // get opponent's Worker from its Cell
                        Cell myWorkerCurrentPosition = worker.position();
                        placed = worker.place(move.getSelectedCell()); // place my Worker into the selected Cell
                        String oppWorkerId = opponentWorker.getWorkerId();
                        int initialX = opponentWorker.position().getX();
                        int initialY = opponentWorker.position().getY();
                        MyMove.forceMove(opponentWorker, myWorkerCurrentPosition); // force movement for opponent's worker
                        int finalX = opponentWorker.position().getX();
                        int finalY = opponentWorker.position().getY();
                        parentCard.getMyMove().setOpponentForcedMove(new MyMove.WorkerMoved(oppWorkerId, initialX, initialY, finalX, finalY));
                    }
                    else
                        placed = worker.place(move.getSelectedCell());

                    return placed;
                };
            }
            else if(godPower.getForceOpponentInto() == FloorDirection.SAME) {
                executor = (move, worker, parentCard) -> {
                    boolean placed = true;

                    /* special rules when performing a Movement */
                    // if the Cell is occupied, force the opponent's Worker into another Cell otherwise perform the Movement
                    if(MyMove.occupiedCell(move.getSelectedCell())) {
                        Worker opponentWorker = move.getSelectedCell().getWorker(); // get opponent's Worker
                        String oppWorkerId = opponentWorker.getWorkerId();
                        int initialX = opponentWorker.position().getX();
                        int initialY = opponentWorker.position().getY();
                        MyMove.forceMove(opponentWorker, MyMove.calculateNextCell(move)); // force movement for opponent's worker
                        int finalX = opponentWorker.position().getX();
                        int finalY = opponentWorker.position().getY();
                        parentCard.getMyMove().setOpponentForcedMove(new MyMove.WorkerMoved(oppWorkerId, initialX, initialY, finalX, finalY));
                        placed = worker.place(move.getSelectedCell()); // place my Worker into the selected Cell
                    }
                    else
                        placed = worker.place(move.getSelectedCell());

                    return placed;
                };
            }
            else {
                executor = (move, worker, parentCard) -> {
                    if(MyMove.occupiedCell(move.getSelectedCell()))
                        return false;
                    else
                        return worker.place(move.getSelectedCell());
                };
            }
        }

        return executor;
    }








    /**
     * Composes and gets Lambda checkers for Construction moves
     * out of a {@code GodPower} object.
     *
     * @param godPower GodPower object
     * @return A list of Lambda checkers
     */
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
            if(!godPower.isDomeAtAnyLevel())
                checkers.add((move, worker, lastMove, constructionLeft, parentCard) -> {
                    if(move.getBlockType() == PlaceableType.DOME && move.getSelectedCell().getLevel() < 3)
                        return false;
                    return true; // everything ok
                });


            /* cannot build on the same space (for additional-time constructions) */
            if(godPower.isSameSpaceDenied())
                checkers.add((move, worker, lastMove, constructionLeft, parentCard) -> {
                    if(parentCard.hasExecutedConstruction() && move.getSelectedCell().equals(lastMove.getSelectedCell())) // if(lastMove != null && constructionLeft == 1 && move.getSelectedCell().equals(lastMove.getSelectedCell()))
                        return false;
                    return true; // everything ok
                });


            /* force build on the same space (for additional-time constructions) */
            if(godPower.isForceConstructionOnSameSpace())
                checkers.add((move, worker, lastMove, constructionLeft, parentCard) -> {
                    if(parentCard.hasExecutedConstruction() && (!move.getSelectedCell().equals(lastMove.getSelectedCell()) || move.getBlockType() == PlaceableType.DOME))
                        return false;
                    return true; // everything ok
                });
        }

        return checkers;
    }





    /**
     * Gets a Lambda executor for Construction moves
     * out of a {@code GodPower} object.
     *
     * @param godPower GodPower object
     * @return A Lambda executor
     */
    public static BuildExecutor getBuildExecutor(GodPower godPower) {
        /* ######################### BOTH DEFAULT AND SPECIAL RULES ####################### */
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







    /**
     * Composes and gets Lambda checkers for Win conditions
     * out of a {@code GodPower} object.
     *
     * @param godPower GodPower object
     * @return A list of Lambda checkers
     */
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
                    if(move.getLevelDepth() <= godPower.getHotLevelDepth())
                        return true;

                return false;
            });
        }


        return checkers;
    }





    /**
     * Composes and gets Lambda checkers for Adversary's
     * Movement moves out of a {@code GodPower} object.
     *
     * @param godPower GodPower object
     * @return A list of Lambda checkers
     */
    public static List<AdversaryMoveChecker> getAdversaryMoveCheckers(GodPower godPower) {
        List<AdversaryMoveChecker> checkers = new ArrayList<>();

        /* ################################ SPECIAL RULES ################################# */
        /* move can be denied only if the God's power has to be applied to opponent's move */
        if(godPower.isActiveOnOpponentMovement()) {
            AdversaryMoveChecker specialEffect = (move, worker, parentCard) -> {
                Move myLastMove = parentCard.getMyMove().getLastMove();

                /* check if my last move was one of the Hot Last Moves checked by my God's power:
                 * in this case, check the opponent's move
                 */
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
            /* ######################### DEFAULT RULES ########################## */
            AdversaryMoveChecker defaultEffect = (move, worker, parentCard) -> true;
            checkers.add(defaultEffect);
        }

        return checkers;
    }
}
