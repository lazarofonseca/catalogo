package com.catalogo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.catalogo.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

}
