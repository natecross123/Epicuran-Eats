import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


/**
 * The Reservation class represents a JFrame application for managing reservations.
 * It allows users to view, make, and cancel reservations, as well as sort them by date or time.
 * Reservations are displayed in a table format, and the data is stored in a file named "ResList.txt".
 */

public class Reservation extends JFrame{
    private JButton sortByDateButton;
    private JButton sortByTimeButton;
    private JButton closeButton;
    private JButton makeReservationButton;
    private JButton editReservationButton;
    private JButton cancelReservationButton;
    

    private JTextField firstNameField;

    JButton button;
    JButton Time;

    private JPanel pnlDisplay;
    private JPanel pnlCommand;

    private ArrayList<CustomerInfo> llist;
    private JTable table;
    private DefaultTableModel model;
    private  JScrollPane scrollPane;

    public Reservation() {
        setTitle("Reservation Listing");
        setSize(500, 500);

        
        pnlCommand = new JPanel();
        pnlDisplay = new JPanel();
       
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pnlDisplay.setLayout(new GridLayout(2, 8));

        llist= loadPeople("ResList.txt");
        String[] columnNames= {"First Name",
                            "Last Name",
                            "Phone Number",
                            "Email",
                            "Amount of People",
                            "Date of Reservation",
                            "Time", "Ambiance"};
        model= new DefaultTableModel(columnNames,0);
        table= new JTable(model);
        showTable(llist);

        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(new Dimension(500, llist.size()*15 +50));

        
        scrollPane = new JScrollPane(table);
        
       
        add(scrollPane);
        /**
         * The button to sort reservations by date.
         */
        sortByDateButton = new JButton("Sort By Date");
        sortByDateButton.setBackground(new Color(242, 150, 13));
        sortByDateButton.addActionListener(new SortByDate());

         /**
         * The button to sort reservations by time.
         */

        sortByTimeButton = new JButton("Sort by Time");
        sortByTimeButton.setBackground(new Color(242, 150, 13));
        sortByTimeButton.addActionListener(new SortByTime());
         /**
         * The button to close the application.
         */
        closeButton = new JButton("Close");
        closeButton.setBackground(Color.RED);
        closeButton.addActionListener(new CloseButtonListener());
    
        /**
     * The button to make a new reservation.
     */
        makeReservationButton = new JButton("Make Reservation");
        makeReservationButton.setBackground(new Color(242, 150, 13));
        makeReservationButton.addActionListener(new MakeReservation());

        editReservationButton = new JButton("Edit Reservation");
        editReservationButton.setBackground(new Color(242, 150, 13));
        editReservationButton.addActionListener(new EditReservation());

        cancelReservationButton = new JButton("Cancel Reservation");
        cancelReservationButton.setBackground(new Color (242, 150, 13));
        cancelReservationButton.addActionListener(new CancelReservation());
       
       /**
     * The panel for command buttons.
     */
        
        pnlCommand.add(sortByDateButton);
        pnlCommand.add(sortByTimeButton);
        pnlCommand.add(makeReservationButton);
        pnlCommand.add(editReservationButton);
        pnlCommand.add(cancelReservationButton);
        pnlCommand.add(closeButton);
        
        

        add(pnlCommand, BorderLayout.SOUTH);
        pack();
        setVisible(true);   

        
    }
    /**
     * Displays the reservations in the table.
     * @param llist The list of reservations to display.
     */
    private void showTable(ArrayList<CustomerInfo> llist)
    {

         for (CustomerInfo person: llist){
             addToTable(person);
         }
    }
    /**
     * Adds a new reservation to the list and table.
     * @param p The reservation to add.
     */
    private void addToTable(CustomerInfo p)
    {
        String[] name= p.getName().split(" ");
        String[] item={name[0],name[1],""+ p.getnNum(),""+p.getMail(),""+
        p.getRes(),""+p.getDate(),""+p.getTime(), ""+ p.getAmbiance()};
        model.addRow(item);        

    }

    public void addPerson(CustomerInfo p)
    {
        llist.add(p);
        addToTable(p);

    }

    public void removePersonFromTable() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            model.removeRow(selectedRow);
        } else {
            System.out.println("No person selected to remove.");
        }
    }

    /**
     * Removes a selected reservation from the file.
     * @param personName The name of the person whose reservation should be removed.
     */
    public void removePersonFromFile(String personName) {
        String filePath = "ResList.txt"; // Update this path as necessary
        List<String> lines = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Remove the line corresponding to the person to be removed
        lines.removeIf(line -> line.contains(personName));
    
        // Write the remaining lines back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    /**
     * Comparator for sorting reservations by time.
     */

    public class TimeComp implements Comparator<CustomerInfo>{
        public int compare(CustomerInfo p1, CustomerInfo p2){
            return  p1.getTime().compareTo(p2.getTime());
        }
    }

    public class Datecomp implements Comparator<CustomerInfo>{
        public int compare(CustomerInfo p1, CustomerInfo p2){
            return p1.getDate().compareTo(p2.getDate());
        }
    }

    private class SortByDate implements ActionListener{
        public void actionPerformed(ActionEvent e){
            Collections.sort(llist, new Datecomp());
            model.setRowCount(0);
            for (CustomerInfo people: llist){
                     String [] name=people.getName().split(" ");
                     model.addRow(new Object[]{name[0], name[1], people.getnNum(),people.getMail(),
                     people.getRes(), people.getDate(), people.getTime(),people.getAmbiance()});
                }
        }
    }

    private class SortByTime implements ActionListener{
        public void actionPerformed(ActionEvent e){
            Collections.sort(llist, new TimeComp());
            model.setRowCount(0);
            for (CustomerInfo people: llist){
                    String [] name=people.getName().split(" ");
                    model.addRow(new Object[]{name[0], name[1], people.getnNum(),people.getMail(),
                people.getRes(), people.getDate(), people.getTime(),people.getAmbiance()});
                }
        }
    }

    private class CloseButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            System.exit(0);
        }

        
    }

   /**
     * ActionListener for the Make Reservation button.
     */

   private class MakeReservation implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        // Add functionality to link back to customer
        linkBackToCustomer();
        dispose();

        
        
    }
}
     /**
     * Action to link back to the Customer class when the Make Reservation button is clicked.
     */
