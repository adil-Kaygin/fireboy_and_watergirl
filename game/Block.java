import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JLabel;

public class Block extends JLabel implements Collidable{

    public final static int DOTSIZE = 6;

    private final int WIDTH;
    private final int HEIGHT;

    private int x;
    private int y;

    private GamePanel myPanel;

    public Block( int x, int y,int WIDTH, int HEIGHT,GamePanel myPanel) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.x = x;
        this.y = y;
        this.myPanel = myPanel;

        setBounds(x, y, WIDTH, HEIGHT);
        setOpaque(true);
        setBackground(Color.YELLOW);

        initDots();
        
    }
    
    public void initDots(){

        for(int i = 0; i*4*DOTSIZE < WIDTH;i++){
            myPanel.addDot(new Dot(x+DOTSIZE+4*i*DOTSIZE, y-2*DOTSIZE, DOTSIZE, DOTSIZE));
        }

    }

    @Override
    public Rectangle collisionMask(){
        return new Rectangle(x+MASKSIZE,y-MASKSIZE,WIDTH-MASKSIZE,HEIGHT+2*MASKSIZE);
    }

    public boolean checkOnBlock(Collidable c) {
        Rectangle mask = this.collisionMask();
        mask.height = MASKSIZE;
        return mask.intersects(c.collisionMask());
    }
    
    public boolean checkWall(Collidable c) {
        Rectangle mask = this.collisionMask();
        mask.width = WIDTH+3*MASKSIZE;
        mask.x = x-3*MASKSIZE;
        mask.y = y+3*MASKSIZE;
        mask.height = HEIGHT-3*MASKSIZE;

        return mask.intersects(c.collisionMask());
    }
    
    public boolean checkWall(Rectangle c) {
        Rectangle mask = this.collisionMask();
        mask.width = WIDTH;
        mask.x = x;
        mask.y = y+3*MASKSIZE;
        mask.height = HEIGHT-3*MASKSIZE;

        return mask.intersects(c);
    }
    
    public boolean checkUnderBlock(Player player) {

        Rectangle mask = this.collisionMask();
        mask.height = MASKSIZE;
        mask.y = y+HEIGHT;

        return mask.intersects(player.collisionMask());

    }

    public boolean checkOnBlock(Rectangle m) {

        Rectangle mask = this.collisionMask();
        mask.height = MASKSIZE;
        mask.y = y-MASKSIZE;
        
        return mask.intersects(m);
    }

}