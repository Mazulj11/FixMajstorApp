package com.example.fixmajstorapp.model;

public class UserProfile {
    private String imePrezime;
    private String email;
    private String adresa;
    private String lokacija;
    private Integer tel;
    

    public UserProfile(String imePrezime, String email, String adresa, String lokacija, Integer tel) {
        this.imePrezime = imePrezime;
        this.email = email;
        this.adresa = adresa ;
        this.lokacija = lokacija;
    }

    public String getImePrezime() {
        return imePrezime;
    }

    public void setImePrezime(String imePrezime) {
        this.imePrezime = imePrezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public Integer getTel() {
        return tel;
    }

    public void setTel(Integer tel) {
        this.tel = tel;
    }
}
