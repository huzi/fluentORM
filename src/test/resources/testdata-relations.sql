CREATE TABLE Customer(
    id INTEGER PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE TodoList(
    id INTEGER PRIMARY KEY,
    customerId INTEGER,
    name VARCHAR(255)
);

CREATE TABLE Item(
    id INTEGER PRIMARY KEY,
    todoListId INTEGER,
    title VARCHAR(255),
    description VARCHAR(255)
);
