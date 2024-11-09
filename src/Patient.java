class Patient {
    private String NIC;
    private String name;
    private String email;
    private String phone;

    public Patient(String NIC, String name, String email, String phone) {
        this.NIC = NIC;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getName() { return name; }
    public String getNIC() { return NIC; }

}

