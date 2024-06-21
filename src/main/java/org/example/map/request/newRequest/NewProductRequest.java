package org.example.map.request.newRequest;

import java.util.Objects;

public class NewProductRequest {
    private String name;
    private Long quantity;
    private Long numberOfSold;
    protected Double price;

    public NewProductRequest() {
    }

    public NewProductRequest(String name, Long quantity, Long numberOfSold, Double price) {
        this.name = name;
        this.quantity = quantity;
        this.numberOfSold = numberOfSold;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getNumberOfSold() {
        return numberOfSold;
    }

    public void setNumberOfSold(Long numberOfSold) {
        this.numberOfSold = numberOfSold;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewProductRequest that = (NewProductRequest) o;
        return Objects.equals(name, that.name) && Objects.equals(quantity, that.quantity) && Objects.equals(numberOfSold, that.numberOfSold) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity, numberOfSold, price);
    }
}
