import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Main {

    public static void main(String[] args) {

        if(args.length != 0){
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(args[0]+".bin"));
                GamePanel saved = (GamePanel)in.readObject();
                in.close();
                new GameFrame(saved);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            new GameFrame();
        }
        
    }
}