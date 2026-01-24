package com.catalogo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.catalogo.dto.CategoryDTO;
import com.catalogo.dto.ProductDTO;
import com.catalogo.entities.Category;
import com.catalogo.entities.Product;
import com.catalogo.exception.DatabaseException;
import com.catalogo.exception.ResourceNotFoundException;
import com.catalogo.repositories.CategoryRepository;
import com.catalogo.repositories.ProductRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<Product> findAll(Pageable pageable) {

		/* IMPLEMENTAÇÃO ANTIGA
		List<Product> list = repository.findAll();
		//return list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
		 * 
		 */
		 return productRepository.findAll(pageable);
		//Page<Product> list = repository.findAll(pageRequest);
		//return list.map(x -> new ProductDTO(x));
		
	}

	@Transactional
	public ProductDTO findById(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado, tente outro id"));
		return new ProductDTO(product, product.getCategories());

	}

	@Transactional
	public ProductDTO create(ProductDTO dto) {
		Product product = new Product();
		copyDtoToEntity(dto, product);
		product = productRepository.save(product);
		return new ProductDTO(product);
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product product = productRepository.getReferenceById(id);
			copyDtoToEntity(dto, product);
			product = productRepository.save(product);
			return new ProductDTO(product);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id não encontrado" + id);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!productRepository.existsById(id)) {
	        throw new ResourceNotFoundException("Categoria não encontrada. ID: " + id);
	    }

	    try {
	        productRepository.deleteById(id);
	    } catch (DataIntegrityViolationException e) {
	        throw new DatabaseException("Violação de integridade: Não é possível deletar categoria vinculada a outros registros");
	    }
	}
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		
		entity.getCategories().clear();
		for(CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
	}

}
