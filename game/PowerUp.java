import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.Random;

public class PowerUp extends JLabel implements Collidable{

    private final double[] P = {0.03,0.3,0.55 ,0.8}; //%3 , %27 ,%25 , %25 ,%20

    private final int MASKSIZE = 1;
    
    private final int WIDTH = 6;
    private final int HEIGHT = 6;

    private int x;
    private int y;

    private Random rand = new Random();

    private int type;

    public PowerUp(int x,int y){
        this.x = x;
        this.y = y;

        setBounds(x, y, WIDTH, HEIGHT);
        setOpaque(true);

        double r = rand.nextDouble();

        if(r<P[0]){ 
            setBackground(Color.PINK);
            type =0;
        }
        else if(r<P[1]){
            setBackground(Color.CYAN); // pac-man hayalet
            type =1;
        }
        else if(r<P[2]){ //you are invisible for red traps
            setBackground(Color.RED);
            type =2;
        }
        else if(r<P[3]){ // invisible blue
            setBackground(Color.BLUE);
            type =3;
        }
        else{ //  magenta
            setBackground(Color.MAGENTA); // more jump
            type =4;
        }
    }

    @Override
    public Rectangle collisionMask(){
        return new Rectangle(x+MASKSIZE,y+MASKSIZE,WIDTH+MASKSIZE,HEIGHT+MASKSIZE);
    }

    public int getType() {
        return type;
    }

}
