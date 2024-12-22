"use client";
import { useEffect, useState } from "react";
import { fetchEstadosPedido, fetchPedidoUpdate } from "./fetch-ms-pedidos";

interface PedidoDTO {
  id: string;
  observaciones: string;
  total: number;
  estado: string;
}
  
export default function EditarPedidoFormulario({
    pedido,
  }: {
    pedido: PedidoDTO;
  }) {
    
  const [isLoading, setIsLoading] = useState(false);
  const [isLoadingEstados, setIsLoadingEstados] = useState(false);
  const [estados, setEstados] = useState([]);
  const [formData, setFormData] = useState({
    id: '',
    observaciones: '',
    total: 0,
    estado: ''
  });
    
    useEffect(() => {
        if (pedido)
            setFormData({
              id: pedido.id || '',
              observaciones: pedido.observaciones || '',
              total: pedido.total || 0,
              estado: pedido.estado || ''
        });
        setearEstados();
    }, [pedido]);

    const setearEstados = async () => {
      try{
        setIsLoadingEstados(true);
        const basePath = process.env.NEXT_PUBLIC_CLIENT_API_URL || '';
        setEstados(await fetchEstadosPedido(basePath));
      } catch (error) {
        console.error("Error al cargar estados:", error);
      } finally {
        setIsLoadingEstados(false);
      }
    }
    const handleSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
      setFormData(prevState => ({
          ...prevState,
          estado: e.target.value
      }));
    };

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
          const resultado = await fetchPedidoUpdate(basePath, formData);
          if (resultado) {
            alert("Pedido actualizado correctamente");
            // Podrías redirigir a la lista de clientes o hacer otra acción
          } else {
            alert("Error al actualizar el pedido");
          }
        } catch (error) {
          console.error("Error al actualizar producto:", error);
          alert("Ocurrió un error al actualizar el producto");
        } finally {
          setIsLoading(false);
        }
    };

    return(
        
    <form onSubmit={handleSubmit} className="col-span-4 grid grid-cols-2 gap-4">
      <div>
        <label htmlFor="observaciones" className="block mb-2">Obsercvaciones</label>
        <input
          type="text"
          id="observaciones"
          name="observaciones"
          value={formData.observaciones}
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div>
        <label htmlFor="total" className="block mb-2">Total</label>
        <input
          type="number"
          id="total"
          name="total"
          value={formData.total}
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      
      <div>
          <label htmlFor="categoria" className="block mb-2">Estado</label>
          <select
              id="categoria"
              name="categoria"
              value={formData.estado}
              onChange={handleSelectChange}
              required
              className="w-full p-2 border rounded text-gray-700 bg-gray-200"
              disabled={isLoadingEstados}
          >
              {isLoadingEstados ? 
                            <option value="">Cargando...</option> 
                            : <option value="">Selecciona un estado</option>}
              {estados.map((estado) => (
                  <option key={estado} value={estado}>
                      {estado}
                  </option>
              ))}
          </select>
      </div>
      <div className="col-span-2 flex justify-center">
        <button 
          type="submit"
          disabled={isLoading}
          className='px-4 p-2 bg-red-600 text-white rounded hover:bg-blue-700'
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