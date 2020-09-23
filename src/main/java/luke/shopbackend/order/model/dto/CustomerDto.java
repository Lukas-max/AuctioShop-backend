package luke.shopbackend.order.model.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class CustomerDto {

    @NotEmpty(message = "Pole imię nie może zostać puste.")
    private String firstName;
    @NotEmpty(message = "Pole nazwisko nie może być puste.")
    private String lastName;
    private long telephone;
    @NotEmpty(message = "Pole email nie może być puste")
    @Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$", message = "Musisz podać poprawny email.")
    private String email;
    private String country;
    @Min(value = 1, message = "Numer domu to przynajmniej 1.")
    private int houseNumber;
    private int apartmentNumber;
    @NotEmpty(message = "Musisz podać kod pocztowy.")
    private String postalCode;
    @NotEmpty(message = "Musisz podać miasto.")
    private String city;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getTelephone() {
        return telephone;
    }

    public void setTelephone(long telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(int apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "CustomerDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", telephone=" + telephone +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", houseNumber=" + houseNumber +
                ", apartmentNumber=" + apartmentNumber +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
