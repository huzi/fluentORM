package at.lemme.fluent.orm.metadata.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by thomas on 04.12.16.
 */
@Entity
public class Address {

    @Id
    String addressId;
    String street;
    String city;
    Integer zipCode;

    public Address() {
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }
}
