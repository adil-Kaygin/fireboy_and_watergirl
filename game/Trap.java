import java.awt.Color;
import java.awt.Rectangle;

public class Trap extends Block{

    private boolean r; // red true / blue false

    public Trap(int x, int y, int WIDTH, int HEIGHT,Boolean r,GamePanel myPanel) {
        super(x, y, WIDTH, HEIGHT, myPanel);
        this.r = r;
        setBackground((r) ? Color.RED : Color.BLUE);
    }

    @Override
    public boolean checkOnBlock(Collidable c) {
        Rectangle mask = c.collisionMask();
        mask.y +=5;
        boolean a = super.checkOnBlock(mask);

        if(c instanceof Player && a){((Player)(c)).checkTrap(r);}
        
        return a;
    }
    
}
