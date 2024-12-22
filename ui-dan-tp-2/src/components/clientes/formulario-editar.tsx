"use client";
import { useEffect, useState } from "react";
import { fetchClientesUpdate } from "./fetch-ms-clientes";

interface Cliente {
    id: number;
    nombre: string;
    correoElectronico: string;
    cuit: string;
    maximoDescubierto: number;
    maximoObrasActivas: number;
  }
  
export default function EditarClienteFormulario({
    cliente,
  }: {
    cliente: Cliente;
  }) {
    
    const [formData, setFormData] = useState({
        nombre: '',
        correoElectronico: '',
        cuit: '',
        maximoDescubierto: 0,
        maximoObrasActivas: 0
    });
    const [isLoading, setIsLoading] = useState(false);
    
    useEffect(() => {
        if (cliente) {
            setFormData({
            nombre: cliente.nombre || '',
            correoElectronico: cliente.correoElectronico || '',
            cuit: cliente.cuit || '',
            maximoDescubierto: cliente.maximoDescubierto || 0,
            maximoObrasActivas: cliente.maximoObrasActivas || 0
        });
    }
    }, [cliente]);
    // Manejar cambios en los inputs
    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
        ...prevState,
        [name]: value
        }));
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsLoading(true);
        try {
          const basePath = process.env.NEXT_PUBLIC_CLIENT_API_URL || '';
          const resultado = await fetchClientesUpdate(basePath, cliente.id, formData);
          if (resultado) {
            alert("Cliente actualizado correctamente");
            // Podrías redirigir a la lista de clientes o hacer otra acción
          } else {
            alert("Error al actualizar el Cliente");
          }
        } catch (error) {
          console.error("Error al actualizar cliente:", error);
          alert("Ocurrió un error al actualizar el cliente");
        } finally {
          setIsLoading(false);
        }
    };

    return(
        
    <form onSubmit={handleSubmit} className="col-span-4 grid grid-cols-2 gap-4">
        <div>
        <label htmlFor="nombre" className="block mb-2">Nombre</label>
        <input
          type="text"
          id="nombre"
          name="nombre"
          value={formData.nombre}
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div>
        <label htmlFor="correoElectronico" className="block mb-2">Correo Electrónico</label>
        <input
          type="email"
          id="correoElectronico"
          name="correoElectronico"
          value={formData.correoElectronico}
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div>
        <label htmlFor="cuit" className="block mb-2">CUIT</label>
        <input
          type="text"
          id="cuit"
          name="cuit"
          value={formData.cuit}
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div>
        <label htmlFor="maximoDescubierto" className="block mb-2">Máximo Descubierto</label>
        <input
          type="number"
          id="maximoDescubierto"
          name="maximoDescubierto"
          value={formData.maximoDescubierto}
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div>
        <label htmlFor="maximoCantidadObras" className="block mb-2">Máximo Cantidad de Obras</label>
        <input
          type="number"
          id="maximoCantidadObras"
          name="maximoCantidadObras"
          value={formData.maximoObrasActivas}
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div className="col-span-2 flex justify-center">
        <button 
          type="submit" 
          className='px-4 p-2 bg-red-600 text-white rounded hover:bg-blue-700'
          disabled={isLoading}
        >
          <div className="flex">
            {isLoading ? (
              <div className="animate-spin rounded-full h-5 w-5 border-t-2 border-white mr-2"></div>
            ) : null}
            {isLoading ? 'Guardando...' : 'Guardar Cambios'}
          </div>
        </button>
      </div>
    </form>
    );
  }