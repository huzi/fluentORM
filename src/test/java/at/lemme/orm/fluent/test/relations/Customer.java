package at.lemme.orm.fluent.test.relations;

import at.lemme.orm.fluent.api.annotation.OneToMany;

import java.util.List;

/**
 * Created by thomas18 on 02.02.2017.
 */
public class Customer {
    private int id;
    private String name;

    @OneToMany(mappedBy = "customer")
    private List<TodoList> lists;

    public Customer() {
    }

    public Customer(int id, String name, List<TodoList> lists) {
        this.id = id;
        this.name = name;
        this.lists = lists;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TodoList> getLists() {
        return lists;
    }

    public void setLists(List<TodoList> lists) {
        this.lists = lists;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lists=" + lists +
                '}';
    }
}
