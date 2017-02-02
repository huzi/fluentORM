package at.lemme.orm.fluent.test.relations;

import at.lemme.orm.fluent.api.annotation.ManyToOne;

/**
 * Created by thomas18 on 02.02.2017.
 */
public class Item {
    private int id;
    private String title;
    private String description;

    @ManyToOne(column = "todoListId")
    private TodoList todoList;

    public Item() {
    }

    public Item(int id, String title, String description, TodoList todoList) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.todoList = todoList;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TodoList getTodoList() {
        return todoList;
    }

    public void setTodoList(TodoList todoList) {
        this.todoList = todoList;
    }
}
