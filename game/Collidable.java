import java.awt.Rectangle;

public interface Collidable {
    
    int MASKSIZE= 1;
    
    Rectangle collisionMask();

    public static boolean checkColision(Collidable obj1,Collidable obj2){
        return obj1.collisionMask().intersects(obj2.collisionMask());
    }
}
