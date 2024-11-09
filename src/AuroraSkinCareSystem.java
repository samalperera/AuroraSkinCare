import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class AuroraSkinCareSystem {
    private List<Appointment> appointments = new ArrayList<>();
    private List<Dermatologist> dermatologists = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public AuroraSkinCareSystem() {
        initializeDermatologists();
        System.out.println("Initialized dermatologists:");
        for (Dermatologist d : dermatologists) {
            System.out.println("- " + d.getName());
        }
    }

    private void initializeDermatologists() {
        Dermatologist dermatologist1 = new Dermatologist("Dermatologist1");
        dermatologist1.addAvailability("Monday", "10:00", "13:00");
        dermatologist1.addAvailability("Wednesday", "14:00", "17:00");
        dermatologist1.addAvailability("Friday", "16:00", "20:00");
        dermatologist1.addAvailability("Saturday", "09:00", "13:00");

        Dermatologist dermatologist2 = new Dermatologist("Dermatologist2");
        dermatologist2.addAvailability("Monday", "10:00", "13:00");
        dermatologist2.addAvailability("Wednesday", "14:00", "17:00");
        dermatologist2.addAvailability("Friday", "16:00", "20:00");
        dermatologist2.addAvailability("Saturday", "09:00", "13:00");

        dermatologists.add(dermatologist1);
        dermatologists.add(dermatologist2);
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== Aurora Skin Care System ===");
            System.out.println("1. Book Appointment");
            System.out.println("2. View Appointments by Date");
            System.out.println("3. Search Appointment");
            System.out.println("4. Update Appointment");
            System.out.println("5. Generate Invoice");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> bookAppointment();
                case 2 -> viewAppointmentsByDate();
                case 3 -> searchAppointment();
                case 4 -> updateAppointment();
                case 5 -> generateInvoice();
                case 6 -> exit = true;
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void bookAppointment() {
        System.out.println("\n--- Book Appointment ---");
        System.out.print("Enter Patient NIC: ");
        String NIC = scanner.nextLine().trim();
        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Patient Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter Patient Phone: ");
        String phone = scanner.nextLine().trim();

        Patient patient = new Patient(NIC, name, email, phone);

        System.out.print("Choose Dermatologist (Dermatologist1 or Dermatologist2): ");
        String dermatologistName = scanner.nextLine().trim();

        Dermatologist selectedDermatologist = null;
        for (Dermatologist dermatologist : dermatologists) {
            if (dermatologist.getName().equalsIgnoreCase(dermatologistName)) {
                selectedDermatologist = dermatologist;
                break;
            }
        }

        if (selectedDermatologist == null) {
            System.out.println("Dermatologist not found. Please make sure you entered the name exactly as shown (Dermatologist1 or Dermatologist2).");
            return;
        }

        System.out.print("Enter Date (dd MM yyyy): ");
        String dateInput = scanner.nextLine().trim();
        LocalDate date;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
            date = LocalDate.parse(dateInput, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please enter the date in dd MM yyyy format.");
            return;
        }


        String dayOfWeek = date.getDayOfWeek().name();
        if (selectedDermatologist.getAvailableSlots(dayOfWeek).isEmpty()) {
            System.out.println("No available slots for the selected date.");
            return;
        }

        System.out.println("Available slots on " + date.format(DateTimeFormatter.ofPattern("dd MM yyyy")) + ":");
        for (LocalTime slot : selectedDermatologist.getAvailableSlots(dayOfWeek)) {
            System.out.println("- " + slot);
        }

        System.out.print("Enter desired time (HH:mm): ");
        String time = scanner.nextLine().trim();

        if (!selectedDermatologist.isSlotAvailable(dayOfWeek, time)) {
            System.out.println("The selected time is not available.");
            return;
        }

        // Accept registration fee
        double registrationFee = 500.00;
        System.out.println("A registration fee of LKR " + String.format("%.2f", registrationFee) + " is required to book this appointment.");
        System.out.print("Do you want to proceed with the payment? (yes/no): ");
        String paymentConfirmation = scanner.nextLine().trim().toLowerCase();

        if (!paymentConfirmation.equals("yes")) {
            System.out.println("Appointment booking cancelled.");
            return;
        }

        // Mark the appointment as paid and book the slot
        Appointment appointment = new Appointment(UUID.randomUUID().toString(), patient, selectedDermatologist, date, time);
        appointment.setPaid(true);
        appointments.add(appointment);
        selectedDermatologist.reserveSlot(dayOfWeek, time);

        System.out.println("Appointment booked successfully for " + date.format(DateTimeFormatter.ofPattern("dd MM yyyy")) + " at " + time + ". Appointment ID: " + appointment.getAppointmentID());
        System.out.println("Registration fee of LKR " + String.format("%.2f", registrationFee) + " has been paid.");
    }


    private void viewAppointmentsByDate() {
        System.out.println("\n--- View Appointments by Date ---");
        System.out.print("Enter Date (dd MM yyyy): ");
        String dateInput = scanner.nextLine().trim();
        LocalDate date;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
            date = LocalDate.parse(dateInput, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please enter the date in dd MM yyyy format.");
            return;
        }

        boolean found = false;
        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(date)) {
                System.out.println(appointment);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No appointments found for the specified date.");
        }
    }

    private void searchAppointment() {
        System.out.println("\n--- Search Appointment ---");
        System.out.print("Enter Patient Name or Appointment ID: ");
        String searchQuery = scanner.nextLine().trim();

        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getName().equalsIgnoreCase(searchQuery) || appointment.getAppointmentID().equalsIgnoreCase(searchQuery)) {
                System.out.println(appointment);
                return;
            }
        }
        System.out.println("No appointment found.");
    }

    private void updateAppointment() {
        System.out.println("\n--- Update Appointment ---");
        System.out.print("Enter Appointment ID to update: ");
        String appointmentID = scanner.nextLine().trim();
        // Search for the appointment by ID
        Appointment appointment = null;
        for (Appointment appt : appointments) {
            if (appt.getAppointmentID().equals(appointmentID)) {
                appointment = appt;
                break;
            }
        }

        if (appointment == null) {
            System.out.println("Appointment not found.");
            return;
        }

        System.out.println("Current Appointment Details:");
        System.out.println(appointment);

        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Change Date");
        System.out.println("2. Change Time");
        System.out.println("3. Change Dermatologist");
        System.out.println("4. Cancel Update");
        System.out.print("Choose an option (1-4): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("Enter new Date (dd MM yyyy): ");
                String newDateInput = scanner.nextLine().trim();
                LocalDate newDate;
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
                    newDate = LocalDate.parse(newDateInput, formatter);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Update failed.");
                    return;
                }
                appointment.setDate(newDate);
                System.out.println("Date updated successfully to " + newDate.format(DateTimeFormatter.ofPattern("dd MM yyyy")) + ".");
            }
            case 2 -> {
                System.out.print("Enter new Time (HH:mm): ");
                String newTime = scanner.nextLine().trim();
                if (!appointment.getDermatologist().isSlotAvailable(appointment.getDate().getDayOfWeek().name(), newTime)) {
                    System.out.println("The selected time is not available. Update failed.");
                    return;
                }
                appointment.setTime(newTime);
                appointment.getDermatologist().reserveSlot(appointment.getDate().getDayOfWeek().name(), newTime);
                System.out.println("Time updated successfully to " + newTime + ".");
            }
            case 3 -> {
                System.out.print("Enter new Dermatologist (Dermatologist1 or Dermatologist2): ");
                String newDermatologistName = scanner.nextLine().trim();
                Dermatologist newDermatologist = null;
                for (Dermatologist d : dermatologists) {
                    if (d.getName().equalsIgnoreCase(newDermatologistName)) {
                        newDermatologist = d;
                        break;
                    }
                }
                if (newDermatologist == null) {
                    System.out.println("Dermatologist not found. Update failed.");
                    return;
                }
                appointment.setDermatologist(newDermatologist);
                System.out.println("Dermatologist updated successfully to " + newDermatologist.getName() + ".");
            }
            case 4 -> {
                System.out.println("Update cancelled.");
                return;
            }
            default -> System.out.println("Invalid option. Update failed.");
        }
    }

    private void generateInvoice() {
        System.out.println("\n--- Generate Invoice ---");
        System.out.print("Enter Appointment ID: ");
        String appointmentID = scanner.nextLine().trim();

        Appointment appointment = null;
        for (Appointment appt : appointments) {
            if (appt.getAppointmentID().equals(appointmentID)) {
                appointment = appt;
                break;
            }
        }

        if (appointment == null) {
            System.out.println("Appointment not found.");
            return;
        }

        System.out.println("Choose Treatment:");
        System.out.println("1. Acne Treatment - LKR 2750.00");
        System.out.println("2. Skin Whitening - LKR 7650.00");
        System.out.println("3. Mole Removal - LKR 3850.00");
        System.out.println("4. Laser Treatment - LKR 12500.00");
        System.out.print("Enter Treatment Option (1-4): ");
        int treatmentOption = scanner.nextInt();
        scanner.nextLine();

        double treatmentCost;
        String treatmentName;

        switch (treatmentOption) {
            case 1 -> {
                treatmentName = "Acne Treatment";
                treatmentCost = 2750.00;
            }
            case 2 -> {
                treatmentName = "Skin Whitening";
                treatmentCost = 7650.00;
            }
            case 3 -> {
                treatmentName = "Mole Removal";
                treatmentCost = 3850.00;
            }
            case 4 -> {
                treatmentName = "Laser Treatment";
                treatmentCost = 12500.00;
            }
            default -> {
                System.out.println("Invalid option. Invoice generation failed.");
                return;
            }
        }

        double taxRate = 0.025;
        double taxAmount = treatmentCost * taxRate;
        double totalAmount = treatmentCost + taxAmount;

        System.out.println("\n--- Invoice ---");
        System.out.println("Appointment ID: " + appointment.getAppointmentID());
        System.out.println("Patient Name: " + appointment.getPatient().getName());
        System.out.println("Dermatologist: " + appointment.getDermatologist().getName());
        System.out.println("Date: " + appointment.getDate().format(DateTimeFormatter.ofPattern("dd MM yyyy")));
        System.out.println("Time: " + appointment.getTime());
        System.out.println("Treatment: " + treatmentName);
        System.out.println("Treatment Cost: LKR " + String.format("%.2f", treatmentCost));
        System.out.println("Tax (2.5%): LKR " + String.format("%.2f", taxAmount));
        System.out.println("Total Amount: LKR " + String.format("%.2f", totalAmount));
    }

    public static void main(String[] args) {
        AuroraSkinCareSystem system = new AuroraSkinCareSystem();
        system.run();
    }
}
