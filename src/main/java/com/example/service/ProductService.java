package com.example.service;

import com.example.dao.ProductDao;
import com.example.dao.ShoppingCartOrderDao;
import com.example.exception.EntityNotFoundException;
import com.example.model.Product;
import com.example.model.ShoppingCartOrder;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {

        List<ProductDao> productListDao = productRepository.findAll();
        List<Product> productList = new ArrayList<>();

        for (ProductDao productDao : productListDao) {
            Product product = new Product();
            product.setId(productDao.getId());
            product.setStoreId(productDao.getStoreId());
            product.setName(productDao.getName());
            product.setCategoryId(productDao.getCategoryId());
            product.setPrice(productDao.getPrice());
            product.setStock(productDao.getStock());
            product.setDescription(productDao.getDescription());
            product.setPic(productDao.getPic());
            productList.add(product);
        }
        return productList;
    }

    public List<Product> getProductByCategoryStore(int storeId, int categoryId){

        List<ProductDao> productListDao = productRepository.findAll();
        List<Product> productListByCat = new ArrayList<>();

        for (ProductDao dao: productListDao) if(dao.getStoreId() == storeId && dao.getCategoryId() == categoryId  ) productListByCat.add(toModel(dao));
        if (productListByCat.isEmpty()) throw new EntityNotFoundException("No hay productos con la categoria ID: "+categoryId+ " en la tienda de ID: "+storeId);

        //return productListDao.stream().map(this::toModel).collect(Collectors.toList());
        return productListByCat;
    }

    public Product getProductById(Integer id) {

        Optional<ProductDao> productDao = productRepository.findById(id);

        ProductDao productDao1 = productDao.orElseThrow(() -> new EntityNotFoundException("Producto con ID: " + id + ", no encontrado"));
        return toModel(productDao1);
    }

    public Product createProduct(Product product) {

        ProductDao productToUpload = new ProductDao();
        productToUpload.setName(product.getName());
        productToUpload.setStoreId(product.getStoreId());
        productToUpload.setCategoryId(product.getCategoryId());
        productToUpload.setPrice(product.getPrice());
        productToUpload.setStock(product.getStock());
        productToUpload.setDescription(product.getDescription());
        productToUpload.setPic(product.getPic());

        ProductDao createdProduct = productRepository.save(productToUpload);

        return toModel(createdProduct);
    }
    public void deleteProduct(Integer id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Producto con ID " + id + ", no encontrado");
        }
        productRepository.deleteById(id);
    }


    public Product updateProduct(Integer id, Product updatedProduct) {

        Optional<ProductDao> optionalProductDao = productRepository.findById(id);

        if (optionalProductDao.isEmpty()) {
            throw new EntityNotFoundException("Producto con ID: " + id + " no encontrado");
        }

        ProductDao productDao = optionalProductDao.get();

        productDao.setName(updatedProduct.getName());
        productDao.setDescription(updatedProduct.getDescription());
        productDao.setPrice(updatedProduct.getPrice());
        productDao.setStock(updatedProduct.getStock());
        productDao.setPic(updatedProduct.getPic());
        productDao.setCategoryId(updatedProduct.getCategoryId());
        productDao.setStoreId(1);

        productRepository.save(productDao);

        return new Product(productDao.getId(),
                productDao.getCategoryId(),
                productDao.getName(),
                productDao.getPrice(),
                productDao.getDescription(),
                productDao.getStock(),
                productDao.getPic(),
                productDao.getStoreId());
    }

    public Product partialUpdateProduct(Integer id, Map<String, Object> updates){

        Optional<ProductDao> optionalProductDao = productRepository.findById(id);
        if (optionalProductDao.isEmpty()) {
            throw new EntityNotFoundException("Producto con ID: " + id + " no encontrado");
        }

        final ProductDao productDao = optionalProductDao.get();

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "name":
                        if (value == null || value instanceof String) productDao.setName((String) value);
                        break;
                    case "description":
                        if (value == null || value instanceof String) productDao.setDescription((String) value);
                        break;
                    case "price":
                        if (value instanceof Integer) productDao.setPrice(((Integer) value));
                        break;
                    case "stock":
                        if (value instanceof Number) productDao.setStock(((Number) value).intValue());
                        break;
                    case "pic":
                        if (value == null || value instanceof String) productDao.setPic((String) value);
                        break;
                    case "categoryId":
                        if (value instanceof Number) productDao.setCategoryId(((Number) value).intValue());
                        break;
                    default:
                        throw new IllegalArgumentException("El campo " + key + " no es válido o no existe para actualización.");
                }
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("Tipo de dato incorrecto para el campo " + key);
            }
        });

        productRepository.save(productDao);
        //System.out.println("Producto recuperado de la BD: " + productDao1.getName()); // Agrega este log

        return new Product(productDao.getId(),
                productDao.getCategoryId(),
                productDao.getName(),
                productDao.getPrice(),
                productDao.getDescription(),
                productDao.getStock(),
                productDao.getPic(),
                productDao.getStoreId());
    }

    //conversor dao a model
    private Product toModel(ProductDao dao) {
        Product model = new Product();
        model.setId(dao.getId());
        model.setStock(dao.getStoreId());
        model.setCategoryId(dao.getCategoryId());
        model.setName(dao.getName());
        model.setPrice(dao.getPrice());
        model.setDescription(dao.getDescription());
        model.setStock(dao.getStock());
        model.setPic(dao.getPic());
        model.setStoreId(dao.getStoreId());
        return model;
    }
}