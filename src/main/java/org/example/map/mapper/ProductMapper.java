package org.example.map.mapper;

import org.example.map.dto.ProductDto;
import org.example.map.request.newRequest.NewProductRequest;
import org.example.map.request.updateRequest.UpdateProductRequest;
import org.example.model.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {
    ProductDto toDto(Product product);
    Product toModel(ProductDto productDto);

    NewProductRequest toNewRequest(Product product);
    Product toModel(NewProductRequest request);

    UpdateProductRequest toUpdateRequest(Product product);
    Product toModel(UpdateProductRequest request);

}
