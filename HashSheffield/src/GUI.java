
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;

public class GUI extends JFrame implements ActionListener {
    private int PASSWORD_COLUMNS = 10;

    String[][] dataArray;

    private Data datahandler;

    private Container contentPane;

    //The size of screen of the device the program is running on
    private Dimension screenSize;
    //private JTextField serviceNameField = new JTextField(20);
    //private JTextField passwordField = new JTextField(20);

    private JTextField serviceNameEntry = new JTextField(10);
    private JPasswordField passwordEntry = new JPasswordField(PASSWORD_COLUMNS);
    private JButton submitButton = new JButton("Add");
    private JLabel serviceEnteredLabel = new JLabel();

    private JComboBox<String> compromisedServiceCombo = new JComboBox<String>();
    private JTextField compromisedTimeField = new JTextField(PASSWORD_COLUMNS);
    private JButton serviceSubmitButton = new JButton("Check");

    private JPanel servicesPanel = new JPanel();
    private ArrayList<JPanel> panelsList = new ArrayList<JPanel>();

    public GUI() throws FileNotFoundException {
        super("HashSheffield");

        datahandler = new Data();
        dataArray = datahandler.readData();

        //Print out data array to the console
        for (String[] aDataArray : dataArray) {
            for (int y = 0; y < 3; y++) {
                System.out.print(aDataArray[y] + " ");
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
        JLabel newServiceLabel = new JLabel();
        newServiceLabel.setText("Enter a new service");
        mainPanel.add(newServiceLabel);
        JPanel entryPanel = new JPanel(new FlowLayout());
        serviceNameEntry.setText("Service Name");
        entryPanel.add(serviceNameEntry);
        entryPanel.add(passwordEntry);
        submitButton.addActionListener(this);
        entryPanel.add(submitButton);
        serviceEnteredLabel.setVisible(false);
        entryPanel.add(serviceEnteredLabel);
        mainPanel.add(entryPanel);

        //Panel for entering a compromised service
        JPanel serviceCompromisedPanel = new JPanel(new FlowLayout());
        JLabel compromisedServiceLabel = new JLabel();
        compromisedServiceLabel.setText("Flag a service that has had a data breach");
        mainPanel.add(compromisedServiceLabel);
        //Add all services
        for (String[] aDataArray : dataArray) {
            compromisedServiceCombo.addItem(aDataArray[0]);
        }
        serviceCompromisedPanel.add(compromisedServiceCombo);
        serviceCompromisedPanel.add(compromisedTimeField);
        serviceSubmitButton.addActionListener(this);
        serviceCompromisedPanel.add(serviceSubmitButton);
        mainPanel.add(serviceCompromisedPanel);

        //Panel for all services existing within data
        servicesPanel.setLayout(new BoxLayout(servicesPanel, BoxLayout.Y_AXIS));
        JLabel existingServiceLabel = new JLabel();
        existingServiceLabel.setText("Update passwords for your services");
        mainPanel.add(existingServiceLabel);
        for (String[] aDataArray : dataArray) { //Thanks for the tidy up, @roberto
            //Check if the service string in the entry = the one entered by the user
            JPanel servicePanel = new JPanel(new FlowLayout());
            TextField serviceField = new TextField();
            serviceField.setPreferredSize(new Dimension(120, 20));
            serviceField.setText(aDataArray[0]);

            JPasswordField passwordUpdate = new JPasswordField(PASSWORD_COLUMNS);
            passwordUpdate.setPreferredSize(new Dimension(60, 20));
            JButton updateButton = new JButton("Update");
            updateButton.addActionListener(this);
            JLabel serviceUpdateLabel = new JLabel();
            serviceUpdateLabel.setVisible(false);

            servicePanel.add(serviceField);
            servicePanel.add(passwordUpdate);
            servicePanel.add(updateButton);
            servicePanel.add(serviceUpdateLabel);

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
            String plainPassword = passwordEntry.getText();
            String unixTime = Long.toString(Instant.now().getEpochSecond());
            System.out.println("Woweee you just entered a new service, " + serviceName + " conFUCKINGgratis");

            String dataEntry =  serviceName + ";" + unixTime + ";" + hashMyString(plainPassword);

            try {
                //Refresh the data array as we have now edited the storage file
                datahandler.createNewPass(dataEntry);
                dataArray = datahandler.readData();

                //Add the new service to the list on the UI
                JPanel servicePanel = new JPanel(new FlowLayout());
                TextField serviceField = new TextField();
                serviceField.setText(serviceName);
                JPasswordField passwordUpdate = new JPasswordField(PASSWORD_COLUMNS);
                JButton updateButton = new JButton("Update");

                servicePanel.add(serviceField);
                servicePanel.add(passwordUpdate);
                servicePanel.add(updateButton);

                panelsList.add(servicePanel);
                servicesPanel.add(servicePanel);

                //Refresh the combo box
                compromisedServiceCombo.addItem(serviceName);

                serviceEnteredLabel.setVisible(true);
                serviceEnteredLabel.setText(serviceName + " entered!");
                //contentPane.removeAll();
                //setupListScreen();
                revalidate();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            serviceNameEntry.setText("Service name");
            passwordEntry.setText("");
        }
        else if (e.getSource() == serviceSubmitButton) {
            String compromisedService = compromisedServiceCombo.getSelectedItem().toString();
            System.out.println();
            System.out.println("Looking for hashes matching that of " + compromisedService);
            for (String[] aDataArray : dataArray) {
                //Check if the service string in the entry = the one entered by the user
                if ((aDataArray[0] == compromisedService)) {
                    System.out.println("Hash value: " + aDataArray[2]);
                    //Sry for the leng line :'( cba to fix
                    int timeOfBreach = Integer.parseInt(compromisedTimeField.getText());
                    ArrayList<String> matchingServiceList = getServicesWithHashAfterTime(aDataArray[2], timeOfBreach);
                    System.out.println("Accounts that are at risk: ");
                    StringBuilder message = new StringBuilder(("You should change your password for the following services\n"));
                    for (String aMatchingServiceList : matchingServiceList) {
                        System.out.println(aMatchingServiceList);
                        message.append(aMatchingServiceList).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, message.toString());
                    break;
                }
            }
        }
        else {
            //For all buttons in the list of services
            for (int i = 0; i < panelsList.size(); i++) {
                if (e.getSource() == panelsList.get(i).getComponent(2)) {
                    //Get the password field of the appropriate element in the list
                    JTextField passwordField = (JTextField)panelsList.get(i).getComponent(1);
                    dataArray[i][2] = hashMyString(passwordField.getText()); //update password hash
                    String unixTime = Long.toString(Instant.now().getEpochSecond());
                    dataArray[i][1] = unixTime; //update time
                    datahandler.updateFile(dataArray); //Update the file with new deets

                    //Display correct labels
                    JLabel label = (JLabel)panelsList.get(i).getComponent(3);
                    label.setVisible(true);
                    label.setText("Password updated!");

                    //Clear password field
                    passwordField.setText("");
                    break;
                }

            }
            System.out.println("nahmate" + e.getSource());
        }


        //System.out.println("Entry: " + serviceName + ", " + hash.toString() + ", " + unixTimestamp);
    }

    private String hashMyString(String stringToHash) {
        String hashText = "";

        try {
            // Static getInstance method is called with hashing SHA
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // digest() method called
            // to calculate message digest of an input
            // and return array of byte
            byte[] messageDigest = md.digest(stringToHash.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            hashText = no.toString(16);

            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }

        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e1) {
            System.out.println("Exception thrown"
                    + " for incorrect algorithm: " + e1);

        }
        return hashText;
    }

    private ArrayList<String> getServicesWithHashAfterTime (String hash, int time) {
        ArrayList<String> services = new ArrayList<>();

        for (int entry = 0; entry < dataArray.length; entry++) {
            System.out.println(entry + ": " + dataArray[entry][2]);
            //Check if the hash in the entry = the hash parsed and the time passed in is greater than the time updated
            if (dataArray[entry][2].equals(hash) && Integer.parseInt(dataArray[entry][1]) < time) {
                //Add to the arraylist the service name
                System.out.println(dataArray[entry][0] + " matches!");
                services.add(dataArray[entry][0]);
            }
        }

        return services;
    }
}

