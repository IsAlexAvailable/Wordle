import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
    private BorderLayout layout;
    private GuessPanel guessPanel;

    public GamePanel() {
        guessPanel = new GuessPanel();
        initLayout();
        setBackground(new Color(30,30,30));
    }
    
    public void initLayout() {
        layout = new BorderLayout();
        setLayout(layout);
        add(guessPanel, BorderLayout.CENTER);
    }
    public void update() {
        
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);    //  very necessary
    }
}
