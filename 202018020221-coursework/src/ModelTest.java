import model.Model;
import model.impl.ModelImpl;
import pojo.CharacterState;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * A test class for the Model implementation.
 */
class ModelTest {

    /**
     * Check whether the model is successfully initialized.
     * @ensures \result != null // The result of getNowEquation should not be null after initialization.
     */
    @org.junit.jupiter.api.Test
    void init() {
        Model model=new ModelImpl();
        model.init();
        assertNotNull(model.getNowEquation());
    }

    /**
     * Check whether the model is reset.
     * @ensures \result == null // The result of getNowEquation should be null after reset.
     */
    @org.junit.jupiter.api.Test
    void reset() {
        Model model=new ModelImpl();
        model.init();
        assertNotNull(model.getNowEquation());
        model.reset();
        assertNull(model.getNowEquation());
    }

    /**
     * Tests whether assert has taken effect.
     * Tests whether the returned status is correct.
     * @requires model.getNowEquation() != null
     * @ensures (\forall CharacterState state; inputState.contains(state); state == CharacterState.CORRECT)
     */
    @org.junit.jupiter.api.Test
    void getInputState() {
        Model model=new ModelImpl();
        assertThrows(AssertionError.class,()->model.getInputState("1234567"));
        model.init();
        List<CharacterState> inputState = model.getInputState(model.getNowEquation());
        for(CharacterState characterState:inputState){
            assertEquals(CharacterState.CORRECT,characterState);
        }
    }

    /**
     * Tests whether the passed equation is valid.
     * @ensures model.isCorrectEquation("1+2+3=6") == true
     * @ensures model.isCorrectEquation("1*3=1+2") == true
     * @ensures model.isCorrectEquation("111=111") == false
     * @ensures model.isCorrectEquation("1+2+3=9") == false
     */
    @org.junit.jupiter.api.Test
    void isCorrectEquation() {
        Model model=new ModelImpl();
        assertTrue(model.isCorrectEquation("1+2+3=6"));
        assertTrue(model.isCorrectEquation("1*3=1+2"));
        assertFalse(model.isCorrectEquation("111=111"));
        assertFalse(model.isCorrectEquation("1+2+3=9"));
    }
}