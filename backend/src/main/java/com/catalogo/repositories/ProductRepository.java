package com.catalogo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.catalogo.entities.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	
	//Page<Product> findAll(Pageable pageable);
	
	//Page<Product> findByNameContaining(String name, Pageable pageable);

}
