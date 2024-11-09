enum Treatment {
    ACNE(2750.0),
    SKIN_WHITENING(7650.0),
    MOLE_REMOVAL(3850.0),
    LASER(12500.0);

    private final double price;

    Treatment(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
