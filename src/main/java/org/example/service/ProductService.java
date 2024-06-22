package org.example.service;

import org.example.map.dto.ProductDto;
import org.example.map.request.newRequest.NewProductRequest;
import org.example.map.request.updateRequest.UpdateProductRequest;

import java.util.List;

public interface ProductService {
    Long create(NewProductRequest request);
    ProductDto update(UpdateProductRequest request);
    ProductDto read(Long id);
    List<ProductDto> readAll();
    void delete(Long id);
}
