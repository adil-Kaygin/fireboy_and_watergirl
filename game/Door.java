import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JLabel;

public class Door extends JLabel implements Collidable{

    private final int WIDTH;
    private final int HEIGHT;

    private int x;
    private int y;

    public Door( int x, int y,int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.x = x;
        this.y = y;

        setBounds(x, y, WIDTH, HEIGHT);
        setOpaque(true);
        setBackground(Color.PINK);
    }
    
    
    @Override
    public Rectangle collisionMask(){
        return new Rectangle(x+MASKSIZE,y+MASKSIZE,WIDTH+MASKSIZE,HEIGHT+MASKSIZE);
    }
    
}
