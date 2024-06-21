package org.example.map.request.updateRequest;

import java.util.Objects;

public class UpdateProductRequest {
    private Long id;
    private String name;
    private Long quantity;
    private Long numberOfSold;
    protected Double price;

    public UpdateProductRequest() {
    }

    public UpdateProductRequest(Long id, String name, Long quantity, Long numberOfSold, Double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.numberOfSold = numberOfSold;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        UpdateProductRequest that = (UpdateProductRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(quantity, that.quantity) && Objects.equals(numberOfSold, that.numberOfSold) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, quantity, numberOfSold, price);
    }
}
