import acm.graphics.GLabel;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

import stanford.spl.GButton;
import stanford.spl.GTextField;

import java.util.ArrayList;
import java.util.Random;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.Timer;


public class MainClass extends GraphicsProgram implements ActionListener
{

    private ArrayList<SnakePart> snakeBody;
    private int snakeX, snakeY, snakeWidth, snakeHeight;
    public Timer timer = new Timer(200, this);
    private boolean isPlaying, isGameOver;
    private int score, previousScore;
    private Scoreboard scoreLabel;
    public GOval food;
    private GLabel instructions;
    private GLabel startLabel, colorLabel, instructionsLabel;
    private Color snakeColor;
    private Boolean showColorSelection;
    private Boolean showInstructions;
    private GTextField redLabel, greenLabel, blueLabel;
    private GButton submitButton;
    private int snake_direction; // 1 : up; 2: down; 3: left; 4: right
    private Boolean showScore;

    public void run()
    {
        setUpStartScreen();
        addKeyListeners();
        randomFood();
        drawSnake();
        showScore = false;
//        food = new Ball (50,50,100,100);
    }
    // food changes location randomly
    public void randomFood(){
        int food_width = 15;
        int food_height = 15 ;
        Random random = new Random();
        int randx = random.nextInt(0, (int) getCanvasSize().getWidth()-food_width);
        int randy = random.nextInt(0, (int) getCanvasSize().getHeight()-food_height);
        //food.setLocation(randx, randy, 15, 15);
        food = new GOval(randx, randy, food_width, food_height);

        food.setFilled(true);
        food.setColor(Color.GREEN);
        add(food);  //add to canvas
    }

    // add instructions and scoreboard to screen
    // MT: expand the setUpStartScreen method to:
    // 1. Notify the user "press any key to start"
    // 2. Show the "Use arrow keys ...." instruction
    // 3. After a certain period of time, remove instructions, show
    private void setUpStartScreen(){
        ;
        startLabel = new GLabel("Click the Mouse to Start", getWidth()/2, getHeight()/2);
        startLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(startLabel);

        waitForClick();
        remove(startLabel);

        ShowInstructions();
        isPlaying = true;
        isGameOver = false;
        timer.start();
        snake_direction = 2 ; // initial direction moving right
        addKeyListeners();
    }
    private void ShowInstructions(){
        instructions = new Instructions("Use the arrow keys to move the snake.", getWidth()/2, getHeight()/2);
        instructions.setFont(new Font("Arial", Font.BOLD, 20));
        add(instructions);
        showInstructions = true;
    }

    private void ShowScore() {
        if (showScore) {
            //remove old score
            remove(scoreLabel);
        }
        scoreLabel = new Scoreboard("Current Score: " + score, getWidth()-100, 20);
        scoreLabel.setFont(new Font( "Arial", Font.ITALIC, 15));
        add(scoreLabel);
        showScore = true;
    }

    public void drawSnake()
    {
         int init_snake_length = 10 ;

         snakeWidth = 15;
         snakeY = 0 ;
         snakeX = 0 ;
         SnakePart snakepart;

         snakeBody = new ArrayList<SnakePart>();
         for (int i = 0 ; i < init_snake_length; i++) {
             snakepart = new SnakePart(snakeX + snakeWidth*i, snakeY, snakeWidth, snakeWidth);
             snakepart.setFilled(true);
             snakepart.setColor(Color.RED);
             snakeBody.add(snakepart);
             add(snakepart);
         }
    }

    public void keyPressed(KeyEvent keyPressed)
    {
        if (showInstructions) {
            remove(instructions);
            showInstructions = false;
        }

        if (!showScore) {
            ShowScore();
            showScore = true;
        }
        switch (keyPressed.getKeyCode())
        {
            case KeyEvent.VK_UP:
                snake_direction = 1 ; break;
            case KeyEvent.VK_DOWN:
                snake_direction = 2 ; break;
            case KeyEvent.VK_LEFT:
                snake_direction = 3; break;
            case KeyEvent.VK_RIGHT:
                snake_direction = 4 ; break;
        }
    }

    private void redrawSnake()
    {
        // redraw the snake
        for (int i = snakeBody.size()-1; i > 0; i--) {
            remove(snakeBody.get(i)); // remove the current snake part from the canvas
            snakeBody.get(i).setLocation(snakeBody.get(i - 1).getX(), snakeBody.get(i - 1).getY());
            add(snakeBody.get(i)); // add the updated snake part to canvas
        }
    }

