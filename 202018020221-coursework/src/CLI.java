import model.Model;
import model.impl.ModelImpl;
import pojo.CharacterState;
import java.util.List;
import java.util.Scanner;

/**
 * A command-line interface for the Numberle game.
 */
public class CLI {
    public static Scanner scanner=new Scanner(System.in);
    public static Model model=new ModelImpl();

    /**
     * The main entry point for the game.
     */
    public static void main(String[] args) {
        gameBegin();
    }

    /**
     * Begins a new game or exits based on user choice.
     * @ensures choice == 1 ==> model.init() is called
     * @ensures choice == 2 ==> System.exit(0) is called
     */
    public static void gameBegin(){
        System.out.println("please choice");
        System.out.println("1.begin a new game");
        System.out.println("2.exit");
        int choice = getChoice(1, 2);
        if(choice==1){
            model.init();
            play();
        }else{
            System.exit(0);
        }
    }

    /**
     * The main game loop that allows the user to play the game.
     * @ensures flag == true ==> "Congratulations, you won" is printed
     * @ensures flag == false ==> "You lose. The correct equation is " + model.getNowEquation() is printed
     */
    public static void play(){
        System.out.println("You have six chances to enter the equation");
        System.out.println("The length of the equation is 7 words, consisting of numbers and +-*/=");
        boolean flag=false;
        for(int i=0;i<6&&!flag;i++){
            String userInputEquation = getUserInputEquation();
            List<CharacterState> inputState = model.getInputState(userInputEquation);
            int cnt=0;
            for(CharacterState characterState:inputState){
                if(characterState==CharacterState.CORRECT){
                    cnt++;
                }
            }
            if(cnt==userInputEquation.length()){
                flag=true;
            }
            model.addInputLine(userInputEquation);
        }
        printBefore();
        if(flag){
            System.out.println("Congratulations, you won");
            System.out.println("The equation is " + model.getNowEquation());
        }else{
            System.out.println("You lose. The correct equation is " + model.getNowEquation());
        }
        model.reset();
        gameBegin();
    }

    /**
     * Gets the user input for the equation.
     * @return a valid equation entered by the user
     * @requires model.getNowEquation() != null
     * @ensures \result.length() == model.getNowEquation().length()
     * @ensures model.getCheck() ==> model.isCorrectEquation(\result)
     */
    public static String getUserInputEquation(){
        while (true){
            try{
                printBefore();
                System.out.println("Please enter an equation of length " + model.getNowEquation().length());
                if(model.getDisplay()){
                    System.out.println("answer is " + model.getNowEquation());
                }
                String s = scanner.nextLine();
                if(s.length()!=model.getNowEquation().length()) continue;
                if(model.getCheck()){
                    boolean correctEquation = model.isCorrectEquation(s);
                    if(!correctEquation){
                        System.out.println("incorrect equation");
                        continue;
                    }
                }
//                String temp="0123456789+-*/=";
                return s;
            }catch (Exception e){
                continue;
            }
        }
    }

    /**
     * Prints the previous input and states.
     */
    public static void printBefore(){
        List<String> allInput = model.getAllInput();
        List<List<CharacterState>> allState = model.getAllState();

        if(allInput.size()!=0){
            System.out.println("Previous input");
            for(int i=0;i<allInput.size();i++){
                for(int j=0;j<allInput.get(0).length();j++){
                    System.out.print("{"+allInput.get(i).charAt(j)+" "+allState.get(i).get(j)+" } ");
                }
                System.out.println();
            }
        }
    }

    /**
     * Gets the user's choice within a specified range.
     * @param begin the beginning of the range (inclusive)
     * @param end the end of the range (inclusive)
     * @return the user's choice as an integer
     * @requires scanner != null
     * @ensures \result >= begin && \result <= end
     */
    public static int getChoice(int begin,int end){
        System.out.println("please input choice number");

        while (true){
            try{
                String s=scanner.nextLine();
                int choice=Integer.parseInt(s);
                if(choice<begin||choice>end){
                    System.out.println("please input number between "+ begin+" and "+end);
                }else{
                    return choice;
                }
            }catch (Exception e){
                System.out.println("please input number");
            }
        }
    }
}