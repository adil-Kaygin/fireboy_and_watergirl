import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

public class Player extends JLabel implements Collidable ,Gravitational {

    private static final double JUMP = 5.4;
    private static final double JCOEF = 1.4;

    private static final int PowerUpSetter = 1000; //  1 sn
    private static final int CoolSetter = 500; //  500ms
    private static final int powerSecond = 9;    // 9 sn

    private int cyanPower = 0;
    private int magentaPower = 0;
    private int redPower = 0;
    private int bluePower = 0;


    private final int WIDTH;
    private final int HEIGHT;
    private final int STEP;

    private double velocityY = 0;
    private int x;
    private double y;

    private int coolDown = 0;

    private boolean jumping = false;
    private boolean onBlock = false;
    private boolean underBlock = false;
    private boolean bulletDirection = false;
    private boolean[] RBM = {false,true,false};

    private GamePanel myPanel;
    private ArrayList<Bullet> bullets = new ArrayList<>(10);
    private JLabel powerLabel;
    private boolean isRunning = true;


    public Player(int x,int y,int WIDTH,int HEIGHT,GamePanel panel){
        this.x = x;
        this.y = y;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        STEP = WIDTH/2;

        myPanel = panel;

        setBackground(Color.BLUE);
        setBounds(x, (int)y, WIDTH, HEIGHT);
        setOpaque(true);

        PowerUp();

        initTimer();
    }

    public void initTimer() {
        new Timer().scheduleAtFixedRate(new TimerTask() {public void run(){if(!isRunning){return;};coolDown = (coolDown > 0) ? coolDown-1:0;}}, 0, CoolSetter);

        new Timer().schedule(new TimerTask() {
            public void run(){
                if(!isRunning){return;}
                redPower = (redPower > 0) ? redPower-1:0;
                bluePower = (bluePower > 0) ? bluePower-1:0;
                magentaPower = (magentaPower > 0) ? magentaPower-1:0;
                cyanPower = (cyanPower > 0) ? cyanPower-1:0;
            }
        }, 0,PowerUpSetter);
    }

    public void up(){
        jump();
    }
    
    public void jump() {

        if(underBlock){return;}

        if(onBlock){
            jumping = true;
        }
        if(jumping){
            velocityY=(RBM[2]) ? JUMP*JCOEF:JUMP*(1+((magentaPower == 0) ? 0:1)*(JCOEF-1));
            jumping = false;
        }
    }

    public void left(){

        Rectangle mask = collisionMask();
        mask.x-=STEP;

        if(myPanel.getX() <= x-STEP && !myPanel.checkWall(mask)){
            x-=STEP;
        }

        bulletDirection = true;
    }
    
    public void right(){
        Rectangle mask = collisionMask();
        mask.x+=STEP;

        if(myPanel.getX()+myPanel.WIDTH() >= x+STEP+WIDTH && !myPanel.checkWall(mask)){
            x+=STEP;
        }

        bulletDirection = false;
    }
    
    public void red(){
        setBackground(Color.RED);
        RBM[0] = true;
        RBM[1] = false;
        RBM[2] = false;
    }
    
    public void blue(){
        setBackground(Color.BLUE);
        RBM[0] = false;
        RBM[1] = true;
        RBM[2] = false;
    }
    
    public void magenta(){
        setBackground(Color.MAGENTA);
        RBM[0] = false;
        RBM[1] = false;
        RBM[2] = true;
    }
    
    public void bullet(){
        if(RBM[2]){return;}
        if(coolDown == 0){
            Bullet b = new Bullet(x, (int)y, bulletDirection, RBM[1]);
            bullets.add(b);
            myPanel.add(b); 
            coolDown = 1;
        }
    }

    public void update(){
        applyGravity();
        updateBullet();
        updatePower();
    }
    
