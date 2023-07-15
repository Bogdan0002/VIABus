package com.viabus.files;

public class FilePath {
    private String bussesFilePath = "/files/Busses.txt";
    private String chauffeursFilePath = "files/Chauffeurs.txt";
    private String customersFilePath = "files/Customers.txt";
    private String reservationsFilePath = "files/Reservations.txt";

    public FilePath() {
    }

    public String getBussesFilePath() {
        return this.bussesFilePath;
    }

    public String getChauffeursFilePath() { return this.chauffeursFilePath;
    }

    public String getCustomersFilePath() { return this.customersFilePath;
    }

    public String getReservationsFilePath() {
        return this.reservationsFilePath;
    }
}
