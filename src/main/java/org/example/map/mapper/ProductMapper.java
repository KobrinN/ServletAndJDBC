package org.example.map.mapper;

import org.example.map.dto.ProductResponse;
import org.example.map.request.ProductRequest;
import org.example.model.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {
    ProductResponse toResponse(Product product);

    Product toModel(ProductRequest request);

}
