package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovementObserverTest {

    private class MyAdversaryObserver extends AdversaryMove{
        boolean allowed;
        boolean loose;

        Move move;
        Worker worker;

        MyAdversaryObserver(GodPower gp){
            super(new Card(gp), gp);
            loose = false;
            allowed = true;

            move = null;
            worker = null;
        }

        public boolean checkMove(Move move, Worker worker) throws LoseException {
            this.move = move;
            this.worker = worker;

            if(!allowed)
                return false;
            if (loose)
                throw new LoseException(new Player("test player"), "testing loose");
            return true;
        }

    }

    @Test
    void check(){
        GodPower gp = new GodPower();
        gp.setMovementsLeft(3);
        gp.setConstructionLeft(3);
        MyAdversaryObserver mao = new MyAdversaryObserver(gp);
        MovementObserver tested = new MovementObserver(mao);

        Move move = new Move(new Cell(0, 0, null), new Cell(0, 0, null));
        Worker worker = new Worker(new Player("worker owner"));

        //Testing that it'll throw no exception if the movment is allowed:
        mao.allowed = true;
        mao.loose = false;

        try{
            tested.check(move, worker);
        }catch (Exception e){
            fail();
        }

        assertTrue((move == mao.move)&&(worker == mao.worker));

        //Testing that if the move is denied it'll throw DeniedException
        mao.allowed = false;
        mao.loose = false;
        boolean denied = false;

        try{
            tested.check(move, worker);
        }catch (DeniedMoveException e){
            denied = true;
        }catch (Exception e){
            fail();
        }
        if(!denied)
            fail();

        assertTrue(denied&&(move == mao.move)&&(worker == mao.worker));

        //testing if it'll throw loose exception if it shall
        mao.allowed = true;
        mao.loose = true;
        boolean loose = false;

        try{
            tested.check(move, worker);
        }catch (LoseException e){
            loose = true;
        }catch (Exception e){
            fail();
        }
        if(!loose)
            fail();

        assertTrue(loose&&(move == mao.move)&&(worker == mao.worker));

        //Checking that it'll throw denied exception if movment is denied and not making the adversary win
        mao.allowed = false;
        mao.loose = true;
        denied = false;

        try{
            tested.check(move, worker);
        }catch (DeniedMoveException e){
            denied = true;
        }catch (Exception e){
            fail();
        }
        if(!denied)
            fail();

        assertTrue(denied&&(move == mao.move)&&(worker == mao.worker));
    }

}