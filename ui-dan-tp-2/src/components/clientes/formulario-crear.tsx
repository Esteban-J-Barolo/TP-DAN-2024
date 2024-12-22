"use client";
import { useState } from "react";
import { fetchClientesCreate } from "./fetch-ms-clientes";
  
export default function CrearClienteFormulario() {
    
    const [formData, setFormData] = useState({
        nombre: '',
        correoElectronico: '',
        cuit: '',
        maximoDescubierto: 0,
        maximoObrasActivas: 0
    });
    const [isLoadingCreate, setIsLoadingCreate] = useState(false);

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
        setIsLoadingCreate(true);
        try {
          const basePath = process.env.NEXT_PUBLIC_CLIENT_API_URL || '';
          const resultado = await fetchClientesCreate(basePath, formData);
          if (resultado) {
            alert("Cliente creado correctamente");
            // Podrías redirigir a la lista de clientes o hacer otra acción
          } else {
            alert("Error al crear al Cliente");
          }
        } catch (error) {
          console.error("Error al crear cliente:", error);
          alert("Ocurrió un error al crear al cliente");
        }finally{
          setIsLoadingCreate(false);
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
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div className="col-span-2 flex justify-center">
        <button 
          type="submit" 
          className='px-4 p-2 bg-red-600 text-white rounded hover:bg-blue-700'
          disabled={isLoadingCreate}
        >
          <div className="flex">
            {isLoadingCreate ? (
              <div className="animate-spin rounded-full h-5 w-5 border-t-2 border-white mr-2"></div>
            ) : null}
            {isLoadingCreate ? 'Guardando...' : 'Guardar'}
          </div>
        </button>
      </div>
    </form>
    );
  }