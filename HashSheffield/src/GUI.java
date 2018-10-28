
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;

public class GUI extends JFrame implements ActionListener, FocusListener {
    private int PASSWORD_COLUMNS = 10;
    private String PASSWORD_PLACEHOLDER = "Password";

    String[][] dataArray;

    private Data datahandler;

    private Container contentPane;

    //The size of screen of the device the program is running on
    private Dimension screenSize;
    //private JTextField serviceNameField = new JTextField(20);
    //private JTextField passwordField = new JTextField(20);

    private CustomTextField serviceNameEntry = new CustomTextField(8);
    private CustomPasswordField passwordEntry = new CustomPasswordField(PASSWORD_COLUMNS);
    private JButton submitButton = new JButton("Add");
    private JLabel serviceEnteredLabel = new JLabel();

    private JComboBox<String> compromisedServiceCombo = new JComboBox<String>();
    private CustomTextField compromisedTimeField = new CustomTextField(11);
    private JButton serviceSubmitButton = new JButton("Check");

    private JButton checkDuplicateServiceButton = new JButton("Check for duplicated services");

    private JPanel servicesPanel = new JPanel();
    private ArrayList<JPanel> panelsList = new ArrayList<JPanel>();

    Font systemFont = new Font("Serif", Font.BOLD, 24);
    Font buttonFont = new Font("Serif", Font.BOLD, 18);
    Font labelFont = new Font("Serif", Font.CENTER_BASELINE, 20);
    Font alertLabelFont = new Font("Serif", Font.CENTER_BASELINE, 16);

