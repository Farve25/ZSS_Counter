package model;

public class Person {
    String name;
    String cardId;
    String group;

    Person (String cardId, String name, String group){
        this.cardId = cardId;
        this.group = group;
        this.name = name;
    }

    Person (String cardId){
        this.cardId = cardId;
    }


}
