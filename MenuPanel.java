import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MenuPanel extends JPanel {
    public void update() {

    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect(0, 0, 50, 50);
    }
}
