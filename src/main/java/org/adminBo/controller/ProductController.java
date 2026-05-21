package org.adminBo.controller;

import jakarta.validation.Valid;
import org.adminBo.dto.product.ProductRequestDTO;
import org.adminBo.dto.product.ProductResponseDTO;
import org.adminBo.contact.IProductService;
import org.adminBo.wrapper.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final IProductService service;

    public ProductController(IProductService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<ProductResponseDTO>> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponseDTO> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ApiResponse<ProductResponseDTO> save(@Valid @RequestBody ProductRequestDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponseDTO> update(@PathVariable Long id,
                                                  @Valid @RequestBody ProductRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}