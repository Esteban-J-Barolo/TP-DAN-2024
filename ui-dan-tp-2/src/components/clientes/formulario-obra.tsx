"use client";
import { useState } from "react";
import { fetchObraCreate } from "./fetch-ms-clientes";

interface Cliente {
    id: number;
    nombre: string;
    correoElectronico: string;
    cuit: string;
    maximoDescubierto: number;
    maximoObrasActivas: number;
  }
  
export default function CrearObraFormulario({
    cliente,
  }: {
    cliente: Cliente;
  }) {
    
    const [formData, setFormData] = useState({
        direccion: '',
        presupuesto: 0,
        lat: 0,
        lng: 0,
        esRemodelacion: false,
        cliente: cliente
    });
    const [isLoading, setIsLoading] = useState(false);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: name === 'presupuesto' || name === 'lat' || name === 'lng' 
                ? Number(value) 
                : value
    }))
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsLoading(true);
        try {
          const basePath = process.env.NEXT_PUBLIC_CLIENT_API_URL || '';
          const resultado = await fetchObraCreate(basePath, formData);
          if (resultado) {
            alert("Obra creada correctamente");
            // Podrías redirigir a la lista de clientes o hacer otra acción
          } else {
            alert("Error al crear la obra");
          }
        } catch (error) {
          console.error("Error al crear la obra:", error);
          alert("Ocurrió un error al crear la obra");
        } finally {
          setIsLoading(false);
        }
    };

    return(
        
    <form onSubmit={handleSubmit} className="col-span-4 grid grid-cols-2 gap-4">
        <div>
        <label htmlFor="direccion" className="block mb-2">Direccion</label>
        <input
          type="text"
          id="direccion"
          name="direccion"
        //   defaultValue="Direccion"
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div>
        <label htmlFor="presupuesto" className="block mb-2">Presupuesto</label>
        <input
          type="number"
          id="presupuesto"
          name="presupuesto"
        //   value="Presupuesto"
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div>
        <label htmlFor="lat" className="block mb-2">Latitud</label>
        <input
          type="number"
          id="lat"
          name="lat"
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div>
        <label htmlFor="lng" className="block mb-2">Longitud</label>
        <input
          type="number"
          id="lng"
          name="lng"
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div>
        <label htmlFor="esRemodelacion" className="flex items-center">
            <input
            type="checkbox"
            id="esRemodelacion"
            name="esRemodelacion"
            checked={formData.esRemodelacion}
            onChange={(e) => {
                setFormData(prevState => ({
                ...prevState,
                esRemodelacion: e.target.checked
                }));
            }}
            className="mr-2 h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
            />
            <span>Es Remodelación</span>
        </label>
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
            {isLoading ? 'Guardando...' : 'Agregar'}
          </div>
        </button>
      </div>
    </form>
    );
  }