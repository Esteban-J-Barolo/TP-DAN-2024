package isi.dan.ms_productos.dao;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import isi.dan.ms_productos.modelo.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
       @Query("SELECT p FROM Producto p WHERE " +
           "(:nombre IS NULL OR :nombre = '' OR LOWER(CAST(p.nombre as string)) LIKE CONCAT('%', LOWER(:nombre), '%')) AND " +
           "(:codigo IS NULL OR :codigo = '' OR p.codigo = :codigo) AND " +
           "(:precioMin IS NULL OR p.precio >= :precioMin) AND " + 
           "(:precioMax IS NULL OR p.precio <= :precioMax) AND " +
           "(:stockMin IS NULL OR p.stockActual >= :stockMin) AND " +
           "(:stockMax IS NULL OR p.stockActual <= :stockMax)")
       Page<Producto> buscarPorCriterios(
            @Param("nombre") String nombre,
            @Param("codigo") String codigo,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax,
            @Param("stockMin") Integer stockMin,
            @Param("stockMax") Integer stockMax,
            Pageable pageable);
}