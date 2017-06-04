package at.lemme.orm.fluent.test.relations.model;

import at.lemme.orm.fluent.api.annotation.Column;
import at.lemme.orm.fluent.api.annotation.ManyToOne;
import at.lemme.orm.fluent.api.annotation.OneToMany;
import at.lemme.orm.fluent.test.relations.model.Item;

import java.util.List;

/**
 * Created by thomas18 on 02.02.2017.
 */
public class TodoList {
    private int id;

    @Column(name = "name")
    private String title;

    @ManyToOne(column = "customerId")
    private Customer customer;

    @OneToMany(mappedBy = "todoList")
    private List<Item> items;

    public TodoList() {
    }

    public TodoList(int id, String title, Customer customer, List<Item> items) {
        this.id = id;
        this.title = title;
        this.customer = customer;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "TodoList{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", customer=" + (customer == null ? "null" : customer.getId()) +
                ", items=" + items +
                '}';
    }
}
