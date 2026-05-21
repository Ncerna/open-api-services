package org.adminBo.controller;

import jakarta.validation.Valid;
import org.adminBo.contact.ICategoryService;
import org.adminBo.dto.category.CategoryRequestDTO;
import org.adminBo.dto.category.CategoryResponseDTO;
import org.adminBo.wrapper.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final ICategoryService service;

    public CategoryController(ICategoryService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<CategoryResponseDTO>> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponseDTO> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ApiResponse<CategoryResponseDTO> save(@Valid @RequestBody CategoryRequestDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public ApiResponse<CategoryResponseDTO> update(@PathVariable Long id,
                                                   @Valid @RequestBody CategoryRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}