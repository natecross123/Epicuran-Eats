import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * The Customer class represents a JFrame application for creating new reservations.
 * It provides fields for entering customer information such as name, phone number,
 * email, number of persons, date of reservation, and time of reservation.
 * The class also includes buttons to save the reservation, close the window, and
 * view the restaurant reservation list.
 */

public class Customer extends JFrame {
    private JTextField txtName;
    private JTextField txtPhoneNumber;
    private JTextField txtEmail;
    private JTextField txtNumberofPersons;
    private JTextField txtDateofReservation;
    private JTextField txtTimeofReservation;

    private JComboBox<String> ambianceComboBox;
    

    private JButton cmdSave;
    private JButton cmdClose;
    private JButton cmdResturantReservationList;

    private JPanel pnlCommand;
    private JPanel pnlDisplay;

    private Customer thiscus;
    private Reservation thisForm;
    private Reservation Rscreen;
    private WelcomeScreen screen;

    public Customer(String test) {
        setTitle("Create New Reservation");
        
        pnlCommand = new JPanel();
        pnlDisplay = new JPanel();

    
        String[] ambianceOptions = {"Romantic", "Family Gathering", "Friend Group", "Fine Dining", "Outdoor"};
        ambianceComboBox = new JComboBox<>(ambianceOptions);
        ambianceComboBox.setSelectedIndex(0); // Default selection
        pnlCommand.add(ambianceComboBox); // Add to panel

        pnlDisplay.setLayout(new GridLayout(6, 2));

        pnlDisplay.add(new JLabel("Name:"));
        txtName = new JTextField(20);
        pnlDisplay.add(txtName);

       
        pnlDisplay.add(new JLabel("Phone Number:"));
        txtPhoneNumber = new JTextField(20);
        pnlDisplay.add(txtPhoneNumber);

        pnlDisplay.add(new JLabel("Email Address:"));
        txtEmail = new JTextField(20);
        pnlDisplay.add(txtEmail);

        pnlDisplay.add(new JLabel("Number of Persons:"));
        txtNumberofPersons = new JTextField(20);
        pnlDisplay.add(txtNumberofPersons);

        pnlDisplay.add(new JLabel("Date of Reservation:"));
        txtDateofReservation = new JTextField(20);
        pnlDisplay.add(txtDateofReservation);

        pnlDisplay.add(new JLabel("Time of Reservation:"));
        txtTimeofReservation = new JTextField(20);
        pnlDisplay.add(txtTimeofReservation);

        cmdSave = new JButton("Save");
        cmdSave.setBackground(new Color(255, 204, 0));  
        cmdSave.setForeground(Color.WHITE);  


        cmdClose = new JButton("Close");
        cmdClose.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cmdClose.setBackground(new Color(230, 66, 25));  // Set your desired color
            }
        
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cmdClose.setBackground(UIManager.getColor("control"));  // Reset to default color
            }
        });
        
        cmdResturantReservationList = new JButton("Restaurant Reservation List");
        cmdResturantReservationList.setBackground(new Color(204, 102, 0));  // Change to your desired color
        cmdResturantReservationList.setForeground(Color.WHITE);  // Change to your desired color

        cmdSave.addActionListener(new SaveButtonListener());
        cmdClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Action listener for the Restaurant Reservation List button

        cmdResturantReservationList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(null, "Show Restaurant Reservation List");
                // Directly create and display a new ReservationListingScreen instance
                //ReservationListingScreen reservationListingScreen = new ReservationListingScreen();
                //thisForm.setVisible(true);
                openResScreen();
            }
        
        });

    /**
     * Constructs a new Customer instance.
     * @param test A placeholder parameter (not used).
     */
        

        pnlCommand.add(cmdSave);
        pnlCommand.add(cmdClose);
        pnlCommand.add(cmdResturantReservationList);
        add(pnlDisplay, BorderLayout.CENTER);
        add(pnlCommand, BorderLayout.SOUTH);
        pack();
        setVisible(true);


        
    }

     /**
     * ActionListener for the Save button.
     * Responsible for validating input and saving the reservation.
     */ 
    private class SaveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = txtName.getText();
            String phoneNumber = txtPhoneNumber.getText();
            String email = txtEmail.getText();
            String numberOfPersons = txtNumberofPersons.getText();
            String dateofReservation= txtDateofReservation.getText();
            String timeOfReservation = txtTimeofReservation.getText();
            String selectedAmbiance = (String) ambianceComboBox.getSelectedItem();

            int numperson= 0;

            try{
                numperson =Integer.parseInt(numberOfPersons);
            }catch(NumberFormatException ex){
            }

            // Basic validation
            if (name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || numberOfPersons.isEmpty()
                    || timeOfReservation.isEmpty()) {
                JOptionPane.showMessageDialog(Customer.this, "Please fill in all fields.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }else{
                CustomerInfo person = new CustomerInfo(name, phoneNumber, email, numperson, dateofReservation, timeOfReservation, selectedAmbiance);
                // Assuming a ReservationListingScreen class is available
                if (thisForm != null) { // Add a null check before invoking methods on thisForm
                    thisForm.addPerson(person);
                }
              

            // Optionally, you can clear the fields after saving
            clearFields();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("ResList.txt", true))) {
                writer.write(person.getName() + " " + person.getnNum()+ " "+ person.getMail()+
                " "+ person.getRes()+ " "+ person.getDate()+ " "+ person.getTime());
                writer.newLine();
                JOptionPane.showMessageDialog(Customer.this, "Save successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE); // Display the save successful message
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Failed to save person data.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            
        }
    }

     /**
     * Opens the Reservation screen to view the reservation list.
     */
    private void openResScreen() {
        // Open the Reservation screen
        Reservation res= new Reservation();
        res.setVisible(true);
        dispose(); // Close the current welcome screen
    }


    /**
     * Clears all input fields.
     */


    private void clearFields() {
        txtName.setText("");
        txtPhoneNumber.setText("");
        txtEmail.setText("");
        txtNumberofPersons.setText("");
        txtDateofReservation.setText("");
        txtTimeofReservation.setText("");
    }

    
}