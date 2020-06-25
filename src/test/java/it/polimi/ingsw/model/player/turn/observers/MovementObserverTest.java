package it.polimi.ingsw.model.player.turn.observers;

import it.polimi.ingsw.model.board.Cell;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.GodPower;
import it.polimi.ingsw.model.card.adversaryMove.AdversaryMove;
import it.polimi.ingsw.model.card.adversaryMove.AdversaryMoveChecker;
import it.polimi.ingsw.model.exceptions.DeniedMoveException;
import it.polimi.ingsw.model.exceptions.LoseException;
import it.polimi.ingsw.model.move.Move;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.worker.Worker;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovementObserverTest {

    private class MyAdversaryObserver extends AdversaryMove {
        boolean allowed;
        boolean loose;

        Move move;
        Worker worker;

        private String myId;

        MyAdversaryObserver(GodPower gp){
            super(new Card(gp, null, null, null, null, null, null), gp, null);
            loose = false;
            allowed = true;

            move = null;
            worker = null;
        }

        MyAdversaryObserver(GodPower gp, String id){
            this(gp);
            myId = id;
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

        public String getMyId() {
            return myId;
        }
    }

    @Test
    void check(){
        String maoID = "Test";

        GodPower gp = new GodPower();
        gp.setMovementsLeft(3);
        gp.setConstructionLeft(3);
        MyAdversaryObserver mao = new MyAdversaryObserver(gp, maoID);
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

        assertEquals(((MyAdversaryObserver) tested.getAdversaryMoveObserver()).getMyId(), maoID);
    }

    @Test
    void getAdversaryMoveObserver() {
        GodPower gp = new GodPower();
        gp.setMovementsLeft(3);
        gp.setConstructionLeft(3);
        MyAdversaryObserver mao1 = new MyAdversaryObserver(gp, "test1");
        MyAdversaryObserver mao2 = new MyAdversaryObserver(gp, "test2");

        MovementObserver tested1 = new MovementObserver(mao1);
        MovementObserver tested2 = new MovementObserver(mao2);

        MyAdversaryObserver maoR1 = (MyAdversaryObserver)tested1.getAdversaryMoveObserver();
        MyAdversaryObserver maoR2 = (MyAdversaryObserver)tested2.getAdversaryMoveObserver();

        assertEquals(mao1.getMyId(), maoR1.getMyId());
        assertEquals(mao2.getMyId(), maoR2.getMyId());

        assertNotEquals(mao1.getMyId(), maoR2.getMyId());
        assertNotEquals(mao2.getMyId(), maoR1.getMyId());

    }
}

//PREVIOUSLY MovementObserverTest.java :

/*
package it.polimi.ingsw.model;

        import it.polimi.ingsw.model.board.Cell;
        import it.polimi.ingsw.model.card.adversaryMove.AdversaryMove;
        import it.polimi.ingsw.model.card.Card;
        import it.polimi.ingsw.model.card.GodPower;
        import it.polimi.ingsw.model.exceptions.DeniedMoveException;
        import it.polimi.ingsw.model.exceptions.LoseException;
        import it.polimi.ingsw.model.move.Move;
        import it.polimi.ingsw.model.player.turn.observers.MovementObserver;
        import it.polimi.ingsw.model.player.Player;
        import it.polimi.ingsw.model.player.worker.Worker;
        import org.junit.jupiter.api.Test;

        import static org.junit.jupiter.api.Assertions.*;

class MovementObserverTest {

    private class MyAdversaryObserver extends AdversaryMove {
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

}*/
