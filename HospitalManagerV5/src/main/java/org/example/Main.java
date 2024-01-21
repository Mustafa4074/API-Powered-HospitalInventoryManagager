package org.example;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String quit_app;
            System.out.print("Sign up(U) or Sign in(I) : ");
            String response = scanner.nextLine();
            System.out.print("Enter Hospital name : ");
            String hospitalName = scanner.nextLine();
            System.out.print("Enter password : ");
            String password = scanner.nextLine();
            Hospital hospital = null;
            boolean signedIn = false;

            if (response.equalsIgnoreCase("I")) {
                hospital = new Hospital(hospitalName, password);
                signedIn = hospital.signIn();

            } else if (response.equalsIgnoreCase("U")) {
                System.out.println("Enter the balance amount : ");
                double balance = scanner.nextDouble();
                hospital = new Hospital(hospitalName, password, balance);
                hospital.SignUp();
                signedIn = true;
            }
            int action = 0;
            if (signedIn) {
                boolean run = true;
                while (run) {
                    System.out.println("1 - Change Password");
                    System.out.println("2 - Add a doctor");
                    System.out.println("3 - Admit a patient");
                    System.out.println("4 - display Doctors");
                    System.out.println("5 - display Patients");
                    System.out.println("6 - display Drugs in inventory");
                    System.out.println("7 - Sign Out");
                    System.out.print("Action : ");
                    action = scanner.nextInt();
                    scanner.nextLine();
                    switch (action) {
                        case (1):
                            System.out.print("New Password");
                            String newPass = scanner.next();
                            hospital.changePassword(newPass);
                            continue;
                        case (2):
                            System.out.print("Doctor's name : ");
                            String docName = scanner.nextLine();
                            System.out.print("Age : ");
                            int age = Integer.parseInt(scanner.nextLine());
                            System.out.print("Gender : ");
                            String gender = scanner.nextLine();
                            System.out.print("Speciality : ");
                            String speciality = scanner.nextLine();
                            System.out.print("Charges : ");
                            double fees = scanner.nextDouble();
                            Doctor doctor = new Doctor(docName, age, gender, speciality, fees);
                            hospital.setOPDdoctors(doctor);
                            continue;
                        case (3):

                            System.out.print("Enter Patient's name : ");
                            String patName = scanner.nextLine();
                            System.out.print("Enter patient's age : ");
                            int patAge = scanner.nextInt();
                            System.out.print("Enter patient's gender : ");
                            String gen = scanner.nextLine();
                            scanner.nextLine();
                            System.out.print("Enter patient's disease : ");
                            String disease = scanner.nextLine();
                            hospital.newPatient(patName, patAge, gen, disease);
                            continue;
                        case (4):
                            hospital.print_doctors();
                            continue;
                        case (5):
                            hospital.print_patients();
                            continue;
                        case (6):
                            hospital.printInventory();
                            continue;
                        case (7):
                            run = false;
                            break;
                    }
                }
            }
            System.out.println("Do you want to quit the app? Y/N");
            quit_app = scanner.nextLine();
            if (quit_app.equalsIgnoreCase("Y")){
                break;
            }

        }
    }
}
