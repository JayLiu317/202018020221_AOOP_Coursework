package pojo;

/**
 * An enumeration representing the state of a character in the game.
 * This state indicates whether a character is not present in the equation,
 * is in the incorrect location, or is correctly placed.
 */
public enum CharacterState {
    NOT_EXIST, // The character is not present in the equation at all.
    INCORRECT_LOCATION, // The character is present in the equation but in the wrong location.
    CORRECT // The character is correctly placed in the equation.
}