import com.sun.org.apache.bcel.internal.generic.JsrInstruction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;

public class GUI extends JFrame implements ActionListener {

    String[][] dataArray;

    private Data datahandler;

    private Container contentPane;

    //The size of screen of the device the program is running on
    private Dimension screenSize;
    private JTextField serviceNameField = new JTextField(20);
    private JTextField passwordField = new JTextField(20);

    private JTextField serviceNameEntry = new JTextField(20);
    private JPasswordField passwordEntry = new JPasswordField(20);
    private JButton submitButton = new JButton("Add");

    private JComboBox compromisedServiceCombo = new JComboBox();
    private JTextField compromisedTimeField = new JTextField(20);
    private JButton serviceSubmitButton = new JButton("Check");

    private ArrayList<JPanel> panelsList;
    public GUI() throws FileNotFoundException {
        super("HashSheffield");

        datahandler = new Data();
        dataArray = datahandler.readData();

        //Print out data array to the console
         for (int x = 0; x < dataArray.length; x ++) {
             for (int y = 0; y < 3; y++) {
                 System.out.print(dataArray[x][y] + " ");
             }
             System.out.println("");
         }

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

        //contentPane.setBackground(Color.orange);
        //Refreshes the graphical display
        revalidate();
    }

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu menu = new JMenu("Settings");
        menuBar.add(menu);

        JMenuItem styleMenu = new JMenuItem("Style");
        JMenuItem prefMenu = new JMenuItem("Preferences");
        JMenuItem dataMenu = new JMenuItem("Clear Data");
        menu.add(styleMenu);
        menu.add(prefMenu);
        menu.add(dataMenu);
    }

    private void setupListScreen() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //Panel for a new service entry
        JPanel entryPanel = new JPanel(new FlowLayout());
        serviceNameEntry.setText("Service Name");
        entryPanel.add(serviceNameEntry);
        entryPanel.add(passwordEntry);
        submitButton.addActionListener(this);
        entryPanel.add(submitButton);
        mainPanel.add(entryPanel);

        //Panel for entering a compromised service
        JPanel serviceCompromisedPanel = new JPanel(new FlowLayout());
        //Add all services
        for (int entry = 0; entry < dataArray.length; entry++) {
            compromisedServiceCombo.addItem(dataArray[entry][0]);
        }
        serviceCompromisedPanel.add(compromisedServiceCombo);
        serviceCompromisedPanel.add(compromisedTimeField);
        serviceCompromisedPanel.add(serviceSubmitButton);
        mainPanel.add(serviceCompromisedPanel);

        JPanel servicesPanel = new JPanel();
        panelsList = new ArrayList<JPanel>();
        servicesPanel.setLayout(new BoxLayout(servicesPanel, BoxLayout.Y_AXIS));
        for (int entry = 0; entry < dataArray.length; entry++) {
            //Check if the service string in the entry = the one entered by the user
            JPanel servicePanel = new JPanel(new FlowLayout());
            TextField serviceField = new TextField();
            serviceField.setText(dataArray[entry][0]);
            JPasswordField passwordUpdate = new JPasswordField(20);
            JButton updateButton = new JButton("Update");

            servicePanel.add(serviceField);
            servicePanel.add(passwordUpdate);
            servicePanel.add(updateButton);

            panelsList.add(servicePanel);
            servicesPanel.add(servicePanel);
        }
        JScrollPane servicesScrollPane = new JScrollPane(servicesPanel);
        //servicesScrollPane.getVerticalScrollBar().setSize(50,50);
        mainPanel.add(servicesScrollPane);

        contentPane.add(mainPanel);
        JPanel compromisedServicePanel = new JPanel();

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
        if (e.getSource() == submitButton) {
            String serviceName = serviceNameEntry.getText();
            String plainPassword = serviceNameEntry.getText();
            String unixTime = Long.toString(Instant.now().getEpochSecond());

            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            byte[] hash = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));

            //String[] dataEntry = new String[] {serviceName,  unixTime, hash.toString(),};

            String dataEntry =  serviceName + ";" + unixTime + ";" + hash.toString();

            try {
                datahandler.createNewPass(dataEntry);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        else if (e.getSource() == serviceSubmitButton) {
            String compromisedService = compromisedServiceCombo.getSelectedItem().toString();
            for (int entry = 0; entry < dataArray.length; entry++) {
                //Check if the service string in the entry = the one entered by the user
                if ((dataArray[entry][0] == compromisedService)) {
                    System.out.println("");
                    break;
                }
            }
        }
        else {
            System.out.println("nahmate" + e.getSource());
        }


        //System.out.println("Entry: " + serviceName + ", " + hash.toString() + ", " + unixTimestamp);
    }

    private ArrayList<String> getServicesWithHash (String hash) {
        ArrayList<String> services = new ArrayList<>();

        for (int entry = 0; entry < dataArray.length; entry++) {
            //Check if the hash in the entry = the hash parsed
            if (dataArray[entry][2] == hash) {
                //Add to the arraylist the service name
                services.add(dataArray[entry][0]);
            }
        }

        return services;
    }
}

