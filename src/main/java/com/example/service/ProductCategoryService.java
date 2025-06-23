package com.example.service;

import com.example.dao.ProductCategoryDao;
import com.example.dto.ProductCategoryDto;
import com.example.exception.BadRequestException;
import com.example.exception.EntityNotFoundException;
import com.example.model.ProductCategory;
import com.example.repository.ProductCategoryRepository;
import com.example.utility.DeletionValidator;
import com.example.utility.ProductCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;
    private final DeletionValidator validator;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository, ProductCategoryMapper productCategoryMapper, DeletionValidator validator) {
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
        this.validator = validator;
    }

    public List<ProductCategory> getAllProductCategories() {
        List<ProductCategoryDao> categoryListDao = productCategoryRepository.findAll();
        return categoryListDao.stream().map(productCategoryMapper::toModel).collect(Collectors.toList());
    }

    public ProductCategory getProductCategoryById(Integer id) {
        Optional<ProductCategoryDao> optionalCategory = productCategoryRepository.findById(id);
        ProductCategoryDao categoryDao = optionalCategory.orElseThrow(() -> new EntityNotFoundException("Categoría con ID: " + id + ", no encontrada"));
        return productCategoryMapper.toModel(categoryDao);
    }

    public ProductCategory createProductCategory(ProductCategoryDto categoryDto) {
        return productCategoryMapper.toModel(productCategoryRepository.save(productCategoryMapper.toDao(productCategoryMapper.toModel(categoryDto))));
    }

    public void deleteProductCategory(Integer id) {
        if (!productCategoryRepository.existsById(id))
            throw new EntityNotFoundException("Categoría con ID: " + id + ", no encontrada");
        validator.deletionValidatorProductCategory(id);
        productCategoryRepository.deleteById(id);
    }

    public ProductCategory updateProductCategory(Integer id, ProductCategoryDto updatedCategoryDto) {
        productCategoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Categoría con ID: " + id + ", no encontrada"));
        if (!Objects.equals(updatedCategoryDto.getId(), id))
            throw new BadRequestException("El ID ingresado en el JSON no coincide con el ID de actualización: " + id);
        return productCategoryMapper.toModel(productCategoryRepository.save(productCategoryMapper.toDao(productCategoryMapper.toModel(updatedCategoryDto))));
    }

    public ProductCategory partialUpdateProductCategory(Integer id, Map<String, Object> updates) {
        Optional<ProductCategoryDao> optionalCategory = productCategoryRepository.findById(id);
        ProductCategoryDao categoryDaoOrigin = optionalCategory.orElseThrow(() -> new EntityNotFoundException("Categoría con ID: " + id + ", no encontrada"));
        return productCategoryMapper.toModel(productCategoryRepository.save(productCategoryMapper.parcialUpdateToDao(categoryDaoOrigin, updates)));
    }
}
