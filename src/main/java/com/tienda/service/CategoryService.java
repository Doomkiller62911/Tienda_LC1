package com.tienda.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tienda.domain.Categoria;
import com.tienda.repository.CategoriaRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activo) {
        if (activo) {
            return categoriaRepository.findByActivoTrue();
        }
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Categoria> getCategoria(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Transactional
    public void save(Categoria categoria, MultipartFile imagenFile) {
        categoriaRepository.save(categoria);

        if (!imagenFile.isEmpty()) { // Si no estÃ¡ vacÃ­o... pasaron una imagen...
            try {
                String rutaImagen = firebaseStorageService.uploadImage(
                        imagenFile,
                        "categoria",
                        categoria.getIdCategoria()
                );

                categoria.setRutaImagen(rutaImagen);
                categoriaRepository.save(categoria);

            } catch (IOException e) {
                // Manejo de excepciÃ³n (no visible en la imagen)
            }
        }
    }

    @Transactional
    public void delete(Integer idCategoria) {

        // Verifica si la categorÃ­a existe antes de intentar eliminarlo
        if (!categoriaRepository.existsById(idCategoria)) {
            // Lanza una excepciÃ³n para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException(
                    "La categoria con ID " + idCategoria + " no existe."
            );
        }

        try {
            categoriaRepository.deleteById(idCategoria);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepciÃ³n para encapsular el problema de integridad de datos
            throw new IllegalStateException(
                    "No se puede eliminar la categoria. Tiene datos asociados.", e
            );
        }
    }
}
