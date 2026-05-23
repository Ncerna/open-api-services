package org.adminBo.service;

import org.adminBo.contact.ICategoryService;
import org.adminBo.dto.category.CategoryRequestDTO;
import org.adminBo.dto.category.CategoryResponseDTO;
import org.adminBo.entity.Category;
import org.adminBo.repository.CategoryRepository;
import org.adminBo.wrapper.ApiResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository repository;
    private final ModelMapper modelMapper;

    public CategoryService(
            CategoryRepository repository,
            ModelMapper modelMapper
    ) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse<List<CategoryResponseDTO>> findAll() {

        List<Category> categories = repository.findAll();
        List<CategoryResponseDTO> list = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponseDTO dto = modelMapper.map(category, CategoryResponseDTO.class);
            list.add(dto);
        }
        return ApiResponse.<List<CategoryResponseDTO>>builder()
                .data(list)
                .build();
    }

    @Override
    public ApiResponse<CategoryResponseDTO> findById(Long id) {

        Category category = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found"));

        CategoryResponseDTO response = modelMapper.map(
                category,
                CategoryResponseDTO.class
        );

        return ApiResponse.<CategoryResponseDTO>builder()
                .message("Category retrieved successfully")
                .data(response)
                .build();
    }

    @Override
    public ApiResponse<CategoryResponseDTO> save(
            CategoryRequestDTO dto
    ) {

        Category category = modelMapper.map(dto, Category.class);

        Category saved = repository.save(category);

        CategoryResponseDTO response = modelMapper.map(
                saved,
                CategoryResponseDTO.class
        );

        return ApiResponse.<CategoryResponseDTO>builder()
                .message("Category created successfully")
                .data(response)
                .build();
    }

    @Override
    public ApiResponse<CategoryResponseDTO> update(
            Long id,
            CategoryRequestDTO dto
    ) {

        Category category = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found"));

        category.setName(dto.getName());

        Category updated = repository.save(category);

        CategoryResponseDTO response = modelMapper.map(
                updated,
                CategoryResponseDTO.class
        );

        return ApiResponse.<CategoryResponseDTO>builder()
                .message("Category updated successfully")
                .data(response)
                .build();
    }

    @Override
    public ApiResponse<String> delete(Long id) {

        Category category = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found"));

        repository.delete(category);

        return ApiResponse.<String>builder()
                .status(true)
                .message("Category deleted successfully")
                .build();
    }
}