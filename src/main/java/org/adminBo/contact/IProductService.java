package org.adminBo.contact;

import org.adminBo.dto.product.ProductRequestDTO;
import org.adminBo.dto.product.ProductResponseDTO;
import org.adminBo.wrapper.ApiResponse;

import java.util.List;

public interface IProductService {

    ApiResponse<List<ProductResponseDTO>> findAll();

    ApiResponse<ProductResponseDTO> findById(Long id);

    ApiResponse<ProductResponseDTO> save(ProductRequestDTO dto);

    ApiResponse<ProductResponseDTO> update(Long id, ProductRequestDTO dto);

    ApiResponse<String> delete(Long id);

}
