package org.example.map.request.newRequest;

import java.util.Objects;

public class NewRoleRequest {
    private String name;

    public NewRoleRequest() {
    }

    public NewRoleRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewRoleRequest that = (NewRoleRequest) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