    private void PowerUp() {

        powerLabel = new JLabel();
        powerLabel.setOpaque(isOpaque());
        powerLabel.setBackground(Color.WHITE);
        powerLabel.setText("red: "+redPower +"  blue: "+bluePower+"  magenta: "+magentaPower+"  cyan: "+cyanPower);

        FontMetrics fm = powerLabel.getFontMetrics(powerLabel.getFont());
        int w =  fm.stringWidth(powerLabel.getText());

        powerLabel.setBounds(10*WIDTH, 0, (int)(1.15*w), powerLabel.getPreferredSize().height);
        myPanel.add(powerLabel);

    }

    private void updatePower(){

        powerLabel.setText("red: "+redPower +"  blue: "+bluePower+"  magenta: "+magentaPower+"  cyan: "+cyanPower);

        powerLabel.repaint();
    }
    
    public void applyGravity() {

        Rectangle mask = collisionMask();
        mask.y -= (velocityY-GRAVITY);
        onBlock = myPanel.checkOnBlock(mask);

        if(!onBlock){
            velocityY -= GRAVITY;
            velocityY = (velocityY < -5) ? -5 : velocityY; //limit velocity
            y-=velocityY;
        }

        setLocation(x, (int)y);
        repaint();
    }
    
    public void updateBullet(){

        Bullet[] local = bullets.toArray(new Bullet[0]);

        for(Bullet b : local){
            b.move();
        }
    }
    
    public void checkAllBullets(Monster m){

        Bullet[] local = bullets.toArray(new Bullet[0]);

        for(Bullet b: local){
            if(Collidable.checkColision(b, m)){
                m.getShot(b.B);
                b.destroy();
            }
        }
    }

    @Override
    public Rectangle collisionMask(){
        return new Rectangle(x-MASKSIZE,(int)y-MASKSIZE,WIDTH+2*MASKSIZE,HEIGHT+MASKSIZE);
    }

    public void checkTrap(boolean r) {

        if(r && RBM[0] || (r && redPower > 0)){return;}

        if(!r && RBM[1] || (!r && bluePower > 0)){return;}

        myPanel.restart();
    }

    public void destroy() {
        Bullet[] local = bullets.toArray(new Bullet[0]);

        for(Bullet b: local){
                b.destroy();
            }
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run(){
                        myPanel.remove(powerLabel);
                        myPanel.remove(Player.this);
                    }
                });
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    public void pause() {
        isRunning = !isRunning;
    }

    public void setOnBlock(boolean onBlock) {
        this.onBlock = onBlock;
    }
    
    public void setUnderBlock(boolean underBlock){

        this.underBlock = underBlock;
        
        if(underBlock){
            velocityY = (velocityY > 0) ? 0:velocityY;
        }
    }

    public void setPowerUp(int i) {

        switch (i){
            case 1:
            cyanPower += powerSecond;
            break;

            case 2:
            redPower += powerSecond;
            break;
            
            case 3:
            bluePower += powerSecond;
            break;
            
            case 4:
            magentaPower += powerSecond;
            break;
        }
    }
    
    public boolean isCyan() {
        return (cyanPower > 0);
    }

    private class Bullet extends JLabel implements Collidable{

        private final int bx = 7;
        private final int by = 3;
        private int x;
        private int y;

        private boolean direction = false; // false ->  true <- 
        private boolean B; // false red / true blue

        private Bullet(int x,int y,boolean direction,boolean B){

            this.direction = direction;
            this.B = B;
            this.x = x;
            this.y = y;

            setBackground((B) ? Color.BLUE:Color.RED);
            setBounds(x, y, bx, by);
            setOpaque(true);
        }
        
        private void move(){

            x = (direction) ? x-3 :x+3 ;

            if (x >= myPanel.WIDTH() || myPanel.checkWall(collisionMask())){
                destroy();
            }
            if (x <= myPanel.getX() || myPanel.checkWall(collisionMask())){
                destroy();
            }
            setLocation(x, y);
        }
        
        private void destroy(){

            bullets.remove(this);

            try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run(){
                    myPanel.remove(Bullet.this);
                    }
                    });
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            myPanel.repaint();
        }
        
        @Override
        public Rectangle collisionMask() {
            return new Rectangle(x, y, bx, by);
        }

    }

    }

