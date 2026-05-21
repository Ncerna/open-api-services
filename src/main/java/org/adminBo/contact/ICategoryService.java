package org.adminBo.contact;



import org.adminBo.dto.category.CategoryRequestDTO;
import org.adminBo.dto.category.CategoryResponseDTO;
import org.adminBo.wrapper.ApiResponse;

import java.util.List;

public interface ICategoryService {

    ApiResponse<List<CategoryResponseDTO>> findAll();

    ApiResponse<CategoryResponseDTO> findById(Long id);

    ApiResponse<CategoryResponseDTO> save(CategoryRequestDTO dto);

    ApiResponse<CategoryResponseDTO> update(Long id, CategoryRequestDTO dto);

    ApiResponse<String> delete(Long id);

}