package isi.dan.msclientes.servicios;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import isi.dan.msclientes.dao.ClienteRepository;
import isi.dan.msclientes.model.Cliente;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    
    @Value("${cliente.maximoDescubierto}")
    private BigDecimal maxDescubierto;
    
    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> findById(Integer id) {
        return clienteRepository.findById(id);
    }

    public Cliente save(Cliente cliente) {
        if(cliente.getMaximoDescubierto() == null){
            cliente.setMaximoDescubierto(maxDescubierto);
        }
        return clienteRepository.save(cliente);
    }

    public Cliente update(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public void deleteById(Integer id) {
        clienteRepository.deleteById(id);
    }

    public Page<Cliente> findByNombreContaining(String nombre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clienteRepository.findByNombreContainingIgnoreCase(nombre, pageable);
    }
}
