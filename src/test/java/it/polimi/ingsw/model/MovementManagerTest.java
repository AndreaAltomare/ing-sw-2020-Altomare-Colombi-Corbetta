package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovementManagerTest {

    private class MyObserver extends TurnObserver {

        private Worker work;
        private Move movment;
        boolean denied;
        boolean lose;

        MyObserver(){
            work = null;
            movment = null;
            denied = false;
            lose = false;
        }

        @Override
        public void check(Move move, Worker worker) throws DeniedMoveException, LoseException {
            if(denied)
                throw new DeniedMoveException();
            if(lose)
                throw new LoseException(new Player("Nemo"));
            if(work!=null){
                System.out.println("Called too many times!!");
                fail();
            }
            work = worker;
            movment = move;
        }

        Worker popWorker(){
            Worker ret = work;
            work = null;
            return ret;
        }

        Move popMovement(){
            Move ret = movment;
            movment = null;
            return ret;
        }
    }

    private static class MyMovement extends Move{

        private FloorDirection myFloorDirection;
        private LevelDirection myLevelDirection;
        private Cell mySelectedCell;

        MyMovement(FloorDirection direction, LevelDirection level, Cell cell) {
            super(null, null);
            myFloorDirection = direction;
            myLevelDirection = level;
            mySelectedCell = cell;
        }

        public FloorDirection getFloorDirection() {
            return myFloorDirection;
        }

        public LevelDirection getLevelDirection() {
            return myLevelDirection;
        }

        public Cell getSelectedCell() {
            return mySelectedCell;
        }
    }

    private MovementManagerTest.MyObserver obs1 = new MovementManagerTest.MyObserver();
    private MovementManagerTest.MyObserver obs2 = new MovementManagerTest.MyObserver();
    private MovementManagerTest.MyObserver obs3 = new MovementManagerTest.MyObserver();
    private MovementManagerTest.MyObserver obs4 = new MovementManagerTest.MyObserver();

    private Worker myWorker = new Worker(null);
    private Move myMove = new MovementManagerTest.MyMovement(FloorDirection.SAME, LevelDirection.SAME, new Cell(0, 0, null));

    private boolean checkAllNull(){
        return ((obs1.popWorker()==null && obs1.popMovement()==null) && (obs2.popWorker()==null && obs2.popMovement()==null) && (obs3.popWorker()==null && obs3.popMovement()==null) && (obs4.popWorker()==null && obs4.popMovement()==null));
    }

    private boolean checkMyObserver(MovementManagerTest.MyObserver obs){
        return (obs.popWorker().equals(myWorker) && obs.popMovement().equals(myMove));
    }


    @Test
    void notifyObservers(){
        //card is not used for this scope
        MovementManager tested = new MovementManager(null);
        //Check initial condition
        assertTrue(checkAllNull());

        //If no observer setted, none should be notified
        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertTrue(checkAllNull());

        //If set only one observer, only that should be notified
        tested.registerObservers(obs1);
        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertTrue(checkMyObserver(obs1));
        assertTrue(checkAllNull());

        //With 2 set observer, it should notify that 2...
        tested.registerObservers(obs2);
        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertTrue(checkMyObserver(obs1));
        assertTrue(checkMyObserver(obs2));
        assertTrue(checkAllNull());

        //Adding all observers
        tested.registerObservers(obs3);
        tested.registerObservers(obs4);
        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertTrue(checkMyObserver(obs1));
        assertTrue(checkMyObserver(obs2));
        assertTrue(checkMyObserver(obs3));
        assertTrue(checkMyObserver(obs4));
        assertTrue(checkAllNull());

        //Trying to unregister the first observer inserted
        tested.unregisterObservers(obs1);
        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertTrue(checkMyObserver(obs2));
        assertTrue(checkMyObserver(obs3));
        assertTrue(checkMyObserver(obs4));
        assertTrue(checkAllNull());

        //Re loading tested to unregister the last observer inserted
        tested = new MovementManager(null);

        tested.registerObservers(obs1);
        tested.registerObservers(obs2);
        tested.registerObservers(obs3);
        tested.registerObservers(obs4);
        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertTrue(checkMyObserver(obs1));
        assertTrue(checkMyObserver(obs2));
        assertTrue(checkMyObserver(obs3));
        assertTrue(checkMyObserver(obs4));
        assertTrue(checkAllNull());

        tested.unregisterObservers(obs4);
        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertTrue(checkMyObserver(obs1));
        assertTrue(checkMyObserver(obs2));
        assertTrue(checkMyObserver(obs3));
        assertTrue(checkAllNull());

        //Re loading tested to unregister a middle observer inserted
        tested = new MovementManager(null);

        tested.registerObservers(obs1);
        tested.registerObservers(obs2);
        tested.registerObservers(obs3);
        tested.registerObservers(obs4);
        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertTrue(checkMyObserver(obs1));
        assertTrue(checkMyObserver(obs2));
        assertTrue(checkMyObserver(obs3));
        assertTrue(checkMyObserver(obs4));
        assertTrue(checkAllNull());

        tested.unregisterObservers(obs3);
        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertTrue(checkMyObserver(obs1));
        assertTrue(checkMyObserver(obs2));
        assertTrue(checkMyObserver(obs4));
        assertTrue(checkAllNull());

        //Let's go messing it really up!!
        //"when the going gets tough, the tough get going"

        tested.registerObservers(obs3);
        tested.registerObservers(obs1);

        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertTrue(checkMyObserver(obs1));
        assertTrue(checkMyObserver(obs2));
        assertTrue(checkMyObserver(obs3));
        assertTrue(checkMyObserver(obs4));
        assertTrue(checkAllNull());

        tested.unregisterObservers(obs2);
        tested.unregisterObservers(obs4);
        tested.unregisterObservers(obs2);

        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertTrue(checkMyObserver(obs1));
        assertFalse(checkMyObserver(obs2));
        assertTrue(checkMyObserver(obs3));
        assertFalse(checkMyObserver(obs4));
        assertTrue(checkAllNull());

        tested.unregisterObservers(obs1);
        tested.unregisterObservers(obs3);

        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertFalse(checkMyObserver(obs1));
        assertFalse(checkMyObserver(obs2));
        assertFalse(checkMyObserver(obs3));
        assertFalse(checkMyObserver(obs4));
        assertTrue(checkAllNull());

        obs1.denied = true;
        tested.registerObservers(obs1);
        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertTrue(checkAllNull());

        tested.registerObservers(obs2);
        tested.registerObservers(obs3);
        tested.registerObservers(obs4);
        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertFalse(checkMyObserver(obs1));
        assertTrue(checkMyObserver(obs2));
        assertTrue(checkMyObserver(obs3));
        assertTrue(checkMyObserver(obs4));
        assertTrue(checkAllNull());

        obs3.denied = true;
        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertFalse(checkMyObserver(obs1));
        assertTrue(checkMyObserver(obs2));
        assertFalse(checkMyObserver(obs3));
        assertTrue(checkMyObserver(obs4));
        assertTrue(checkAllNull());

        tested.unregisterObservers(obs1);
        tested.unregisterObservers(obs4);
        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertFalse(checkMyObserver(obs1));
        assertTrue(checkMyObserver(obs2));
        assertFalse(checkMyObserver(obs3));
        assertFalse(checkMyObserver(obs4));
        assertTrue(checkAllNull());

        obs1.denied=false;
        obs3.denied=false;

        tested.registerObservers(obs1);
        tested.registerObservers(obs4);
        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            fail();
        }
        assertTrue(checkMyObserver(obs1));
        assertTrue(checkMyObserver(obs2));
        assertTrue(checkMyObserver(obs3));
        assertTrue(checkMyObserver(obs4));
        assertTrue(checkAllNull());

        obs3.lose = true;
        try {
            tested.notifyObservers(myMove, myWorker);
        } catch (LoseException e) {
            assertTrue(true);
        }
        assertFalse(checkMyObserver(obs1));
        assertTrue(checkMyObserver(obs2));
        assertFalse(checkMyObserver(obs3));
        assertFalse(checkMyObserver(obs4));
        assertTrue(checkAllNull());

        //YEAH, You did it!!
    }

    private class MyCard extends Card{

        private MyMove myMove;
        private MyConstruction myConstruction;
        private MyVictory myVictory;
        private AdversaryMove adversaryMove;
        private GodPower godPower;

        boolean movementExecuted;
        boolean constructionExecuted;

        protected class MyTestMove extends MyMove{

            private GodPower godPower;

            private int movesLeft;

            private Move movment;
            private Worker worker;

            private Move cmovment;
            private Worker cworker;

            boolean executable;
            boolean checkable;

            boolean outOfBound;
            boolean winExc;

            MyTestMove(){
                super(null, new GodPower());

                executable = true;
                checkable = true;


                movment = null;
                worker = null;
                cmovment = null;
                cworker = null;
            }

            public boolean executeMove(Move move, Worker worker) throws OutOfBoardException, WinException {
                if(outOfBound)
                    throw new OutOfBoardException("Testing");

                if(winExc)
                    throw new WinException(new Player("test winner"));

                if(executable) {
                    movment = move;
                    this.worker = worker;
                    return true;
                }
                return false;
            }

            public boolean checkMove(Move move, Worker worker) {
                if(checkable){
                    cmovment = move;
                    cworker = worker;
                    return true;
                }
                return false;
            }

            public GodPower getGodPower() {
                return godPower;
            }

            public void setGodPower(GodPower gp){
                godPower = gp;
            }

            public Move getLastMove() {
                Move ret;
                ret = movment;
                movment = null;
                return ret;
            }

            public int getMovesLeft() {
                return movesLeft;
            }

            public void decreaseMovesLeft() {
                movesLeft -= 1;
            }

            public void setMovesLeft(int t){
                movesLeft = t;
            }

            public void resetMovesLeft() {
                movesLeft = 0;
            }

            public Worker getWorker(){
                return worker;
            }

            public Move getCmovment(){
                return cmovment;
            }

            public Worker getCworker(){
                return cworker;
            }
        }

        protected class MyTestConstruction extends MyConstruction{

            private GodPower godPower;

            private int movesLeft;

            private Move movment;
            private Worker worker;

            private Move cmovment;
            private Worker cworker;

            boolean executable;
            boolean checkable;

            boolean outOfBound;
            boolean winExc;

            MyTestConstruction(){
                super(null, new GodPower());

                executable = true;
                checkable = true;


                movment = null;
                worker = null;
                cmovment = null;
                cworker = null;
            }

            public boolean executeMove(Move move, Worker worker) throws OutOfBoardException, WinException {
                if(outOfBound)
                    throw new OutOfBoardException("test exception");

                if(winExc)
                    throw new WinException(new Player("test winner"));

                if(executable) {
                    movment = move;
                    this.worker = worker;
                    return true;
                }
                return false;
            }

            public boolean checkMove(Move move, Worker worker) {
                if(checkable){
                    cmovment = move;
                    cworker = worker;
                    return true;
                }
                return false;
            }

            public GodPower getGodPower() {
                return godPower;
            }

            public void setGodPower(GodPower gp){
                godPower = gp;
            }

            public BuildMove getLastMove() {
                return null;
            }

            public Move popMovment(){
                Move ret = movment;
                movment = null;
                return ret;
            }

            public int getConstructionLeft() {
                return movesLeft;
            }

            public void decreaseConstructionLeft() {
                movesLeft -= 1;
            }

            public void setConstructionLeft(int t){
                movesLeft = t;
            }

            public void resetConstructionLeft() {
                movesLeft = 0;
            }

            public Worker getWorker(){
                return worker;
            }

            public Move getCmovment(){
                return cmovment;
            }

            public Worker getCworker(){
                return cworker;
            }
        }

        protected class MyTestVictory extends MyVictory{

            boolean checkable;
            private GodPower godPower;

            Move movment;
            Worker worker;

            MyTestVictory(){
                super(null, new GodPower());
                godPower = null;
                movment = null;
                worker = null;
            }

            public boolean checkMove(Move move, Worker worker) {
                if(checkable) {
                    movment = move;
                    this.worker = worker;
                    return true;
                }
                return false;
            }

            public GodPower getGodPower() {
                return godPower;
            }

            Worker popWorker(){
                Worker ret = worker;
                worker = null;
                return ret;
            }

            Move popMovment(){
                Move ret = movment;
                movment = null;
                return ret;
            }
        }

        protected class AdversaryTestMove extends AdversaryMove{
            boolean checked;
            boolean lose;

            Move movment;
            Worker worker;

            AdversaryTestMove(){
                super(null, new GodPower());
                checked = false;
                lose = false;

                movment = null;
                worker = null;
            }

            public boolean checkMove(Move move, Worker worker) throws LoseException {
                if (lose)
                    throw new LoseException(new Player("test player"));
                if(checked){
                    movment = move;
                    this.worker = worker;
                    return true;
                }
                return false;
            }

            Worker popWorker(){
                Worker ret = worker;
                worker = null;
                return ret;
            }


            Move popMovment(){
                Move ret = movment;
                movment = null;
                return ret;
            }
        }

        MyCard() {
            super(new GodPower());
            myMove = new MyTestMove();
            myConstruction = new MyTestConstruction();
            myVictory = new MyTestVictory();
            adversaryMove = new AdversaryTestMove();
        }

        void setGodPower(GodPower gp){
            godPower = gp;
        }

        public void resetForStart() {
            myMove.resetMovesLeft();
            myConstruction.resetConstructionLeft();

            /* Reset Movement and Construction executed */
            movementExecuted = false;
            constructionExecuted = false;
        }

        public MyMove getMyMove() {
            return myMove;
        }

        public MyConstruction getMyConstruction() {
            return myConstruction;
        }

        public MyVictory getMyVictory() {
            return myVictory;
        }

        public AdversaryMove getAdversaryMove() {
            return adversaryMove;
        }

        public boolean hasExecutedMovement() {
            return movementExecuted;
        }


        public void setMovementExecuted(boolean movementExecuted) {
            this.movementExecuted = movementExecuted;
        }

        public boolean hasExecutedConstruction() {
            return constructionExecuted;
        }

        public void setConstructionExecuted(boolean constructionExecuted) { this.constructionExecuted = constructionExecuted; }

        public GodPower getGodPower() {
            return godPower;
        }
    }


    @Test
    void handle() {

        MyCard myCard = new MyCard();
        GodPower gp = new GodPower();
        Worker myWorker = new Worker(new Player("test worker player"));
        MyCard.MyTestMove myMove = (MyCard.MyTestMove) myCard.getMyMove();
        Move movment = new MyMovement(FloorDirection.ANY, LevelDirection.ANY, new Cell(0, 0, null));

        myWorker.setChosen(ChooseType.CHOSEN);

        myMove.setMovesLeft(10);

        myCard.setGodPower(gp);
        gp.setBuildBeforeMovement(false);
        myCard.setConstructionExecuted(false);
        myCard.setMovementExecuted(true);
        myMove.checkable = true;


        MovementManager tested = new MovementManager(myCard);

        //Verifying that it'll make a rigth move
        try {
            tested.handle(movment, myWorker);
        }catch(Exception e){
            fail();
        }
        assertTrue((myMove.getLastMove().equals(movment))&&(myMove.getWorker().equals(myWorker)));

        //Verifying that if worker is not selected it'll throw WrongWorkerException
        myMove.setMovesLeft(10);
        myWorker.setChosen(ChooseType.NOT_CHOSEN);
        boolean notExcepted = true;
        try {
            tested.handle(movment, myWorker);
        } catch (WrongWorkerException e) {
            notExcepted = false;
        } catch (Exception e) {
            fail();
        }
        if (notExcepted)
            fail();
        assertTrue(true);

        myWorker.setChosen(ChooseType.CHOSEN);

        //Verifying that if there are no more construction to perform, it'll throw RunOutMovesException
        myMove.setMovesLeft(0);
        notExcepted = true;
        try {
            tested.handle(movment, myWorker);
        } catch (RunOutMovesException e) {
            notExcepted = false;
        } catch (Exception e) {
            fail();
        }
        if (notExcepted)
            fail();
        assertTrue(true);

        //Checking if it'll trhow loose exception
        myMove.setMovesLeft(10);
        myCard.setConstructionExecuted(false);

        tested.registerObservers(obs1);
        obs1.lose=true;

        notExcepted = true;
        try {
            tested.handle(movment, myWorker);
        } catch (LoseException e) {
            notExcepted = false;
        } catch (Exception e) {
            fail();
        }
        if (notExcepted)
            fail();
        assertTrue(true);
    }

    @Test
    void getMovesLeft() {
        MovementManagerTest.MyCard myCard = new MovementManagerTest.MyCard();
        GodPower gp = new GodPower();
        myCard.setGodPower(gp);
        MovementManager tested = new MovementManager(myCard);

        MovementManagerTest.MyCard.MyTestMove myMove = (MovementManagerTest.MyCard.MyTestMove)myCard.getMyMove();
        myMove.setMovesLeft(0);
        assertTrue(tested.getMovesLeft() == 0);
        myMove.setMovesLeft(1);
        assertTrue(tested.getMovesLeft() == 1);
        myMove.setMovesLeft(2);
        assertTrue(tested.getMovesLeft() == 2);
        myMove.setMovesLeft(-1);
        assertTrue(tested.getMovesLeft() <= 0);
        myMove.setMovesLeft(-5);
        assertTrue(tested.getMovesLeft() <= 0);
        myMove.setMovesLeft(100);
        assertTrue(tested.getMovesLeft() == 100);
    }
}