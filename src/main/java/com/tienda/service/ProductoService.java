package com.tienda.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tienda.domain.Producto;
import com.tienda.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean activo) {
        if (activo) {
            return productoRepository.findByActivoTrue();
        }
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer idProducto) {
        return productoRepository.findById(idProducto);
    }

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Transactional
    public void save(Producto producto, MultipartFile imagenFile) {
        productoRepository.save(producto);

        if (!imagenFile.isEmpty()) {
            try {
                String rutaImagen = firebaseStorageService.uploadImage(
                        imagenFile,
                        "producto",
                        producto.getIdProducto()
                );

                producto.setRutaImagen(rutaImagen);
                productoRepository.save(producto);

            } catch (IOException e) {
                // Manejo de excepción
            }
        }
    }

    @Transactional
    public void delete(Integer idProducto) {
        if (!productoRepository.existsById(idProducto)) {
            throw new IllegalArgumentException(
                    "El producto con ID " + idProducto + " no existe."
            );
        }

        try {
            productoRepository.deleteById(idProducto);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "No se puede eliminar el producto. Tiene datos asociados.", e
            );
        }

    }

    @Transactional(readOnly = true)
    public List<Producto> consultaDerivada(double precioINF, double precioSup) {
        return productoRepository.findByPrecioBetweenOrderByPrecioAsc(precioINF, precioSup);
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaJQPL(double precioINF, double precioSup) {
        return productoRepository.consultaJPQL(precioINF, precioSup);
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaSQL(double precioINF, double precioSup) {
        return productoRepository.consultaSQL(precioINF, precioSup);
    }
}
