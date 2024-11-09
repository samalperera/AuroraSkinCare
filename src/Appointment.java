import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private String appointmentID;
    private Patient patient;
    private Dermatologist dermatologist;
    private LocalDate date;
    private String time;
    private boolean isPaid;

    // Constructor
    public Appointment(String appointmentID, Patient patient, Dermatologist dermatologist, LocalDate date, String time) {
        this.appointmentID = appointmentID;
        this.patient = patient;
        this.dermatologist = dermatologist;
        this.date = date;
        this.time = time;
        this.isPaid = false;
    }

    // Getters
    public String getAppointmentID() {
        return appointmentID;
    }

    public Patient getPatient() {
        return patient;
    }

    public Dermatologist getDermatologist() {
        return dermatologist;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public boolean isPaid() {
        return isPaid;
    }

    // Setters
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDermatologist(Dermatologist dermatologist) {
        this.dermatologist = dermatologist;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
        return "Appointment ID: " + appointmentID +
                ", Patient: " + patient.getName() +
                ", Dermatologist: " + dermatologist.getName() +
                ", Date: " + date.format(formatter) + // Format LocalDate to String
                ", Time: " + time +
                ", Paid: " + (isPaid ? "Yes" : "No");
    }
}
