import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;

public class GamePanel extends JPanel{

    private static final long DELAY = 20;

    private final int UNIT = 16;
    private final int ROW = 13 * 3;
    private final int COL = 23 * 3;
    private final int WIDTH = COL * UNIT;
    private final int HEIGHT = ROW * UNIT;

    private int score = 0;

    private boolean isRunning = true;

    private ArrayList<Block> blocks = new ArrayList<>(30);
    private ArrayList<Monster> monsters = new ArrayList<>(20);
    private ArrayList<PowerUp> powerUps = new ArrayList<>(20);
    private ArrayList<Dot> dots = new ArrayList<>(100);

    private Player player;
    private Block block;
    private Door door;
    private JLabel scorLabel;
    private Spawner spawner;

    public GamePanel() {

        setLayout(null);
        setDoubleBuffered(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        addKeyListener(new MyKeyAdapter());

        generateMap();

        initMonsters();

        player = new Player((int)(UNIT*0.5), (int)(HEIGHT-2.5*UNIT), UNIT, UNIT,this);
        add(player);

        spawner = new Spawner(3*UNIT, 0, 2*UNIT, 2*UNIT,this);

        initScore();
        
        new Thread(new GameLoop()).start();
    }

    private void initScore() {

        scorLabel = new JLabel();
        scorLabel.setOpaque(isOpaque());
        scorLabel.setBackground(Color.WHITE);
        scorLabel.setText("SCORE: "+score );

        FontMetrics fm = scorLabel.getFontMetrics(scorLabel.getFont());
        int w =  fm.stringWidth(scorLabel.getText());

        scorLabel.setBounds(0, 0, (int)(1.5*w), scorLabel.getPreferredSize().height);
        add(scorLabel);

    }
 
    private void generateMap() {

        door = new Door(0,2*UNIT,3*UNIT, 4*UNIT);
        add(door);


        block = new Block(0,(int) (HEIGHT*0.15),(int) (WIDTH*0.5), UNIT,this);
        add(block);
        blocks.add(block);

        block = new Block((int) (WIDTH*0.6),(int) (HEIGHT*0.35),UNIT,(int) (HEIGHT*0.1)+UNIT ,this);
        add(block);
        blocks.add(block);

        block = new Block((int) (WIDTH*0.2),(int) (HEIGHT*0.35),UNIT,(int) (HEIGHT*0.1)+UNIT ,this);
        add(block);
        blocks.add(block);

        block = new Block((int) (WIDTH*0.7),(int) (HEIGHT*0.15),(int) (WIDTH*0.3), UNIT,this);
        add(block);
        blocks.add(block);

        block = new Trap((int) (WIDTH*0.5),(int) (HEIGHT*0.25),(int) (WIDTH*0.15), UNIT,true,this);
        add(block);
        blocks.add(block);

        block = new Block((int) (WIDTH*0.15), (int) (HEIGHT*0.45), (int) (WIDTH*0.45), UNIT,this);
        add(block);
        blocks.add(block);

        block = new Block(0, (int) (HEIGHT*0.45), (int) (WIDTH*0.1), UNIT,this);
        add(block);
        blocks.add(block);

        block = new Trap((int) (WIDTH*0.7), (int) (HEIGHT*0.4), (int) (WIDTH*0.1), UNIT,false,this);
        add(block);
        blocks.add(block);

        block = new Trap((int) (WIDTH*0.85), (int) (HEIGHT*0.5), (int) (WIDTH*0.1), UNIT,true,this);
        add(block);
        blocks.add(block);

        block = new Block((int) (WIDTH*0.1),(int) (HEIGHT*0.6),(int) (WIDTH*0.2), UNIT,this);
        add(block);
        blocks.add(block);

        block = new Trap((int) (WIDTH*0.3),(int) (HEIGHT*0.7),(int) (WIDTH*0.1), UNIT,false,this);
        add(block);
        blocks.add(block);

        block = new Block((int) (WIDTH*0.43),(int) (HEIGHT*0.7),(int) (WIDTH*0.05), UNIT,this);
        add(block);
        blocks.add(block);

        block = new Trap((int) (WIDTH*0.55), (int) (HEIGHT*0.7), (int) (WIDTH*0.1), UNIT,true,this);
        add(block);
        blocks.add(block);

        block = new Block((int) (WIDTH*0.75), (int) (HEIGHT*0.7), (int) (WIDTH*0.25), UNIT,this);
        add(block);
        blocks.add(block);

        block = new Block(0, (int) (HEIGHT*0.8), (int) (WIDTH*0.15), UNIT,this);
        add(block);
        blocks.add(block);

        block = new Block((int) (WIDTH*0.65), (int) (HEIGHT*0.85), (int) (WIDTH*0.3), UNIT,this);
        add(block);
        blocks.add(block);

        block = new Block((int) (WIDTH*0.65), (int) (HEIGHT*0.7), UNIT, (int) (HEIGHT*0.15)+UNIT,this);
        add(block);
        blocks.add(block);

        block = new Block(0, HEIGHT-UNIT, (int) (WIDTH*0.35), UNIT,this);
        add(block);
        blocks.add(block);

        block = new Block((int) (WIDTH*0.7), HEIGHT-UNIT, (int) (WIDTH*0.3), UNIT,this);
        add(block);
        blocks.add(block);

        block = new Trap((int) (WIDTH*0.55), HEIGHT-UNIT, (int) (WIDTH*0.15), UNIT,true,this);
        add(block);
        blocks.add(block);

        block = new Block((int) (WIDTH*0.45), HEIGHT-UNIT, (int) (WIDTH*0.1), UNIT,this);
        add(block);
        blocks.add(block);

        block = new Trap((int) (WIDTH*0.35), HEIGHT-UNIT, (int) (WIDTH*0.1), UNIT,false,this);
        add(block);
        blocks.add(block);
    }

    private void initMonsters(){
        Monster m = new Monster((int) (WIDTH*0.3), (int) (HEIGHT*0.3), 2*UNIT, 2*UNIT, this);
        monsters.add(m);
        add(m);

        m = new Monster((int) (WIDTH*0.4), (int) (HEIGHT*0.3), 2*UNIT, 2*UNIT, this);
        monsters.add(m);
        add(m);

        m = new Monster((int) (WIDTH*0.5), HEIGHT - 4*UNIT, 2*UNIT, 2*UNIT, this);
        monsters.add(m);
        add(m);

        m = new Monster((int) (WIDTH*0.7), HEIGHT - 4*UNIT, 2*UNIT, 2*UNIT, this);
        monsters.add(m);
        add(m);

        m = new Monster((int) (WIDTH*0.8), HEIGHT - 4*UNIT, 2*UNIT, 2*UNIT, this);
        monsters.add(m);
        add(m);

        m = new Monster((int) (WIDTH*0.9), HEIGHT - 4*UNIT, 2*UNIT, 2*UNIT, this);
        monsters.add(m);
        add(m);

        m = new Monster((int) (WIDTH*0.6), HEIGHT - 4*UNIT, 2*UNIT, 2*UNIT, this);
        monsters.add(m);
        add(m);

        m = new Monster((int) (WIDTH*0.7), (int) (HEIGHT*0.85) - 3*UNIT, 2*UNIT, 2*UNIT, this);
        monsters.add(m);
        add(m);

        m = new Monster((int) (WIDTH*0.75), (int) (HEIGHT*0.85) - 3*UNIT, 2*UNIT, 2*UNIT, this);
        monsters.add(m);
        add(m);
    }

    public void initSave() {
        player.initTimer();
        spawner.initTimer();
        addKeyListener(new MyKeyAdapter());
        new Thread(new GameLoop()).start();
    }

    private class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();

            if(!isRunning){
                if(keyCode == KeyEvent.VK_P){
                    pause();
                }
                if(keyCode == KeyEvent.VK_K){
                    save();
                }
                return;
            }
            switch(keyCode){
                case KeyEvent.VK_UP:
                    player.up();
                    break;
                case KeyEvent.VK_LEFT:
                    player.left();
                    break;
                case KeyEvent.VK_RIGHT:
                    player.right();
                    break;
                case KeyEvent.VK_1:
                    player.red();
                    break;
                case KeyEvent.VK_2:
                    player.blue();
                    break;
                case KeyEvent.VK_3:
                    player.magenta();
                    break;
                case KeyEvent.VK_SPACE:
                    player.bullet();
                    break;
                case KeyEvent.VK_P:
                    pause();
                    break;
            }
        }
        
