package com.example.service;

import com.example.dao.ProductDao;
import com.example.dto.ProductDto;
import com.example.exception.BadRequestException;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.ProductMapper;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.utility.DeletionValidator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final DeletionValidator validator;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, DeletionValidator validator) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.validator = validator;
    }

    public List<Product> getAllProducts() {
        List<ProductDao> productListDao = productRepository.findAll();
        return productListDao.stream().map(productMapper::toModel).collect(Collectors.toList());
    }

    public Product getProductById(Integer id) {
        Optional<ProductDao> optionalProduct = productRepository.findById(id);
        ProductDao productDao = optionalProduct.orElseThrow(() -> new EntityNotFoundException("Producto con ID: " + id + ", no encontrado"));
        return productMapper.toModel(productDao);
    }

    public List<Product> getProductByCategoryStore(int storeId, int categoryId) {

        List<ProductDao> productListDao = productRepository.findAll();
        List<Product> productListByCat = new ArrayList<>();

        for (ProductDao dao : productListDao)
            if (dao.getStoreId() == storeId && dao.getCategoryId() == categoryId)
                productListByCat.add(productMapper.toModel(dao));
        if (productListByCat.isEmpty())
            throw new EntityNotFoundException("No hay productos con la categoria ID: " + categoryId + " en la tienda de ID: " + storeId);

        return productListByCat;
    }

    public Product createProduct(ProductDto productDto) {
        return productMapper.toModel(productRepository.save(productMapper.toDao(productMapper.toModel(productDto))));
    }

    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id))
            throw new EntityNotFoundException("Producto con ID: " + id + ", no encontrado");
        validator.deletionValidatorProduct(id);
        productRepository.deleteById(id);
    }

    public Product updateProduct(Integer id, ProductDto updatedProductDto) {
        productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Producto con ID: " + id + ", no encontrado"));
        if (!Objects.equals(updatedProductDto.getId(), id))
            throw new BadRequestException("El ID ingresado en el JSON no coincide con el ID de actualización: " + id);
        return productMapper.toModel(productRepository.save(productMapper.toDao(productMapper.toModel(updatedProductDto))));
    }

    public Product partialUpdateProduct(Integer id, Map<String, Object> updates) {
        Optional<ProductDao> optionalProduct = productRepository.findById(id);
        ProductDao productDaoOrigin = optionalProduct.orElseThrow(() -> new EntityNotFoundException("Producto con ID: " + id + ", no encontrado"));
        return productMapper.toModel(productRepository.save(productMapper.parcialUpdateToDao(productDaoOrigin, updates)));
    }
}
