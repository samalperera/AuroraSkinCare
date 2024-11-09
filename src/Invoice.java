import java.util.UUID;

class Invoice {
    private String invoiceID;
    private Appointment appointment;
    private Treatment treatment;
    private double totalAmount;

    public Invoice(Appointment appointment, Treatment treatment) {
        this.invoiceID = UUID.randomUUID().toString();
        this.appointment = appointment;
        this.treatment = treatment;
        this.totalAmount = calculateTotalWithTax();
    }

    private double calculateTotalWithTax() {
        double baseAmount = treatment.getPrice();
        double tax = baseAmount * 0.025;
        return Math.round((baseAmount + tax) * 100.0) / 100.0;
    }

    public void generateInvoice() {
        System.out.println("Invoice ID: " + invoiceID);
        System.out.println("Patient: " + appointment.getPatient().getName());
        System.out.println("Treatment: " + treatment);
        System.out.println("Total Amount (with tax): LKR " + totalAmount);
    }
}
