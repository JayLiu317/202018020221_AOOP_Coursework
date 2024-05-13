package model;

import pojo.CharacterState;
import java.util.List;


public interface Model {
    /**
     * init model and set equation randomly
     */
    void init();

    /**
     * reset model
     */

    void reset();

    /**
     * Enter a string that returns the status of the equation for each character
     * @param input user input
     * @return
     */
    List<CharacterState> getInputState(String input);

    /**
     * Adds a line of user input to model
     * @param input user input
     */
    void addInputLine(String input);

    List<String> getAllInput();

    List<List<CharacterState>> getAllState();

    String getNowEquation();
    boolean isCorrectEquation(String input);

    boolean getCheck();

    boolean getDisplay();
}