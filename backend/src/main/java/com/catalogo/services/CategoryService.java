package com.catalogo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.catalogo.dto.CategoryDTO;
import com.catalogo.entities.Category;
import com.catalogo.exception.DatabaseException;
import com.catalogo.exception.ResourceNotFoundException;
import com.catalogo.repositories.CategoryRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {

		List<Category> list = repository.findAll();
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}

	@Transactional
	public CategoryDTO findById(Long id) {
		Category categoria = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado, tente outro id"));
		return CategoryDTO.converterCategoryToCategoryDto(categoria);

	}

	@Transactional
	public CategoryDTO create(CategoryDTO dto) {
		Category categoria = new Category();
		categoria.setName(dto.getName());
		categoria = repository.save(categoria);
		return new CategoryDTO(categoria);
	}
	
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category category = repository.getReferenceById(id);
			category.setName(dto.getName());
			category = repository.save(category);
			return new CategoryDTO(category);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id não encontrado" + id);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
	        throw new ResourceNotFoundException("Categoria não encontrada. ID: " + id);
	    }

	    try {
	        repository.deleteById(id);
	    } catch (DataIntegrityViolationException e) {
	        throw new DatabaseException("Violação de integridade: Não é possível deletar categoria vinculada a outros registros");
	    }
	}

}
