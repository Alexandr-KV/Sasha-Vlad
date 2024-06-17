package ru.otus;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) {

        String jsonString = "{\n" +
                "  \"age\": 21,\n" +
                "  \"name\": \"vlad\",\n" +
                "  \"height\": 190.2,\n" +
                "  \"animal\": {\n" +
                "    \"name\": \"zig-zag\",\n" +
                "    \"age\": 9\n" +
                "  }\n" +
                "}\n";

        try {
            Person person = new ObjectMapper().readValue(jsonString, Person.class);
            person.setAge(22);
            Animal animal = person.getAnimal();
            animal.setAge(10);
            person.setAnimal(animal);
            jsonString = new ObjectMapper().writeValueAsString(person);
            System.out.println(jsonString);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}