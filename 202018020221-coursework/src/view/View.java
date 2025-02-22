package view;

import controller.Controller;
import controller.impl.ControllerImpl;
import pojo.CharacterState;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * A view class for the Numberle game that extends JFrame.
 * @invariant grid != null && buttons != null && buttonLabels != null
 * @invariant controller != null
 * @invariant round >= 0 && col >= 0
 */
public class View extends JFrame {
    private JTextField[][] grid;
    private JButton[] buttons;
    private final String[] buttonLabels = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0","BackSpace", "+", "-", "*", "/", "=","Enter",};

    private Controller controller=new ControllerImpl();
    private boolean gameBegin=false;
    private int round=0;
    private int col=0;
    private JButton begin;
    private JLabel equation_target=new JLabel();

    /**
     * Constructs the view and initializes the game interface.
     * @ensures gameBegin == false && round == 0 && col == 0
     * @ensures begin != null && equation_target != null
     */
    public View() {
        setTitle("Numberle");
        setMinimumSize(new Dimension(500,500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initGrid();
        initButtons();
        pack();
        setVisible(true);
    }

    /**
     * Initializes the grid for the game.
     * @ensures grid is a 6x7 matrix of non-editable, centered JTextField elements
     */
    private void initGrid() {
        JPanel gridPanel = new JPanel(new GridLayout(6, 7));
        grid = new JTextField[6][7];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new JTextField();
                grid[i][j].setEditable(false);
                grid[i][j].setHorizontalAlignment(JTextField.CENTER);
                grid[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
                gridPanel.add(grid[i][j]);
            }
        }
        add(gridPanel, BorderLayout.CENTER);

        begin=new JButton("Start Game");
        begin.addActionListener(new ActionListener() {
            /**
             * Handles the action performed when the 'begin' button is clicked.
             * @param e The event generated by clicking the button.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameBegin) {
                    begin.setText("Begin a new game");
                    gameBegin = true;
                } else {
                    reset();
                }
                gameBegin=true;
                // If the controller is set to display the equation, show it in the target label.
                if(controller.getDisplayEquation()){
                    equation_target.setText("answer   " + controller.getNowEquation());
                    equation_target.setForeground(Color.red);
                }else{
                    equation_target.setText("");
                }
                begin.setEnabled(false);
            }
        });

        JPanel jPanel=new JPanel();
        jPanel.add(begin);
        jPanel.add(equation_target);

        add(jPanel,BorderLayout.NORTH);
    }

    /**
     * Initializes the buttons for the game.
     * @ensures buttons.length == buttonLabels.length
     * @ensures (\forall int i; 0 <= i && i < buttons.length; buttons[i].getActionListeners().length > 0)
     */
    private void initButtons() {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 9)); // Create button panel

        buttons = new JButton[buttonLabels.length];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);
            buttonPanel.add(buttons[i]);
            buttons[i].addActionListener(new ButtonListener()); // Add listener
        }
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Resets the color of all buttons to white.
     * @ensures (\forall JButton button; Arrays.asList(buttons).contains(button); button.getBackground() == Color.white)
     */
    private void resetButtonColor(){
        Arrays.stream(buttons).forEach(e-> e.setBackground(Color.white));
    }

    /**
     * Resets the game to its initial state.
     * @ensures (\forall int i; 0 <= i && i < grid.length; (\forall int j; 0 <= j && j < grid[i].length; grid[i][j].getText().isEmpty() && grid[i][j].getBackground() == Color.white))
     * @ensures round == 0 && col == 0
     * @ensures equation_target.getText().equals(controller.getNowEquation())
     */
    private void reset(){
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {

                grid[i][j].setBackground(Color.white);
                grid[i][j].setText("");
            }
        }
        round=0;
        col=0;
        controller.reset();
        equation_target.setText(controller.getNowEquation());
        resetButtonColor();
    }