    private void growSnake()
    {
        SnakePart snakepart;
        int snake_tail_1  = snakeBody.size()-1;
        int snake_tail_2  = snake_tail_1 - 1 ;
        double new_tail_x = 2*snakeBody.get(snake_tail_1).getX() - snakeBody.get(snake_tail_2).getX();
        double new_tail_y = 2*snakeBody.get(snake_tail_1).getY() - snakeBody.get(snake_tail_2).getY();
        snakepart = new SnakePart(new_tail_x, new_tail_y, snakeWidth, snakeWidth);
        snakepart.setFilled(true);
        snakepart.setColor(Color.RED);
        snakeBody.add(snakepart);
        add(snakepart);

    }

    private void moveUp()
    {
        // update snake
        redrawSnake();
        //if the snake head doesn't hit the wall, the move the snake head position up
        if ( (snakeBody.get(0).getX() + snakeWidth >= 0 && snakeBody.get(0).getX() + snakeWidth <= getWidth() ) &&
                (snakeBody.get(0).getY() + snakeWidth >=0 && snakeBody.get(0).getY()+snakeWidth <= getHeight()  ) ) {
            snakeBody.get(0).setLocation(snakeBody.get(0).getX(), snakeBody.get(0).getY() - snakeWidth);
            add(snakeBody.get(0));
        }
        else {
            isGameOver = true;
        }
    }

    private void moveDown()
    {
        redrawSnake();
        //if the snake head doesn't hit the wall, the move the snake head position up
        if ( (snakeBody.get(0).getX() + snakeWidth >= 0 && snakeBody.get(0).getX() + snakeWidth <= getWidth() ) &&
                (snakeBody.get(0).getY() + snakeWidth >=0 && snakeBody.get(0).getY()+snakeWidth <= getHeight()  ) ) {
            snakeBody.get(0).setLocation(snakeBody.get(0).getX(), snakeBody.get(0).getY()  + snakeWidth);
            add(snakeBody.get(0));
        }
        else {
            isGameOver = true;
        }
    }

    private void moveLeft()
    {
        redrawSnake();
        //if the snake head doesn't hit the wall, the move the snake head position up
        if ( (snakeBody.get(0).getX() + snakeWidth >= 0 && snakeBody.get(0).getX() + snakeWidth <= getWidth() ) &&
                (snakeBody.get(0).getY() + snakeWidth >=0 && snakeBody.get(0).getY()+snakeWidth <= getHeight()  ) ) {
            snakeBody.get(0).setLocation(snakeBody.get(0).getX() - snakeWidth, snakeBody.get(0).getY() );
            add(snakeBody.get(0));
        }
        else {
            isGameOver = true;
        }

    }

    private void moveRight()
    {
        redrawSnake();
        //if the snake head doesn't hit the wall, the move the snake head position up
        if ( (snakeBody.get(0).getX() + snakeWidth >= 0 && snakeBody.get(0).getX() + snakeWidth <= getWidth() ) &&
                (snakeBody.get(0).getY() + snakeWidth >=0 && snakeBody.get(0).getY()+snakeWidth <= getHeight()  ) ) {
            snakeBody.get(0).setLocation(snakeBody.get(0).getX() + snakeWidth, snakeBody.get(0).getY() );
            add(snakeBody.get(0));
        }
        else {
            isGameOver = true;
        }

    }

    private void move_snake() {
        switch (snake_direction) {
            case 1: moveUp(); break;
            case 2: moveDown(); break;
            case 3: moveLeft(); break;
            case 4: moveRight(); break;
        }
    }

    private boolean eat_fruit() {
        // check if the snake eats the fruit
        if (food.getBounds().intersects(snakeBody.get(0).getBounds())) {
            return true;
        }
        else {
            return false;
        }
    }


    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        if (!isGameOver) {
            // if not game over:
            // show score
            // move snake (depnding on direction, 4 directions)
            // check if the food is eaten by snake; if eaten, increase score; grow snake; and generate a new food
            // check if the snake hit the walls or hit itself if so then game over
            move_snake();
            if (eat_fruit()) {
                score += 10;
                remove(food);
                randomFood();
                growSnake();
                ShowScore();
            }

        }
        else { // if game over
        }
    }

    public static void main(String[] args)
    {
        new MainClass().start();
    }
}