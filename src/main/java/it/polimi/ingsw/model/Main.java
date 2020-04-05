package it.polimi.ingsw.model;

import com.google.gson.Gson;

/**
 * Main class for the Model package
 */
public class Main {
    // TODO: maybe to remove
    public static void main(String[] args) {
        GodPower godPower = new GodPower();

        // set properties
        godPower.setMovementsLeft(1);
        godPower.setConstructionLeft(1);
        godPower.setSameSpaceDenied(false);

        godPower.setHotLastMoveDirection(LevelDirection.UP);
        godPower.setMoveIntoOpponentSpace(true);
        godPower.setForceOpponentInto(FloorDirection.NORTH);
        godPower.setDeniedDirection(LevelDirection.SAME);

        godPower.setOpponentDeniedDirection(LevelDirection.DOWN);

        godPower.setDomeAtAnyLevel(true);
        godPower.setForceConstructionOnSameSpace(false);

        godPower.setNewVictoryCondition(true);

        Gson gson = new Gson();
        String json = gson.toJson(godPower);

        System.out.println(json);
    }
}
