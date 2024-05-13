package controller;

import pojo.CharacterState;
import java.util.List;

/**
 * An interface for the game controller.
 */
public interface Controller {

    /**
     * Retrieves the state of each character in the input compared to the current equation.
     * @param string the string to compare with the current equation
     * @return a list of CharacterState indicating the state of each character
     * @requires string != null
     * @ensures \result != null
     */
    List<CharacterState> getInputState(String string);

    /**
     * Determines if the game has been won based on the states of characters.
     * @param states the list of character states to evaluate
     * @return true if all states are CORRECT, false otherwise
     * @requires states != null
     * @ensures \result == (\forall int i; 0 <= i && i < states.size(); states.get(i) == CharacterState.CORRECT)
     */
    boolean isWin(List<CharacterState> states);

    /**
     * Gets the current equation.
     * @return the current equation as a string
     * @ensures \result != null
     */
    String getNowEquation();

    /**
     * Resets the game state.
     */
    void reset();

    /**
     * Checks if the given input is a correct equation.
     * @param input the input to check
     * @return true if the input is a correct equation, false otherwise
     * @requires input != null
     * @ensures \result == (the input is a valid equation according to the game rules)
     */
    boolean isCorrectEquation(String input);

    /**
     * Gets the checkValid status.
     * @return the checkValid status
     * @ensures \result != null
     */
    boolean getCheckValid();

    /**
     * Gets the displayEquation status.
     * @return the displayEquation status
     * @ensures \result != null
     */
    boolean getDisplayEquation();
}