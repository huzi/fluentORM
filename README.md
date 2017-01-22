# fluentORM

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





