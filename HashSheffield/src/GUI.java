import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    private Container contentPane;

    //The size of screen of the device the program is running on
    private Dimension screenSize;

    public GUI()  {
        super("HashSheffield");

        contentPane = getContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        //Get the size of the screen from the awt toolkit
        Toolkit windowToolkit = Toolkit.getDefaultToolkit();
        screenSize = windowToolkit.getScreenSize();
    }
}

