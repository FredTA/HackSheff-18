import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class GUI extends JFrame implements ActionListener {

    private Container contentPane;

    //The size of screen of the device the program is running on
    private Dimension screenSize;
    private JTextField serviceNameField = new JTextField(20);
    private JTextField passwordField = new JTextField(20);

    private JTextField serviceNameEntry;
    JPasswordField passwordEntry;
    private JButton submitButton;

    public GUI()  {
        super("HashSheffield");

        contentPane = getContentPane();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        //Get the size of the screen from the awt toolkit
        Toolkit windowToolkit = Toolkit.getDefaultToolkit();
        screenSize = windowToolkit.getScreenSize();

        //calculate the size of the square window based on the size of the screen
        double windowSizeX = screenSize.width;
        double windowSizeY = screenSize.height;

        //Setup the window with the appropriate size
        resizeWindow(windowSizeX / 1.2, windowSizeY / 1.2);

        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));


        setupMenu();
        setupListScreen();

        contentPane.setBackground(Color.orange);
        //Refreshes the graphical display
        revalidate();

    }

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu menu = new JMenu("Settings");
        menuBar.add(menu);

        JMenuItem styleMenu = new JMenuItem("Style");
        JMenuItem dataMenu = new JMenuItem("Clear Data");
        menu.add(styleMenu);
        menu.add(dataMenu);
    }

    private void setupListScreen() {
        JPanel entryPanel = new JPanel(new FlowLayout());

        serviceNameEntry = new JTextField(20);
        serviceNameEntry.setText("Service Name");
        passwordEntry = new JPasswordField(20);

        entryPanel.add(serviceNameEntry);
        entryPanel.add(passwordEntry);

        submitButton = new JButton("Add");
        submitButton.addActionListener(this);
        entryPanel.add(submitButton);

        contentPane.add(entryPanel);
    }

    //Resizes the window according to screen size and centres the window
    private void resizeWindow(double windowSizeX, double windowSizeY) {
        //Resize and center the window
        setSize((int)windowSizeX, (int)windowSizeY);
        int windowPosX = (int)((screenSize.width / 2) - windowSizeX / 2);
        int windowPosY = (int)((screenSize.height / 2) - windowSizeY / 2);
        setLocation(new Point(windowPosX, windowPosY));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String serviceName = serviceNameEntry.getText();
        String plainPassword = serviceNameEntry.getText();
        String unixTimestamp = Long.toString(Instant.now().getEpochSecond());


        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        byte[] hash = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));

        String[] dataEntry = new String[] {serviceName, hash.toString(), unixTimestamp};

        System.out.println("Entry: " + serviceName + ", " + hash.toString() + ", " + unixTimestamp);
    }
}