    public GUI() throws FileNotFoundException {
        super("HashSheffield");

        datahandler = new Data();
        dataArray = datahandler.readData();

        //Print out data array to the console
        for (String[] aDataArray : dataArray) {
            for (int y = 0; y < 4; y++) {
                System.out.print(aDataArray[y] + "  ");
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
        resizeWindow(windowSizeX / 2.4, windowSizeY / 1.2);

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
        newServiceLabel.setFont(labelFont);
        newServiceLabel.setText("                        Enter a new service");
        mainPanel.add(newServiceLabel);
        JPanel entryPanel = new JPanel(new FlowLayout());
        serviceNameEntry.setPlaceholder("Service Name");
        //serviceNameEntry.setText("Service Name");
        serviceNameEntry.setPreferredSize(new Dimension(120, 32));
        serviceNameEntry.setFont(systemFont);
        entryPanel.add(serviceNameEntry);
        passwordEntry.setFont(systemFont);
        passwordEntry.addFocusListener(this);
        passwordEntry.setPlaceholder(PASSWORD_PLACEHOLDER);
        passwordEntry.setEchoChar((char) 0);
        entryPanel.add(passwordEntry);
        entryPanel.setFont(systemFont);
        submitButton.addActionListener(this);
        submitButton.setFont(buttonFont);
        entryPanel.add(submitButton);
        serviceEnteredLabel.setVisible(false);
        serviceEnteredLabel.setFont(alertLabelFont);
        entryPanel.add(serviceEnteredLabel);
        mainPanel.add(entryPanel);

        //Panel for entering a compromised service
        JPanel serviceCompromisedPanel = new JPanel(new FlowLayout());
        JLabel compromisedServiceLabel = new JLabel();
        mainPanel.add(compromisedServiceLabel);
        compromisedServiceLabel.setFont(labelFont);
        compromisedServiceLabel.setText("    Flag a service that has had a data breach");
        //Add all services
        for (String[] aDataArray : dataArray) {
            compromisedServiceCombo.addItem(aDataArray[0]);
        }
        compromisedServiceCombo.setFont(systemFont);
        compromisedServiceCombo.setLightWeightPopupEnabled(false); // use heavyweight component
        serviceCompromisedPanel.add(compromisedServiceCombo);
        compromisedTimeField.setFont(systemFont);
        compromisedTimeField.setPlaceholder("Enter UNIX Time");
        serviceCompromisedPanel.add(compromisedTimeField);
        serviceSubmitButton.addActionListener(this);
        serviceSubmitButton.setFont(buttonFont);
        serviceCompromisedPanel.add(serviceSubmitButton);
        checkDuplicateServiceButton.addActionListener(this);
        checkDuplicateServiceButton.setFont(buttonFont);
        serviceCompromisedPanel.add(checkDuplicateServiceButton);
        mainPanel.add(serviceCompromisedPanel);

        //Panel for all services existing within data
        servicesPanel.setLayout(new BoxLayout(servicesPanel, BoxLayout.Y_AXIS));
        JLabel existingServiceLabel = new JLabel();
        existingServiceLabel.setFont(labelFont);
        existingServiceLabel.setText("          Update passwords for your services");
        mainPanel.add(existingServiceLabel);
        for (String[] aDataArray : dataArray) { //Thanks for the tidy up, @roberto
            //Check if the service string in the entry = the one entered by the user
            JPanel servicePanel = new JPanel(new FlowLayout());
            TextField serviceField = new TextField();
            serviceField.setPreferredSize(new Dimension(160, 32));
            serviceField.setFont(systemFont);
            serviceField.setEditable(false);
            serviceField.setText(aDataArray[0]);

            //here
            CustomPasswordField passwordUpdate = new CustomPasswordField(PASSWORD_COLUMNS);
            passwordUpdate.addFocusListener(this);
            passwordUpdate.setPlaceholder(PASSWORD_PLACEHOLDER);
            passwordUpdate.setEchoChar((char) 0);
            passwordUpdate.setPreferredSize(new Dimension(60, 32));
            passwordUpdate.setFont(systemFont);
            JButton updateButton = new JButton("Update");
            updateButton.addActionListener(this);
            updateButton.setFont(buttonFont);
            JLabel serviceUpdateLabel = new JLabel();
            serviceUpdateLabel.setVisible(false);
            serviceUpdateLabel.setFont(alertLabelFont);

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

        setServicesTextColours();

        contentPane.add(mainPanel);
        JPanel compromisedServicePanel = new JPanel();

    }

    private void setServicesTextColours() {
        for (int i = 0; i < panelsList.size(); i++) {
            //If the entry has been flagged as
            if (dataArray[i][3].equals("1")) {
                panelsList.get(i).getComponent(0).setBackground(Color.RED);
            }
            else {
                panelsList.get(i).getComponent(0).setBackground(Color.WHITE);
            }
        }
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
        System.out.println("Listener whoowhooowhooo");
        serviceEnteredLabel.setVisible(true);
        if (e.getSource() == submitButton) {
            String serviceName = serviceNameEntry.getText();
            if (!isServicePresent(serviceName)) {
                String plainPassword = passwordEntry.getText();
                String unixTime = Long.toString(Instant.now().getEpochSecond());
                System.out.println("Woweee you just entered a new service, " + serviceName + " congratis");

                String dataEntry =  serviceName + ";" + unixTime + ";" + hashMyString(plainPassword);

                try {
                    //Refresh the data array as we have now edited the storage file
                    datahandler.createNewPass(dataEntry);
                    dataArray = datahandler.readData();

                    //Add the new service to the list on the UI
                    JPanel servicePanel = new JPanel(new FlowLayout());
                    TextField serviceField = new TextField();
                    serviceField.setPreferredSize(new Dimension(160, 32));
                    serviceField.setFont(systemFont);
                    serviceField.setEditable(false);
                    serviceField.setText(serviceName);

                    //here
                    CustomPasswordField passwordUpdate = new CustomPasswordField(PASSWORD_COLUMNS);
                    passwordUpdate.addFocusListener(this);
                    passwordUpdate.setPlaceholder(PASSWORD_PLACEHOLDER);
                    passwordUpdate.setEchoChar((char) 0);
                    passwordUpdate.setPreferredSize(new Dimension(60, 32));
                    passwordUpdate.setFont(systemFont);
                    JButton updateButton = new JButton("Update");
                    updateButton.addActionListener(this);
                    updateButton.setFont(buttonFont);
                    JLabel serviceUpdateLabel = new JLabel();
                    serviceUpdateLabel.setFont(alertLabelFont);
                    serviceUpdateLabel.setVisible(false);

                    servicePanel.add(serviceField);
                    servicePanel.add(passwordUpdate);
                    servicePanel.add(updateButton);
                    servicePanel.add(serviceUpdateLabel);

                    panelsList.add(servicePanel);
                    servicesPanel.add(servicePanel);
                    //here
                    //Refresh the combo box
                    compromisedServiceCombo.addItem(serviceName);

                    serviceUpdateLabel.setFont(alertLabelFont);
                    //contentPane.removeAll();
                    //setupListScreen();
                    revalidate();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                serviceNameEntry.setText("Service name");
                passwordEntry.setText("");
                serviceEnteredLabel.setForeground(Color.GREEN);
                serviceEnteredLabel.setText(serviceName + " entered!");
            }
            else {
                serviceEnteredLabel.setForeground(Color.RED);
                serviceEnteredLabel.setText(serviceName + " already exists");
            }

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
                    setServicesTextColours();
                    break;
                }
            }
        }
        else if (e.getSource() == checkDuplicateServiceButton){
            User user = new User();
            try {
                ArrayList<String []> duplicatesArrayList = user.searchForPairs();
                System.out.println(duplicatesArrayList);
                StringBuilder outputString = new StringBuilder("These groups of services share a common password. You should change these to protect yourself from attacks:\n \n");
                for (int i = 0; i < duplicatesArrayList.size(); i++) {
                    for (int j = 0; j < duplicatesArrayList.get(i).length; j++) {
                        outputString.append(duplicatesArrayList.get(i)[j] + ", ");
                    }
                    outputString.append("\n");
                }
                JOptionPane.showMessageDialog(null, outputString.toString());
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        else {
            //For all buttons in the list of services
            for (int i = 0; i < panelsList.size(); i++) {
                if (e.getSource() == panelsList.get(i).getComponent(2)) {
                    //Get the password field of the appropriate element in the list
                    CustomPasswordField passwordField = (CustomPasswordField) panelsList.get(i).getComponent(1);

                    //Display correct labels
                    JLabel label = (JLabel)panelsList.get(i).getComponent(3);
                    label.setVisible(true);

                    String newHash = hashMyString(passwordField.getText());
                    //Check if the password is different to what is already stored
                    if (!dataArray[i][2].equals(newHash)) {
                        dataArray[i][2] = newHash; //update password hash
                        String unixTime = Long.toString(Instant.now().getEpochSecond());
                        dataArray[i][1] = unixTime; //update time
                        dataArray[i][3] = "0"; //entry no longer flagged as compromised
                        datahandler.updateFile(dataArray); //Update the file with new deets
                        label.setForeground(Color.GREEN);
                        label.setText("Password updated!");
                        setServicesTextColours(); //Reset text colours
                    }
                    else {
                        label.setText("No change!");
                        label.setForeground(Color.RED);
                    }


                    //Clear password field
                    passwordField.setText("");
                    passwordField.setPlaceholder(PASSWORD_PLACEHOLDER);
                    passwordField.setEchoChar((char) 0);
                    break;
                }


            }
            System.out.println("nahmate" + e.getSource());
        }


        //System.out.println("Entry: " + serviceName + ", " + hash.toString() + ", " + unixTimestamp);
    }

    private Boolean isServicePresent(String service) {
        for (int i = 0; i < dataArray.length; i++) {
            if (dataArray[i][0].equals(service)) {
                return true;
            }

        }
        return false;
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
                dataArray[entry][3] = "1"; //Flag entry as compromised
                datahandler.updateFile(dataArray); //Update the file with new flags
            }
        }

        return services;
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() == passwordEntry) {
            passwordEntry.setEchoChar('*');
            passwordEntry.setFont(systemFont);
        }
        else {
            for (int i = 0; i < panelsList.size(); i++) {
                //If its one of the password feels (needed for hiding and unhiding text
                if (e.getSource() == panelsList.get(i).getComponent(1)) {
                    CustomPasswordField passwordField = (CustomPasswordField) panelsList.get(i).getComponent(1);
                    passwordField.setEchoChar('*');
                    passwordField.setFont(systemFont);
                    break;
                }
            }
        }

    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() == passwordEntry) {
            if (passwordEntry.getText().length() == 0) {
                passwordEntry.setEchoChar((char) 0);
            } else {
                passwordEntry.setEchoChar('*');
            }
            passwordEntry.setFont(systemFont);
        }
        else {
            for (int i = 0; i < panelsList.size(); i++) {
                //If its one of the password feels (needed for hiding and unhiding text
                if (e.getSource() == panelsList.get(i).getComponent(1)) {
                    CustomPasswordField passwordField = (CustomPasswordField) panelsList.get(i).getComponent(1);
                    if (passwordField.getText().length() == 0) {
                        passwordField.setEchoChar((char) 0);
                    } else {
                        passwordField.setEchoChar('*');
                    }
                    passwordField.setFont(systemFont);
                    break;
                }
            }
        }
    }
}