    /**
     * A listener for button actions within the game.
     * @invariant builder != null
     */
    private class ButtonListener implements ActionListener {
        /**
         * Responds to button clicks in the game interface.
         * @param e The action event triggered by clicking a button.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!gameBegin){ // If the game has not started, prompt the user to begin the game.
                JOptionPane.showMessageDialog(null,"Please click start game");
                return;
            }
            String command = e.getActionCommand(); // Get the command associated with the button click.
            if(command.equals("BackSpace")){ // Handle the BackSpace button functionality.
                if(col>0){ // If there is text to delete, remove the last character entered.
                    if(col!=6){ // Special handling for the last column.
                        if(grid[round][col].getText().isEmpty()){ // If the current cell is empty, delete from the previous cell.
                            grid[round][col-1].setText("");
                        }
                        grid[round][col].setText("");
                    }else{
                        grid[round][col].setText("");
                    }
                    col--;
                }else{
                    grid[round][col].setText("");
                }
            }else if(command.equals("Enter")){ // Handle the Enter button functionality.
                StringBuilder builder=new StringBuilder();
                // Build the equation string from the grid.
                for(int i=0;i<grid[0].length;i++){
                    if(grid[round][i].getText().length()==0){
                        continue;
                    }
                    builder.append(grid[round][i].getText());
                }
                if(builder.length()==7){ // Check if the equation is complete.
                    // Validate the equation.
                    boolean correctEquation = controller.isCorrectEquation(builder.toString());
                    if(!correctEquation&& controller.getCheckValid()){ // If the equation is incorrect and validation is enabled, prompt the user.
                        JOptionPane.showMessageDialog(null,"please input correct equation");
                        return;
                    }

                    // Get the state of each character in the equation.
                    List<CharacterState> states=controller.getInputState(builder.toString());
                    for(int i=0;i<grid[0].length;i++){ // Update the grid colors based on the character states.
                        switch (states.get(i)){
                            case CORRECT -> grid[round][i].setBackground(Color.GREEN);
                            case INCORRECT_LOCATION -> grid[round][i].setBackground(Color.orange);
                            case NOT_EXIST -> grid[round][i].setBackground(Color.gray);
                        }
                    }
                    // Update the keyboard colors based on the character states.
                    setKeyBoardColor();
                    // Check if the user has won the game.
                    boolean flag=controller.isWin(states);
                    if(flag){
                        //win
                        int k = JOptionPane.showConfirmDialog(null, "Congratulations on your victory,Do you want to play again?");
                        if(k==0){
                            reset();
                        }else{
                            System.exit(0);
                        }
                    }else{
                        if(round==5){
                            int k = JOptionPane.showConfirmDialog(null, "You lose! correct equation is " + controller.getNowEquation()
                                    + " Do you want to play again?");
                            if(k==0){
                                reset();
                            }else{
                                System.exit(0);
                            }
                        }else{
                            round++;
                            col=0;
                            if(round>0){
                                begin.setEnabled(true);
                            }
                        }
                    }
                }else {
                    JOptionPane.showMessageDialog(null,"Please enter an equation of length " + grid[0].length);
                }
            }else{
                if(col<grid[0].length){
                    grid[round][col].setText(command);

                    if(col<6) col++;
                }
            }
        }

        /**
         * Sets the color of the keyboard buttons based on their state.
         * @ensures (\forall JButton button; Arrays.asList(buttons).contains(button);
         *           (builder.toString().contains(button.getText()) && controller.getNowEquation().contains(button.getText())) ==>
         *           (button.getBackground() == Color.GREEN || button.getBackground() == Color.orange))
         */
        private void setKeyBoardColor(){
            StringBuilder builder=new StringBuilder();
            for(int i=0;i<grid[0].length;i++){
                if(grid[round][i].getText().length()==0){
                    continue;
                }
                builder.append(grid[round][i].getText());
            }
            while (builder.length()!=7){
                builder.append(" ");
            }
            List<CharacterState> inputState = controller.getInputState(builder.toString());
            Arrays.stream(buttons).forEach(e->{
                if(builder.toString().contains(e.getText())&&controller.getNowEquation().contains(e.getText())){
                    boolean flag=false;
                    for(int i=0;i<7;i++){
                        if(inputState.get(i)==CharacterState.CORRECT&&builder.toString().charAt(i)==e.getText().charAt(0)){
                            flag=true;
                            break;
                        }
                    }
                    if(flag){
                        e.setBackground(Color.GREEN);
                    }else{
                        e.setBackground(Color.orange);
                    }
                }else if(builder.toString().contains(e.getText())&&!controller.getNowEquation().contains(e.getText())){
                    e.setBackground(Color.gray);
                }
            });
        }
    }
}