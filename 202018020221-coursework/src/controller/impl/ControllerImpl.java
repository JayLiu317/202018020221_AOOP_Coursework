package controller.impl;

import controller.Controller;
import model.Model;
import model.impl.ModelImpl;
import pojo.CharacterState;
import java.util.List;

/**
 * A controller implementation that uses a model to manage game state.
 * @invariant model != null
 */
public class ControllerImpl implements Controller {
    private Model model=new ModelImpl();

    /**
     * Initializes the controller and the underlying model.
     * @ensures model != null
     */
    public ControllerImpl(){
        model.init();
    }

    /**
     * Retrieves the state of each character in the input compared to the current equation.
     * @param string the string to compare with the current equation
     * @return a list of CharacterState indicating the state of each character
     * @requires model != null
     * @ensures \result != null
     */
    @Override
    public List<CharacterState> getInputState(String string) {
        return model.getInputState(string);
    }

    /**
     * Determines if the game has been won based on the states of characters.
     * @param states the list of character states to evaluate
     * @return true if all states are CORRECT, false otherwise
     * @requires states != null
     * @ensures \result == (\forall int i; 0 <= i && i < states.size(); states.get(i) == CharacterState.CORRECT)
     */
    @Override
    public boolean isWin(List<CharacterState> states) {
        for(CharacterState state:states){
            if(state!=CharacterState.CORRECT){
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the current equation from the model.
     * @return the current equation as a string
     * @requires model != null
     * @ensures \result.equals(model.getNowEquation())
     */
    @Override
    public String getNowEquation() {
        return model.getNowEquation();
    }

    /**
     * Resets the game state by reinitializing the model.
     * @ensures model != null
     */
    public void reset() {
        model.reset();
        model.init();
    }

    /**
     * Checks if the given input is a correct equation according to the model.
     * @param input the input to check
     * @return true if the input is a correct equation, false otherwise
     * @requires model != null
     * @ensures \result == model.isCorrectEquation(input)
     */
    @Override
    public boolean isCorrectEquation(String input) {
        return model.isCorrectEquation(input);
    }

    /**
     * Gets the checkValid status from the model.
     * @return the checkValid status
     * @requires model != null
     * @ensures \result == model.getCheck()
     */
    @Override
    public boolean getCheckValid() {
        return model.getCheck();
    }

    /**
     * Gets the displayEquation status from the model.
     * @return the displayEquation status
     * @requires model != null
     * @ensures \result == model.getDisplay()
     */
    @Override
    public boolean getDisplayEquation() {
        return model.getDisplay();
    }
}