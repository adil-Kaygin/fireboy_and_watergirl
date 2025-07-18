import java.awt.Color;
import java.awt.Rectangle;

import java.util.Random;

import javax.swing.JLabel;

public class Monster extends JLabel implements Collidable ,Gravitational{

    private final int WIDTH;
    private final int HEIGHT;

    private final int score  = 10;

    private final int STEP = 1;

    private int health = 3;
    private int x;

    private double velocityY = 0;
    private double y;

    private boolean onBlock;
    private boolean direction = false; // true --> false <--
    private boolean alive = true;
    private boolean[] RBO = new boolean[3];

    private GamePanel myPanel;
    private Random rand = new Random();

    public Monster(int x, int y, int WIDTH, int HEIGHT,GamePanel panel) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.x = x;
        this.y = y;

        myPanel = panel;

        setBounds(x, y, WIDTH, HEIGHT);
        setOpaque(true);
        randomColor();
    }
    
    public void randomColor(){
        if (rand.nextDouble() < 0.1){RBO[2] = true;setBackground(Color.ORANGE);}

        else{
            if(rand.nextBoolean()){
                setBackground(Color.RED);
                RBO[0] = true;
            }
            else{
                setBackground(Color.BLUE);
                RBO[1] = true;
            }
        }
    }

    public void move(){

        if (x >= myPanel.WIDTH()-WIDTH){
            direction = false;
        }

        if (x <= myPanel.getX()){
            direction = true;
        }

        x = (direction) ? x+STEP : x-STEP;

        setLocation( x ,(int) y);
    }

    @Override
    public Rectangle collisionMask(){
        if(alive){
            return new Rectangle((int)x-MASKSIZE,(int)y-MASKSIZE,WIDTH+2*MASKSIZE,HEIGHT+2*MASKSIZE);
        }
        return new Rectangle(-10, -10, 0, 0);
        
    }

    public void update(){
        if(!alive){return;}
        applyGravity();
        move();
    }

    @Override
    public void applyGravity() {
        Rectangle mask = collisionMask();
        mask.y -= (velocityY-GRAVITY);
        onBlock = myPanel.checkOnBlock(mask);

        if(!onBlock){
            velocityY -= GRAVITY;
            velocityY = (velocityY < -4) ? -4 : velocityY;
            y-=velocityY;
        }

        setLocation((int)x, (int)y);
        repaint();
    }

    void setOnBlock(boolean onBlock) {

        this.onBlock = onBlock;

        velocityY = (onBlock) ? 0:velocityY;
    }
    
    public void getShot(boolean B) {
        if(RBO[2]){return;}

        if (B){
            if(RBO[1]){
                health++;
            }
            else{
                health--;
            }
        }

        else{
            if(RBO[0]){
                health++;
            }
            else{
                health--;
            }
        }
        if(health <=0){destroy();}
    }

    void destroy(){
        alive = false;
        if (true){ //(rand.nextDouble() < 0.2)
            PowerUp p = new PowerUp(x, (int)(y+HEIGHT*0.5));
            myPanel.addPowerUp(p);
        }

        myPanel.increaseSkor(score);
        myPanel.removeMonster(this);
        myPanel.repaint();
    }

    public void setDirection() {
        direction = !direction;
    }
    
}
