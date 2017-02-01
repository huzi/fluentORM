# fluentORM

## Roadmap

### Version 0.1

Simple CRUD: 
* select + where, order, limit
* delete where
* update object/list
* insert object/list


###Version 0.2

* Annotations: rename table & attributes, set id
* Get id via Metadata class
* Add new datatypes: long, short, float, double


###Version 0.3

* Add new datatypes: BigDecimal, Byte, Boolean
* select().byId()
* Id generation done by Fluent
* Support H2, MySQL, PostgreSQL


### Backlog

* Tests for illegal arguments (Conditions, crud etc.)
* Id Generation: done by DBMS, by fluent
* Relations
* SQL functions: format date, count, sum...
* DBMS support: H2, MySQL, Postgres, Oracle, SQL Server
* Fluent SQL Support (projections, functions, joins)
* SQL named parameters support?
* Automatic DB schema generation
* Aggregations, Grouping ?
* select().byId() 
* Support Datatypes: https://www.tutorialspoint.com/hibernate/hibernate_mapping_types.htm
  * All simple types
  * All Date & Time types
  * Blobs
  * Streaming of Blobs
* XML/Json Configuration

## select

    fluent.select().from(Person.class).list();
    fluent.select().from(Person.class).where(Conditions.equal("id","abc")).uniqueResult();
    fluent.select().from(Person.class).where(Conditions.lowerThan("bithDate",dateTime)).list();
    fluent.select().from(Person.class).withFetch().list();
    fluent.select().from(Person.class).withFetch("address").list();
    fluent.select().from(Person.class).alias(p);
    fluent.select().from(Person.class).alias(p).join(Entity.class).alias(e).on("p.id = e.fk").list(Person.class);
    
### @ManyToOne
   
    fluent.select(University.class).withFetch("students").list();
    SELECT * FROM University u JOIN Student s ON(u.id = s.university_id);

    fluent.select(University.class)
        .withFetch("students")
        .withFetch("students.exams").list();
    SELECT * FROM University u 
        JOIN Student s ON(u.id = s.university_id)
        JOIN Certificate c ON(s.id = c.student_id);
    
    
## insert

    fluent.insert().byObject(person).execute();
    fluent.insert().byObject(person).withCascade().execute();
    fluent.insert().byObject(person).withCascade("address").execute();

## update

    fluent.update().byObject(person).execute();
    fluent.update().byObject(person).withCascade().execute();
    fluent.update().byObject(person).withCascade("address").execute();

## delete

    fluent.delete().from(Person.class).byId("abc").execute();
    fluent.delete().from(Person.class).withCascade().execute();
    fluent.delete().from(Person.class).withCascade("address").execute();
    fluent.delete().from(Person.class).byObject(person).execute();
    fluent.delete().from(Person.class).where(Conditions.lowerThan("bithDate",dateTime)).execute();


## Predicates
* Comparison Predicate
* Between
* In
* Like
* Is Null
* Is Not Null
* Not supported: Quantified, Subselects, Exists, Match, Overlaps

## Comparison Operators:
* Equals
* Not Equals
* Greater Than
* Greater Than Or Equals
* Less Than
* Less Than Or Equals





