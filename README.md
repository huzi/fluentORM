# fluentORM


fluent.select().from(Person.class).list();
fluent.select().from(Person.class).where(Conditions.equal("id","abc")).uniqueResult();
fluent.select().from(Person.class).where(Conditions.lowerThan("bithDate",dateTime)).list();
fluent.select().from(Person.class).withFetch().list();
fluent.select().from(Person.class).withFetch("address").list();
fluent.select().from(Person.class).alias(p);
fluent.select().from(Person.class).alias(p).join(Entity.class).alias(e).on("p.id = e.fk").list(Person.class);


fluent.insert().byObject(person).execute();
fluent.insert().byObject(person).withCascade().execute();
fluent.insert().byObject(person).withCascade("address").execute();


fluent.update().byObject(person).execute();
fluent.update().byObject(person).withCascade().execute();
fluent.update().byObject(person).withCascade("address").execute();


fluent.delete().from(Person.class).byId("abc").execute();
fluent.delete().from(Person.class).withCascade().execute();
fluent.delete().from(Person.class).withCascade("address").execute();
fluent.delete().from(Person.class).byObject(person).execute();
fluent.delete().from(Person.class).where(Conditions.lowerThan("bithDate",dateTime)).execute();



