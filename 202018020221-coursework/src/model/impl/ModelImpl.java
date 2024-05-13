package model.impl;

import model.Model;
import pojo.CharacterState;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @invariant strings != null && allState != null && allInput != null
 * @invariant checkValid == true && displayEquation == true && randomSelect == true
 */
public class ModelImpl extends Observable implements Model {
    private boolean checkValid =true;//one
    private boolean displayEquation =true;//two
    private boolean randomSelect =true;

    private List<String> strings=new ArrayList<>();
    private List<List<CharacterState>> allState=new ArrayList<>();
    private List<String> allInput=new ArrayList<>();

    private String nowEquation=null;

    /**
     * Initializes the model by reading equations from a file.
     * @throws IOException if there is an error reading the file
     * @ensures \old(strings).size() == 0 ==> strings.size() > 0
     */
    @Override
    public void init() {
        assert new File("equations.txt").exists():"The equations.txt required for initialization does not exist";
        //read file
        try{
            BufferedReader bufferedReader=new BufferedReader(new FileReader("equations.txt"));
            String line=null;
            while ((line=bufferedReader.readLine())!=null){
                strings.add(line);
            }
            bufferedReader.close();
            setRandomEquation(randomSelect);
        }catch (IOException  e){
            e.printStackTrace();
        }
    }

    /**
     * Resets the model to its initial state.
     * @ensures strings.isEmpty() && allState.isEmpty() && allInput.isEmpty() && nowEquation == null
     */
    @Override
    public void reset() {
        strings.clear();
        allState.clear();
        allInput.clear();
        nowEquation=null;
    }

    /**
     * Sets a random equation from the list of strings.
     * @param flag indicates whether a random equation should be selected
     * @ensures flag == false ==> nowEquation.equals(strings.get(0))
     * @ensures flag == true ==> strings.contains(nowEquation)
     */
    private void setRandomEquation(boolean flag) {
        assert strings.size()>0 : "model initialization failed.";
        Random random=new Random(System.currentTimeMillis());
        if(!flag){
            nowEquation=strings.get(0);
        }else{
            nowEquation=strings.get(random.nextInt(strings.size()));
        }
    }

    /**
     * Retrieves the state of each character in the input compared to the current equation.
     * @param input the string to compare with the current equation
     * @return a list of CharacterState indicating the state of each character
     * @requires nowEquation != null && input.length() == nowEquation.length()
     * @ensures \result != null
     */
    @Override
    public List<CharacterState> getInputState(String input) {
        assert nowEquation!=null: "model initialization failed.";
        assert input.length()==nowEquation.length():"The string for comparison must be the same length";

        List<CharacterState> characterStates=new ArrayList<>();
        for(int i=0;i<nowEquation.length();i++){
            char in=input.charAt(i);
            char equ=nowEquation.charAt(i);
            if(in==equ){
                characterStates.add(CharacterState.CORRECT);
            }else if(nowEquation.indexOf(in)!=-1){
                characterStates.add(CharacterState.INCORRECT_LOCATION);
            }else{
                characterStates.add(CharacterState.NOT_EXIST);
            }
        }
        return characterStates;
    }

    /**
     * Adds a new input line and updates the state of the game.
     * @param input the input line to add
     * @requires allInput.size() < 6
     * @ensures allInput.contains(input) && allState.contains(getInputState(input))
     */
    @Override
    public void addInputLine(String input) {
        assert allInput.size()<6:"Maximum 6 chances per game";
        allInput.add(input);
        allState.add((ArrayList<CharacterState>) getInputState(input));
    }

    /**
     * Gets all the input lines that have been entered.
     * @return a list of all input lines
     * @ensures \result == allInput
     */
    @Override
    public List<String> getAllInput() {
        return allInput;
    }

    /**
     * Gets the state of all input lines.
     * @return a list of lists, each containing the state of an input line
     * @ensures \result == allState
     */
    @Override
    public List<List<CharacterState>> getAllState() {
        return allState;
    }

    /**
     * Gets the current equation.
     * @return the current equation as a string
     * @ensures \result == nowEquation
     */
    @Override
    public String getNowEquation() {
        return nowEquation;
    }

    /**
     * Checks if the given input is a correct equation.
     * @param input the input to check
     * @return true if the input is a correct equation, false otherwise
     * @ensures \result == (evaluate(split[0]) == evaluate(split[1]))
     */
    @Override
    public boolean isCorrectEquation(String input) {
        String[] split = input.split("=");
        String[] strings1=new String[]{"+","-","*","/"};
        boolean flag=false;
        for(int i=0;i<strings1.length;i++){
            if(input.contains(strings1[i])){
                flag=true;
            }
        }
        if(!flag){
            return false;
        }
        if(split.length!=2){
            return false;
        }
        try{
            double leftResult = evaluate(split[0]);
            double rightResult = evaluate(split[1]);
            return leftResult==rightResult;
        }catch (Exception e){
            return false;
        }

    }

    /**
     * Gets the value of the checkValid flag.
     * @return the value of checkValid
     * @ensures \result == checkValid
     */
    @Override
    public boolean getCheck() {
        return checkValid;
    }

    /**
     * Gets the value of the displayEquation flag.
     * @return the value of displayEquation
     * @ensures \result == displayEquation
     */
    @Override
    public boolean getDisplay() {
        return displayEquation;
    }

    /**
     * Evaluates a mathematical expression.
     * @param expression the expression to evaluate
     * @return the result of the evaluation
     * @ensures \result == parseExpression(expression)
     */
    public double evaluate(String expression) {
        expression = expression.replaceAll("\\s+", ""); // Remove all spaces
        return parseExpression(expression);
    }

    /**
     * Parses and evaluates a mathematical expression.
     * @param expr the expression to parse
     * @return the result of the parsing
     * @ensures \result == (parsed and evaluated result of expr)
     */
    private double parseExpression(String expr) {
        if (expr.contains("+")) {
            String[] parts = expr.split("\\+", 2);
            return parseExpression(parts[0]) + parseExpression(parts[1]);
        } else if (expr.contains("-")) {
            int minus = expr.indexOf('-');
            if (minus == 0) { // Negative number at start
                return -parseExpression(expr.substring(1));
            } else {
                return parseExpression(expr.substring(0, minus)) - parseExpression(expr.substring(minus + 1));
            }
        } else if (expr.contains("*")) {
            String[] parts = expr.split("\\*", 2);
            return parseExpression(parts[0]) * parseExpression(parts[1]);
        } else if (expr.contains("/")) {
            String[] parts = expr.split("/", 2);
            return parseExpression(parts[0]) / parseExpression(parts[1]);
        } else {
            return Double.parseDouble(expr); // Base case: simple number
        }
    }
}