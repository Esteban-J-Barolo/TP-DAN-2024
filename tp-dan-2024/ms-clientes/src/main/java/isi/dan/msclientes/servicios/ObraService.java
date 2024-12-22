package isi.dan.msclientes.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isi.dan.msclientes.dao.ClienteRepository;
import isi.dan.msclientes.dao.ObraRepository;
import isi.dan.msclientes.enums.EstadoObra;
import isi.dan.msclientes.exception.AgregarObraNotFoundException;
import isi.dan.msclientes.exception.ClienteNotFoundException;
import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.model.Obra;

import java.util.List;
import java.util.Optional;

@Service
public class ObraService {
    
    @Autowired
    private ObraRepository obraRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Obra> findAll() {
        return obraRepository.findAll();
    }

    public Optional<Obra> findById(Integer id) {
        return obraRepository.findById(id);
    }

    public List<Obra> findByCliente(Cliente cli) {
        return obraRepository.findByCliente(cli);
    }

    public Obra save(Obra obra) {
        return obraRepository.save(obra);
    }

    public Obra update(Obra obra) {
        return obraRepository.save(obra);
    }

    public void deleteById(Integer id) {
        obraRepository.deleteById(id);
    }

    public Obra enableObra(Obra obra) {

        Long countActive = obraRepository.findByClienteAndEstado(obra.getCliente(), EstadoObra.HABILITADA)
                                  .stream()
                                  .count();

        if (countActive < obra.getCliente().getMaximoObrasActivas() && obra.getEstado() == EstadoObra.PENDIENTE) {
            obra.setEstado(EstadoObra.HABILITADA);
        }

        return obraRepository.save(obra);
    }

    public Obra finishObra(Obra obra) {

        obra.setEstado(EstadoObra.FINALIZADA);
        
        Obra obraPendiente = obraRepository.findByClienteAndEstado(obra.getCliente(), EstadoObra.PENDIENTE).stream()
                                            .findFirst()
                                            .orElse(null);
        
        if (obraPendiente != null) {
            obraPendiente.setEstado(EstadoObra.HABILITADA);
            obraRepository.save(obraPendiente);
        }

        return obraRepository.save(obra);
    }

    public Obra pendingObra(Obra obra) {

        obra.setEstado(EstadoObra.PENDIENTE);
        
        return obraRepository.save(obra);
    }

    public Obra agregarCliente(Integer clienteId, Obra obra)  throws AgregarObraNotFoundException, ClienteNotFoundException{

        if(!obra.getEstado().equals(EstadoObra.HABILITADA)){
            throw new AgregarObraNotFoundException("Obra no se puede asignar la Obra, no esta habilitada."); 
        }

        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado"));
        
        obra.setCliente(cliente);

        return obraRepository.save(obra);
    }
}

