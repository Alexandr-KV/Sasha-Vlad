package ru.otus;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Person {
    private int age;
    private String name;
    private double height;
    private Animal animal;

    public Person() {
    }

    public Person(int age, String name, double height, Animal animal) {
        this.age = age;
        this.name = name;
        this.height = height;
        this.animal = animal;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", height=" + height +
                ", animal=" + animal +
                '}';
    }

}
