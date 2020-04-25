package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.interfaces.*;
import it.polimi.ingsw.model.Model;

import java.util.Observable;
//implements ClientGeneralListener, MoveListener, TurnStatusChangeListener, CardSelectionListener

/**
 * Game Controller.
 *
 * @author AndreaAltomare
 */
public class Controller implements GeneralListener {
    private final Model model; // Object reference to the Game Model

    // TODO: Write Javadoc here to explain, for example, why a Model object is passed by argument
    public Controller(Model model) {
        this.model = model;
    }

    /**
     * General update() method for Observer Pattern.
     *
     * @param o (Object object)
     */
    @Override
    public void update(Object o) {
        // todo code
    }
}
