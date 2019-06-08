package com.example.abdul.pieasdirectory;

import java.io.Serializable;
import java.util.HashMap;

public class Person implements Serializable {

    private HashMap<String, String> personData = new HashMap<>();
    public static final String[] PERSON_KEYS = {"personName", "designation", "post", "department", "officeLocation", "extension", "phoneNo", "cellNo", "email"};

    Person(String[] values) {
        this(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);
    }

    private Person(String personName, String designation, String post, String department, String officeLocation, String extension, String phoneNo, String cellNo, String emai) {
        setPersonData(PERSON_KEYS[0], personName);
        setPersonData(PERSON_KEYS[1], designation);
        setPersonData(PERSON_KEYS[2], post);
        setPersonData(PERSON_KEYS[3], department);
        setPersonData(PERSON_KEYS[4], officeLocation);
        setPersonData(PERSON_KEYS[5], extension);
        setPersonData(PERSON_KEYS[6], phoneNo);
        setPersonData(PERSON_KEYS[7], cellNo);
        setPersonData(PERSON_KEYS[8], emai);
    }

    public String getPersonData(String key) {
        return personData.get(key);
    }

    public void setPersonData(String key, String value) {
        personData.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : PERSON_KEYS) {
            stringBuilder.append(key).append(" -> ").append(personData.get(key)).append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object personObject) {
        if (personObject instanceof Person) {
            Person person = (Person) personObject;
            return this.personData.get("extension").equalsIgnoreCase(person.personData.get("extension")) ||
                    this.personData.get("phoneNo").equalsIgnoreCase(person.personData.get("phoneNo")) ||
                    this.personData.get("cellNo").equalsIgnoreCase(person.personData.get("cellNo")) ||
                    this.personData.get("email").equalsIgnoreCase(person.personData.get("email"));
        }
        return false;
    }

    // Static Methods
    public static String parsePersonToString(Person person) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("'");
        for (String key : PERSON_KEYS) {
            if (!key.equals("email")) {
                stringBuilder.append(person.personData.get(key)).append("', '");
            } else {
                stringBuilder.append(person.personData.get(key)).append("'");
            }
        }
        return stringBuilder.toString();
    }

}
