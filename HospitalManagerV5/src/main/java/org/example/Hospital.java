package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Hospital{
    private String name;
    private String password;
    private double balance;
    private int num_doctors;
    private int num_patients;
    private ArrayList<Patient> patients = new ArrayList<>();
    private ArrayList<Doctor> OPDdoctors = new ArrayList<>();
    private ArrayList<Drug> inventory = new ArrayList<>();


    public Hospital(String name, String password, double balance) {
        this.name = name;
        this.password = password;
        this.balance = balance;
        num_patients = 0;
        num_doctors = 0;
    }
    public Hospital(String name, String password){
        this.name = name;
        this.password = password;
        this.balance = 0;
        num_patients = 0;
        num_doctors = 0;
    }

    public Hospital(){}

    public String getName() {
        return name;
    }


    public double getBalance() {
        return balance;
    }

    public void changePassword(String newPassword) {
        setPassword(newPassword);
        String credentialsFileName = name + "-credentials.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(credentialsFileName));
            String[] lines = new String[5];
            for (int i = 0; i < 5; i++) {
                lines[i] = reader.readLine();
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(credentialsFileName));
            writer.write(lines[0]);
            writer.write("\n" + newPassword);
            writer.write("\n" + lines[2]);
            writer.write("\n" + lines[3]);
            writer.write("\n" + lines[4]);
            writer.close();

            System.out.println("Password changed successfully!");
        } catch (IOException e) {
            System.out.println("Failed to change password: " + e.getMessage());
        }
    }

    public void setPatients(Patient patient) {
        String fileName = name + "-patients.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            String line = patient.name + "," + patient.age + "," + patient.gender + "," + patient.disease;
            writer.write(line);
            writer.newLine();
            writer.close();
            num_patients++;
            updateCredentialsFile();
        } catch (IOException e) {
            System.out.println("Failed to write patient to file: " + e.getMessage());
        }

        patients.add(patient);
    }

    public void setOPDdoctors(Doctor doctor) {
        String fileName = name + "-doctors.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            String line = doctor.name + "," + doctor.age + "," + doctor.gender + "," +
                    doctor.speciality + "," + doctor.fee;
            writer.write(line);
            writer.newLine();
            writer.close();
            num_doctors++;
            updateCredentialsFile();
        } catch (IOException e) {
            System.out.println("Failed to write doctor to file: " + e.getMessage());
        }

        OPDdoctors.add(doctor);
    }
    private void updateCredentialsFile() {
        String credentialsFileName = name + "-credentials.txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(credentialsFileName));
            String[] lines = new String[5];
            for (int i = 0; i < 5; i++) {
                lines[i] = reader.readLine();
            }
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(credentialsFileName));
            writer.write(lines[0]);
            writer.write("\n" + lines[1]);
            writer.write("\n" + lines[2]);
            writer.write("\n" + num_doctors);
            writer.write("\n" + num_patients);
            writer.close();

            System.out.println("Credentials file updated successfully!");
        } catch (IOException e) {
            System.out.println("Failed to update credentials file: " + e.getMessage());
        }
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void print_doctors() {
        for (Doctor doctor : OPDdoctors) {
            System.out.println("Doctor's name: " + doctor.getName());
            System.out.println("Age: " + doctor.getAge());
            System.out.println("Gender: " + doctor.getGender());
            System.out.println("Speciality: " + doctor.getSpeciality());
            System.out.println("Charges per visit: " + doctor.getFee());
            System.out.println();
        }
    }

    public void print_patients() {
        for (Patient patient : patients) {
            System.out.println("Patient's name: " + patient.getName());
            System.out.println("Age: " + patient.getAge());
            System.out.println("Gender: " + patient.getGender());
            System.out.println("Disease: " + patient.getDisease());
            System.out.println();
        }

    }

    public void printInventory() {
        for (Drug drug : inventory) {
            System.out.println(drug.getName() + " : " + drug.getQuantity() + " : $" + drug.price);
        }
    }

    public boolean SignUp() {
        File patients_file = new File(name + "-patients.txt");
        File doctors_file = new File(name + "-doctors.txt");
        File drug_file = new File(name + "-inventory.txt");
        File credentials_file = new File(name + "-credentials.txt");
        try {
            if (patients_file.createNewFile() && doctors_file.createNewFile() && drug_file.createNewFile() && credentials_file.createNewFile()) {
                BufferedWriter writer= new BufferedWriter(new FileWriter(credentials_file));
                writer.write(name);
                writer.write("\n" + password);
                writer.write("\n" + balance);
                writer.write("\n" + num_doctors);
                writer.write("\n" + num_patients);
                writer.close();
                System.out.println("Account created successfully.");
                return true;
            } else {
                System.out.println("Account already exists!");
                return false;
            }
        } catch (IOException e) {
            System.out.println("Failed! Some error occurred.");
            return false;
        }

    }
    public boolean signIn() {
        String credentialsFileName = this.name + "-credentials.txt";
        File credentialsFile = new File(credentialsFileName);

        if (credentialsFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(credentialsFile));
                String storedName = reader.readLine();
                String storedPassword = reader.readLine();

                if (this.password.equals(storedPassword)) {
                    this.setBalance(Double.parseDouble(reader.readLine()));
                    this.num_doctors = Integer.parseInt(reader.readLine());
                    this.num_patients = Integer.parseInt(reader.readLine());

                    readDoctorsFromFile();
                    readPatientsFromFile();
                    readInventoryFromFile();

                    System.out.println("Signed in successfully!");
                    reader.close();
                    return true;
                } else {
                    System.out.println("Incorrect hospital name or password.");
                }
                reader.close();
            } catch (IOException e) {
                System.out.println("Failed to read credentials from file: " + e.getMessage());
            }
        } else {
            System.out.println("Hospital does not exist.");
        }

        return false;
    }

    public void readPatientsFromFile() {
        String fileName = name + "-patients.txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                int age = Integer.parseInt(data[1]);
                String gender = data[2];
                String disease = data[3];
                Patient patient = new Patient(name, age, gender, disease);
                patients.add(patient);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Failed to read patients from file: " + e.getMessage());
        }
    }

    public void readDoctorsFromFile() {
        String fileName = name + "-doctors.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                int age = Integer.parseInt(data[1]);
                String gender = data[2];
                String speciality = data[3];
                double fee = Double.parseDouble(data[4]);

                Doctor doctor = new Doctor(name, age, gender, speciality, fee);
                OPDdoctors.add(doctor);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Failed to read doctors from file: " + e.getMessage());
        }
    }


    public void readInventoryFromFile() {
        String fileName = name + "-inventory.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                int quantity = Integer.parseInt(data[1]);
                double price = Double.parseDouble(data[2]);

                Drug drug = new Drug(name, quantity, price);
                inventory.add(drug);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Failed to read inventory from file: " + e.getMessage());
        }
    }

    public void removeZeroQuantityDrugsFromFile() {
        try {
            String fileName = name + "-inventory.txt";
            File file = new File(fileName);

            if (file.length() == 0) {
                return;
            }

            ArrayList<Drug> updatedInventory = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String drugName = parts[0];
                int quantity = Integer.parseInt(parts[1]);
                double price = Double.parseDouble(parts[2]);

                if (quantity > 0) {
                    Drug drug = new Drug(drugName, quantity, price);
                    updatedInventory.add(drug);
                }
            }

            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
            for (Drug drug : updatedInventory) {
                String lineToAdd = drug.getName() + "," + drug.getQuantity() + "," + drug.getPrice();
                writer.write(lineToAdd);
                writer.newLine();
            }
            writer.close();

            inventory = updatedInventory;
        } catch (IOException e) {
            System.out.println("Failed to remove zero quantity drugs from inventory: " + e.getMessage());
        }
    }

    public boolean checkDrug(String drugName, int requiredNum) {
        removeZeroQuantityDrugsFromFile();
        for (Drug drug : inventory) {
            if (drug.getName().equalsIgnoreCase(drugName)) {
                if (requiredNum <= drug.getQuantity()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    public double getCost(int quantity, double price){
        return price*quantity;
    }

    public void buy(String drugName, int quantity, double totalCost, double price) {
        try {
            setBalance(getBalance() - totalCost);
            String credentialsFileName = name + "-credentials.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(credentialsFileName));
            writer.write(name);
            writer.newLine();
            writer.write(password);
            writer.newLine();
            writer.write(String.valueOf(balance)); // Updated balance
            writer.newLine();
            writer.write(String.valueOf(num_doctors)); // Num doctors
            writer.newLine();
            writer.write(String.valueOf(num_patients)); // Num patients
            writer.close();

            boolean drugExists = false;
            for (Drug drug : inventory) {
                if (drug.getName().equalsIgnoreCase(drugName)) {
                    drug.setQuantity(drug.getQuantity() + quantity);
                    drugExists = true;
                    break;
                }
            }

            if (!drugExists) {
                Drug newDrug = new Drug(drugName, quantity, price);
                inventory.add(newDrug);
            }

            String inventoryFileName = name + "-inventory.txt";
            BufferedWriter inventoryWriter = new BufferedWriter(new FileWriter(inventoryFileName, true));
            inventoryWriter.write(drugName + "," + quantity + "," + price);
            inventoryWriter.newLine();
            inventoryWriter.close();

            System.out.println("Purchase successful!");

        } catch (IOException e) {
            System.out.println("Failed to complete the purchase: " + e.getMessage());
        }
    }

    public int generateRequiredNum(){
        Random random = new Random();
        int numRequired = random.nextInt(5) + 1;
        return numRequired;
    }
    public void removeQuantityFromInventory(String drugName, int quantity) {
        try {
            File file = new File(name + "-inventory.txt");
            if (!file.exists() || file.length() == 0) {
                System.out.println("Inventory is empty.");
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            ArrayList<Drug> updatedInventory = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0].trim();
                int existingQuantity = Integer.parseInt(parts[1].trim());
                double price = Double.parseDouble(parts[2].trim());

                if (name.equalsIgnoreCase(drugName)) {
                    int remainingQuantity = existingQuantity - quantity;
                    if (remainingQuantity >= 0) {
                        Drug updatedDrug = new Drug(name, remainingQuantity, price);
                        updatedInventory.add(updatedDrug);
                    } else {
                        System.out.println("Insufficient quantity of drug: " + drugName);
                    }
                } else {
                    Drug drug = new Drug(name, existingQuantity, price);
                    updatedInventory.add(drug);
                }
            }

            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (Drug drug : updatedInventory) {
                writer.write(drug.getName() + "," + drug.getQuantity() + "," + drug.getPrice());
                writer.newLine();
            }
            writer.close();

            inventory = updatedInventory;

        } catch (IOException e) {
            System.out.println("Failed to remove quantity from inventory: " + e.getMessage());
        }
    }


    public boolean getDrugs(String disease){
        OpenFDA openfda = new OpenFDA();
        ArrayList<String> drugs = openfda.getDrugsForDisease(disease);
        Scanner input = new Scanner(System.in);
        for(String drug : drugs){
            int numRequired = generateRequiredNum();
            Random random = new Random();
            double price = 5 + random.nextDouble() * 5;
            double cost = numRequired * price;
            Drug requiredDrug = new Drug(drug, numRequired, price);
            boolean isAvailable = checkDrug(drug, numRequired);
            System.out.println("\n");
            if (isAvailable){
                System.out.println("Required number of the " + drug +" available.\n" + numRequired + " dozes administered.");
                removeQuantityFromInventory(drug, numRequired);
                System.out.println("\n");
            }
            else{
                System.out.println(drug + " Not enough available in the inventory\nRequired number : " + numRequired + "\n" + "Cost per unit" + price);
                System.out.print("Would you like to buy the drugs? Y/N : ");
                String response = input.next();
                if (response.equalsIgnoreCase("Y")){
                    System.out.print("Number you want to buy : ");
                    int buyNum = input.nextInt();
                    double totalCost = getCost(buyNum , price);
                    System.out.print("Total cost is : " + totalCost + "\nPlease confirm to proceed. Y/N : ");
                    String confirmation = input.next();
                    if (confirmation.equalsIgnoreCase("Y")){
                        buy(drug, buyNum, totalCost, price);
                        System.out.println("Transaction successful.");
                        System.out.println("Your balance is now : " + getBalance());
                        removeQuantityFromInventory(drug, numRequired);
                        System.out.println("\n");
                    }
                    else {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
        }
        return true;
    }
    public void newPatient(String patientName, int age, String gender, String disease){
        Patient patient = new Patient(patientName, age, gender, disease);
        boolean status = getDrugs(disease);
        if (status){
            System.out.println("Patient admitted.");
            setPatients(patient);
        }
        else {
            System.out.println("Patient was sent back.");
        }
    }
    public void printDoctorOfSpeciality(String speciality){
        for (Doctor doctor : OPDdoctors){
            if ((doctor.speciality).equalsIgnoreCase(speciality)){
                System.out.println(doctor.name + " : " + doctor.fee);
            }
        }
    }
}



