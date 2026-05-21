package org.adminBo.service;

import org.adminBo.contact.IProductService;
import org.adminBo.dto.product.ProductRequestDTO;
import org.adminBo.dto.product.ProductResponseDTO;
import org.adminBo.entity.Category;
import org.adminBo.entity.Product;
import org.adminBo.repository.CategoryRepository;
import org.adminBo.repository.ProductRepository;
import org.adminBo.wrapper.ApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            ModelMapper modelMapper
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse<List<ProductResponseDTO>> findAll() {
        List<ProductResponseDTO> list = productRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.success(list);
    }

    @Override
    public ApiResponse<ProductResponseDTO> findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ApiResponse.success("Product retrieved successfully", mapToDTO(product));
    }

    @Override
    public ApiResponse<ProductResponseDTO> save(ProductRequestDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = modelMapper.map(dto, Product.class);
        product.setCategory(category);

        Product saved = productRepository.save(product);

        return ApiResponse.success("Product created successfully", mapToDTO(saved));
    }

    @Override
    public ApiResponse<ProductResponseDTO> update(Long id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(category);

        Product updated = productRepository.save(product);

        return ApiResponse.success("Product updated successfully", mapToDTO(updated));
    }

    @Override
    public ApiResponse<String> delete(Long id) {
                 Product product = productRepository.findById(id)
                   .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);

        return ApiResponse.success("Product deleted successfully", null);
    }

    private ProductResponseDTO mapToDTO(Product product) {
        ProductResponseDTO dto = modelMapper.map(product, ProductResponseDTO.class);
        dto.setCategoryName(product.getCategory().getName());
        return dto;
    }
}