package org.example.service.impl;

import org.example.exception.BadRequestException;
import org.example.map.dto.ProductDto;
import org.example.map.mapper.ProductMapper;
import org.example.map.mapper.ProductMapperImpl;
import org.example.map.request.newRequest.NewProductRequest;
import org.example.map.request.updateRequest.UpdateProductRequest;
import org.example.model.Product;
import org.example.repo.ProductRepo;
import org.example.repo.UserRepo;
import org.example.repo.impl.ProductRepoImpl;
import org.example.repo.impl.UserRepoImpl;
import org.example.service.ProductService;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final ProductMapper productMapper = new ProductMapperImpl();
    public ProductServiceImpl(){
        userRepo = UserRepoImpl.getInstance();
        productRepo = ProductRepoImpl.getInstance();
    }
    public ProductServiceImpl(UserRepo userRepo, ProductRepo productRepo){
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @Override
    public Long create(NewProductRequest request) {
        if(!checkNewRequest(request)) throw new BadRequestException("Invalid fields!");

        Product product = productMapper.toModel(request);
        product = productRepo.save(product);

        return product.getId();
    }

    @Override
    public ProductDto update(UpdateProductRequest request) {
        if(!checkUpdateRequest(request)) throw new BadRequestException("Invalid fields!");
        Product product = productMapper.toModel(request);
        if(productRepo.exitsById(product.getId())){
            productRepo.update(product);
        }
        else product = productRepo.save(product);
        return productMapper.toDto(product);
    }

    @Override
    public ProductDto read(Long id) {
        if(!productRepo.exitsById(id)) throw new BadRequestException("Not valid ID!");
        return productMapper.toDto(productRepo.findById(id).get());
    }

    @Override
    public List<ProductDto> readAll() {
        return productRepo.findAll().stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        productRepo.deleteById(id);
    }

    private boolean checkNewRequest(NewProductRequest request){
        return (!request.getName().isEmpty() &&
                request.getNumberOfSold() != null &&
                request.getQuantity() != null &&
                request.getPrice() != null && request.getPrice() != 0 &&
                request.getQuantity() >= request.getNumberOfSold());
    }
    private boolean checkUpdateRequest(UpdateProductRequest request){
        return (!request.getName().isEmpty() &&
                request.getNumberOfSold() != null &&
                request.getQuantity() != null &&
                request.getPrice() != null && request.getPrice() != 0 &&
                request.getQuantity() >= request.getNumberOfSold() &&
                request.getId() != null);
    }
}
