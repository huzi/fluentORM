package at.lemme.orm.fluent.test.relations.model;

import at.lemme.orm.fluent.api.annotation.ManyToOne;

/**
 * Created by thomas18 on 02.02.2017.
 */
public class Address {
    private String id;
    private String street;
    private String city;
    private String country;

    @ManyToOne(column = "customerId")
    private Customer customer;

    public Address() {
    }

    public Address(String id, String street, String city, String country, Customer customer) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.country = country;
        this.customer = customer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", customer=" + (customer == null ? null : customer.getId()) +
                '}';
    }
}
