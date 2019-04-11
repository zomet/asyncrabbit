package fr.zomet.tuto.dto;

public class UserDto {

    public int id;

    public String fullName;

    @Override
    public String toString() {
        return "id: " + id + ", fullName: " + fullName;
    }
}
