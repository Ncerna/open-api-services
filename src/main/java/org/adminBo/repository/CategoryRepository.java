package org.adminBo.repository;

import org.adminBo.entity.Category;
import org.adminBo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
    List<Category> findByNameContaining(String name);
    List<Category> findByNameContainingOrderByNameAsc(String name);
    @Query("SELECT c FROM Category c WHERE c.name = :name")
    Category findByNameContaining_(@Param("name") String name);
}
