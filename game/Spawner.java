import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class Spawner implements Serializable{

    private static final long SPAWN = 6000;
    private int x;
    private int y;
    private int WIDTH;
    private int HEIGHT;

    private boolean isRunning = true;


    private GamePanel myPanel;

    private Monster monster;

    public Spawner(int x, int y, int WIDTH, int HEIGHT, GamePanel myPanel) {
        this.x = x;
        this.y = y;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.myPanel = myPanel;

        initTimer();
    }

    public void initTimer() {
        new Timer().schedule( new TimerTask() {public void run() {spawn();}}, 0,SPAWN);
    }

    private void spawn(){

        if(!isRunning){return;}

        monster = new Monster(x, y, WIDTH, HEIGHT, myPanel);
        myPanel.addMonster(monster);
    }

    public void pause() {
        isRunning = !isRunning;
    }

}
