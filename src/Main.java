import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame obj=new JFrame();
        GameManager GameManager = new GameManager();

        obj.setBounds(10, 10, 700, 600);
        obj.setTitle("Brick Breaker");
        obj.setResizable(false);
        obj.setVisible(true);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(GameManager);
        obj.setVisible(true);

    }
}