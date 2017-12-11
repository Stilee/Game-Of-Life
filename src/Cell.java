import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Cell extends JButton implements ActionListener {
    private boolean isAlive;

    public Cell() {
        isAlive = false;
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        isAlive = !isAlive;
    }

    public void setDead() {isAlive = false;}

    public void setAlive() {isAlive = true;}

    public boolean getStatus() {
        return isAlive;
    }


}