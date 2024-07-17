package org.example.service.impl;

import org.example.exception.BadRequestException;
import org.example.map.dto.ProductResponse;
import org.example.map.mapper.ProductMapper;
import org.example.map.mapper.ProductMapperImpl;
import org.example.map.request.ProductRequest;
import org.example.model.Product;
import org.example.repo.ProductRepo;
import org.example.repo.impl.ProductRepoImpl;
import org.example.service.ProductService;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper = new ProductMapperImpl();

    public ProductServiceImpl() {
        productRepo = ProductRepoImpl.getInstance();
    }

    public ProductServiceImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Override
    public Long post(ProductRequest request) {
        if (!checkNewRequest(request)) throw new BadRequestException("Invalid fields!");

        Product product = productMapper.toModel(request);
        product = productRepo.save(product);

        return product.getId();
    }

    @Override
    public ProductResponse edit(Long id, ProductRequest request) {
        if (!checkUpdateRequest(request)) throw new BadRequestException("Invalid fields!");
        Product product = productMapper.toModel(request);
        if (productRepo.exitsById(id)) {
            productRepo.update(product);
        } else product = productRepo.save(product);
        return productMapper.toResponse(product);
    }


    @Override
    public ProductResponse get(Long id) {
        if (!productRepo.exitsById(id)) throw new BadRequestException("Not valid ID!");
        return productMapper.toResponse(productRepo.findById(id).get());
    }

    @Override
    public List<ProductResponse> getAll() {
        return productRepo.findAll().stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        productRepo.deleteById(id);
    }

    private boolean checkNewRequest(ProductRequest request) {
        return (!request.getName().isEmpty() &&
                request.getNumberOfSold() != null &&
                request.getQuantity() != null &&
                request.getPrice() != null && request.getPrice() != 0 &&
                request.getQuantity() >= request.getNumberOfSold());
    }

    private boolean checkUpdateRequest(ProductRequest request) {
        return (!request.getName().isEmpty() &&
                request.getNumberOfSold() != null &&
                request.getQuantity() != null &&
                request.getPrice() != null && request.getPrice() != 0 &&
                request.getQuantity() >= request.getNumberOfSold());
    }
}
