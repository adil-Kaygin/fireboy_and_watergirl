import javax.swing.JFrame;

public class GameFrame extends JFrame{
    
    public GameFrame(){

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Ateş ve Su");

        getContentPane().add(new GamePanel());
        pack();

        setLocationRelativeTo(null);
        setResizable(false);
        
        setVisible(true);
    }

    public GameFrame(GamePanel saved) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Ateş ve Su");

        saved.initSave();

        getContentPane().add(saved);
        pack();

        setLocationRelativeTo(null);
        setResizable(false);
        
        setVisible(true);
    }
}