private void linkBackToCustomer() {
    Customer customer = new Customer("");
    System.out.println("Linking back to customer...");
    
}

    
    private String getSelectedPersonName() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            // Assuming the name is stored in the first two columns of the table
            String firstName = (String) model.getValueAt(selectedRow, 0); // First Name
            String lastName = (String) model.getValueAt(selectedRow, 1); // Last Name
            return firstName + " " + lastName; // Combine first and last name
        } else {
            System.out.println("No row selected.");
            return null;
        }
    }

    private class CancelReservation implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String personName = getSelectedPersonName();
            if (personName != null) {
                removePersonFromTable();
                removePersonFromFile(personName);
            } else {
                System.out.println("No person selected to remove.");
            }
        }
    }

   
    /**
     * Loads reservations from a file.
     * @param lfile The file to load reservations from.
     * @return The list of loaded reservations.
     */
    

    private ArrayList<CustomerInfo> loadPeople(String lfile)
    {
        Scanner pscan = null;
        ArrayList<CustomerInfo> llist = new ArrayList<CustomerInfo>();
        try
        {
            pscan  = new Scanner(new File(lfile));
            while(pscan.hasNext())
            {
                String [] nextLine = pscan.nextLine().split(" ");
                if(nextLine.length >= 8){
                    String name = nextLine[0]+ " " + nextLine[1];
                String num = nextLine[2];
                String mail= nextLine[3];
                int res= Integer.parseInt(nextLine[4]);
                String date= nextLine[5];
                String time= nextLine[6];
                String ambiance= nextLine[7];

                CustomerInfo p = new CustomerInfo(name, num, mail,res, date, time, ambiance);
                llist.add(p);
                }
            }

            pscan.close();
        }
        catch(IOException e)
        {}
        return llist;
    }

     /**
     * ActionListener for the Edit Reservation button.
     */
    
    private class EditReservation implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // Retrieve the selected reservation from the table
                CustomerInfo selectedReservation = llist.get(selectedRow);
    
                // Prompt the user to modify the details of the reservation
                String newName = JOptionPane.showInputDialog("Enter new name:", selectedReservation.getName());
                String newNum = JOptionPane.showInputDialog("Enter new phone number:", selectedReservation.getnNum());
                String newMail = JOptionPane.showInputDialog("Enter new email:", selectedReservation.getMail());
                String newRes = JOptionPane.showInputDialog("Enter new amount of people:", selectedReservation.getRes());
                String newDate = JOptionPane.showInputDialog("Enter new date of reservation:", selectedReservation.getDate());
                String newTime = JOptionPane.showInputDialog("Enter new time:", selectedReservation.getTime());

                String [] nameParts =newName.split(" ");
                String fName = "";
                String lName = "";
                if(nameParts.length >= 2){
                    fName = nameParts[0];
                    lName = nameParts[1];
                }
    
                // Update the selected reservation with the modified details
                selectedReservation.setName(fName + " "+ lName);
                selectedReservation.setnNum(newNum);
                selectedReservation.setMail(newMail);
                selectedReservation.setRes(Integer.parseInt(newRes));
                selectedReservation.setDate(newDate);
                selectedReservation.setTime(newTime);
    
                // Update the table with the edited reservation details
                model.setValueAt(fName, selectedRow, 0);
                model.setValueAt(lName, selectedRow, 1);
                model.setValueAt(newNum, selectedRow, 2);
                model.setValueAt(newMail, selectedRow, 3);
                model.setValueAt(newRes, selectedRow, 4);
                model.setValueAt(newDate, selectedRow, 5);
                model.setValueAt(newTime, selectedRow, 6);
    
                // Update the file with the edited reservation details
                updateReservationInFile(selectedReservation);
            } else {
                JOptionPane.showMessageDialog(null, "No reservation selected to edit.");
            }
        }
    }

    public void updateReservationInFile(CustomerInfo updatedReservation) {
        String filePath = "ResList.txt"; // Update this path as necessary
        List<String> lines = new ArrayList<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Find the index of the line that matches the reservation to be updated
        int indexToUpdate = -1;
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(" ");
            // Assuming the first part of the line is the name, which is unique for each reservation
            if (parts[0].equals(updatedReservation.getName())) {
                indexToUpdate = i;
                break;
            }
        }
    
        if (indexToUpdate != -1) {
            String updatedLine = String.format("%s %s %s %s %s %s %s",
                                                updatedReservation.getName(),
                                                updatedReservation.getnNum(),
                                                updatedReservation.getMail(),
                                                updatedReservation.getRes(),
                                                updatedReservation.getDate(),
                                                updatedReservation.getTime());
            lines.set(indexToUpdate, updatedLine);
    
            // Write the updated lines back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Reservation not found in the file.");
        }
    }
}