package com.one.digitalapi.dto;

public class UserDTO {

    private Long id;
    private String userId;

    public UserDTO(Long id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    public UserDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