        private void save() {
            String name;
            while(true){
                name = JOptionPane.showInputDialog(null, "kayıt dosyası ismi:");
                if(name != null){break;}
                JOptionPane.showMessageDialog(null, "lütfen geçerli bir isim girin:", null, JOptionPane.INFORMATION_MESSAGE);
            }

            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(name+".bin"));
                out.writeObject(GamePanel.this);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(null, "Kayıt tamam!", null, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void pause() {
        isRunning  = !isRunning;
        player.pause();
        spawner.pause();
    }

    private void win() {
        JOptionPane.showMessageDialog(null, "Kazandın!\nScore: " + score, null, JOptionPane.INFORMATION_MESSAGE);
        pause();
    }

    private class GameLoop implements Runnable{

        
        @Override
        public void run() { // gameLoop;
            while (true) {
                
                if(isRunning){
                        update();
                    try {
                        Thread.sleep(DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        Thread.sleep(DELAY*10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        private void update(){
            scorLabel.setText("SCORE: "+score);

            checkAllCollisions();

            player.update();

            Monster[] local = monsters.toArray(new Monster[0]);
            for(Monster c : local){
                c.update();
            }
        }
        
        private void checkAllCollisions(){

            if(Collidable.checkColision(door, player)){win();}

            checkDots();

            checkPowerUps();

            checkWall();

            checkTrap();

            checkUnderBlock();
            
            checkAllBullets();

            checkDie();
            }

        private void checkTrap() {
            Block[] local1 = blocks.toArray(new Block[0]);
                for(Block c1: local1){
                    c1.checkOnBlock(player);
                }
            }

        private void checkPowerUps() {
            PowerUp[] local = powerUps.toArray(new PowerUp[0]);
            for(PowerUp p : local){
                    if(Collidable.checkColision(player, p)){
                        if(p.getType() == 0)
                            win();
                        player.setPowerUp(p.getType());
                        try {
                            SwingUtilities.invokeAndWait(new Runnable() {
                                public void run(){
                                    remove(p);
                                }
                            });
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        powerUps.remove(p);
                    }
            }
            repaint();
        }

        private void checkDots() {
            Dot[] local = dots.toArray(new Dot[0]);
            for(Dot d : local){
                    if(Collidable.checkColision(player, d)){
                        score+=5;
                        try {
                            SwingUtilities.invokeAndWait(new Runnable() {
                                public void run(){
                                    remove(d);
                                }
                            });
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dots.remove(d);
                    }
            }
            repaint();
        }

        private void checkAllBullets() {
            Monster[] local = monsters.toArray(new Monster[0]);
            for(Monster c : local){
                    if(c != null)
                        player.checkAllBullets(c);
            }
        }

        private void checkWall() {
            boolean tmpWall = false;
            Monster[] local = monsters.toArray(new Monster[0]);
            Block[] local1 = blocks.toArray(new Block[0]);
            for(Monster c : local){
                    for(Block c1: local1){
                        tmpWall = tmpWall || c1.checkWall(c);
                    }
                    if (tmpWall){c.setDirection();}
                    tmpWall = false;
                }
            }

        private void checkUnderBlock() {
                boolean tmp = false;
                Block[] local1 = blocks.toArray(new Block[0]);
                    tmp = false;
                    for(Block c1: local1){
                        tmp = tmp || c1.checkUnderBlock(player);
                    }
                    player.setUnderBlock(tmp);
                }

        private void checkDie(){
            Monster[] local = monsters.toArray(new Monster[0]);
            for(Monster c: local){
                    if(Collidable.checkColision(c, player)){
                        if(player.isCyan()){c.destroy();continue;}
                        restart();
                }
            }
        }


    }
    
    public boolean checkWall(Rectangle r){
        boolean tmp = false;
        Block[] local1 = blocks.toArray(new Block[0]);
            tmp = false;
            for(Block c1: local1){
                tmp = tmp || c1.checkWall(r);
            }
        return tmp;
    }
    
    public boolean checkOnBlock(Rectangle mask) {
        boolean tmp = false;
        Block[] local1 = blocks.toArray(new Block[0]);
            tmp = false;
            for(Block c1: local1){
                tmp = tmp || c1.checkOnBlock(mask);
            }
            return tmp;
        }

    public void increaseSkor(int score) {
        this.score += score;
    }

    public int WIDTH() {
        return WIDTH;
    }

    public void addDot(Dot dot) {
        add(dot);
        dots.add(dot);
    }

    public void addPowerUp(PowerUp p) {
        add(p);
        powerUps.add(p);
    }
    
    public void addMonster(Monster m){
        add(m);
        monsters.add(m);
    }
    
    public void removeMonster(Monster monster) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run(){
                    remove(monster);
                }
            });
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        monsters.remove(monster);
    }

    void restart() {
        player.destroy();

        Monster[] local = monsters.toArray(new Monster[0]);
        for(Monster m : local){
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run(){
                        remove(m);
                    }
                });
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        monsters.clear();

        Dot[] localD = dots.toArray(new Dot[0]);
        for(Dot d : localD){
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run(){
                        remove(d);
                    }
                });
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        dots.clear();

        Block[] local1 = blocks.toArray(new Block[0]);
        for(Block b:local1){
            b.initDots();
        }

        PowerUp[] local2 = powerUps.toArray(new PowerUp[0]);
        for(PowerUp p:local2){
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run(){
                        remove(p);
                    }
                });
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
                }
            }
        powerUps.clear();

        player = new Player((int)(UNIT*0.5), (int)(HEIGHT-2.5*UNIT), UNIT, UNIT,GamePanel.this);
        add(player);

        initMonsters();

        score = 0;

        repaint();
    }

}
