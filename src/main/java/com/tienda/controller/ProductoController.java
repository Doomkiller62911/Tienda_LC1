package com.tienda.controller;

import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tienda.domain.Producto;
import com.tienda.service.CategoriaService;
import com.tienda.service.ProductoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/categoria")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/listado")
    public String inicio(Model model) {
        var categorias = productoService.getProductos(false);
        model.addAttribute("categorias", categorias);
        model.addAttribute("totalProductos", categorias.size());
        return "/categoria/listado";
    }

    @Autowired
    private MessageSource messageSource;

    @PostMapping("/guardar")
    public String guardar1(
            @Valid Producto categoria,
            @RequestParam MultipartFile imagenFile,
            RedirectAttributes redirectAttributes) {

        productoService.save(categoria, imagenFile);

        redirectAttributes.addFlashAttribute(
                "todoOk",
                messageSource.getMessage(
                        "mensaje.actualizado",
                        null,
                        Locale.getDefault()
                )
        );

        return "redirect:/categoria/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar1(
            @RequestParam Integer idProducto,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";

        try {
            productoService.delete(idProducto);

        } catch (IllegalArgumentException e) {
            titulo = "error";          // Captura la excepción de argumento inválido para el mensaje de "no existe"
            detalle = "categoria.error01";

        } catch (IllegalStateException e) {
            titulo = "error";          // Captura la excepción de estado ilegal para el mensaje de "datos asociados"
            detalle = "categoria.error02";

        } catch (Exception e) {
            titulo = "error";          // Captura cualquier otra excepción inesperada
            detalle = "categoria.error03";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(
                        detalle,
                        null,
                        Locale.getDefault()
                )
        );

        return "redirect:/categoria/listado";
    }

    //PEGAR 2NDA PARTE ACA
    @PostMapping("/guardar")
    public String guardar(@Valid Producto producto, @RequestParam MultipartFile imagenFile, RedirectAttributes redirectAttributes) {

        productoService.save(producto, imagenFile);
        redirectAttributes.addFlashAttribute(
                "todoOk",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault())
        );
        return "redirect:/producto/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idProducto,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String detalle = "mensaje.eliminado";

        try {
            productoService.delete(idProducto);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "categoria.error1";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "categoria.error2";
        } catch (Exception e) {
            titulo = "error";
            detalle = "categoria.error3";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(detalle, null, Locale.getDefault())
        );
        return "redirect:/producto/listado";
    }

    @GetMapping("/modificar/{idProducto}")
    public String modificar(@PathVariable("idProducto") Integer idProducto,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Producto> productoOpt = productoService.getProducto(idProducto);

        if (productoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    messageSource.getMessage("producto.error1", null, Locale.getDefault())
            );
            return "redirect:/producto/listado";
        }

        model.addAttribute("producto", productoOpt.get());

        var categorias = categoriaService.getProductos(true);
        model.addAttribute("categorias", categorias);

        return "/producto/modifica";
    }
}
