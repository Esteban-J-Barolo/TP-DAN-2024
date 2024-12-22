package isi.dan.ms_productos.data;

import java.math.BigDecimal;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import isi.dan.ms_productos.dao.ProductoRepository;
import isi.dan.ms_productos.modelo.Categoria;
import isi.dan.ms_productos.modelo.Producto;

@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private ProductoRepository productoRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {

        productoRepository.deleteAll();

        // Crear y guardar productos
        IntStream.range(1, 21).forEach(i -> {
            Producto producto = new Producto();
            producto.setId((long)i);
            producto.setNombre("Producto " + i);
            producto.setDescripcion("Descripción del producto " + i);
            
            // Generar valores aleatorios para stock y precio
            producto.setStockActual(random.nextInt(100));
            producto.setStockMinimo(random.nextInt(10) + 1); // Evitar stock mínimo 0
            producto.setPrecio(BigDecimal.valueOf(random.nextDouble() * 100));
            producto.setDescuento(BigDecimal.valueOf(random.nextInt(10)));

            // Asignar categoría aleatoria
            Categoria[] categorias = Categoria.values();
            producto.setCategoria(categorias[random.nextInt(categorias.length)]);

            // Guardar en la base de datos
            productoRepository.save(producto);
        });

        System.out.println("Datos de productos cargados en la base de datos.");
    }

}
