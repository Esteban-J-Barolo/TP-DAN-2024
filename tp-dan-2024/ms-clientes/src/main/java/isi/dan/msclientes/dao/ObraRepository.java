package isi.dan.msclientes.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import isi.dan.msclientes.enums.EstadoObra;
import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.model.Obra;

@Repository
public interface ObraRepository extends JpaRepository<Obra, Integer> {

    List<Obra> findByPresupuestoGreaterThanEqual(BigDecimal price);

    @Query("SELECT o FROM Obra o WHERE o.cliente = :cliente AND o.estado = :estado")
    public List<Obra> findByClienteAndEstado(@Param("cliente") Cliente cliente, @Param("estado") EstadoObra estado);

    public List<Obra> findByCliente(Cliente cliente);
}

