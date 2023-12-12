package org.chatapp.swing;

import lombok.Data;

import java.awt.*;


public class ActiveStatus extends Component {
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        repaint();
    }

    private boolean active;

    public ActiveStatus() {
        setPreferredSize(new Dimension(8, 8));
    }

    @Override
    public void paint(Graphics grphcs) {
        if (active) {
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(62, 165, 49));
            g2.fillOval(0, (getHeight() / 2) - 4, 8, 8);
        }
    }
}
