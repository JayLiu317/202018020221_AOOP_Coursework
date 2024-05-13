import view.View;
import javax.swing.*;
import java.io.IOException;
import java.util.Locale;

/**
 * A GUI class to start the Numberle game.
 */
public class GUI {
    /**
     * The main entry point for the GUI.
     * @ensures Locale.getDefault() == Locale.ENGLISH
     * @ensures i == 0 ==> a new View object is created
     * @ensures i != 0 ==> System.exit(0) is called
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        int i = JOptionPane.showConfirmDialog(null, "Whether to start the game");
        System.out.println(i);
        if(i==0){
            new View();
        }else{
            System.exit(0);
        }
    }
}