package com.arquitectura.ecommerce.service;

import com.arquitectura.ecommerce.dto.CategoriaDTO;

import java.util.List;

public interface CategoriaService {

    CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO);

    CategoriaDTO obtenerCategoriaPorId(Long id);

    List<CategoriaDTO> obtenerTodasLasCategorias();

    CategoriaDTO actualizarCategoria(Long id, CategoriaDTO categoriaDTO);

    void eliminarCategoria(Long id);
}
