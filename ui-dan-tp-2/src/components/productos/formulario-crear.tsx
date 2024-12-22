"use client";
import { useEffect, useState } from "react";
import { fetchCategorias, fetchProductoCreate} from "./fetch-ms-productos";
  
export default function CrearProductoFormulario() {
    
  const [isLoading, setIsLoading] = useState(false);
  const [categorias, setCategorias] = useState([]);

    const [formData, setFormData] = useState({
      id: 0,
      nombre: '',
      descripcion: '',
      stockActual: 0,
      stockMinimo: 0,
      precio: 0,
      descuento: 0,
      codigo: '',
      categoria: ''
    });

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
          const resultado = await fetchProductoCreate(basePath, formData);
          if (resultado) {
            alert("Producto creado correctamente");
            // Podrías redirigir a la lista de clientes o hacer otra acción
          } else {
            alert("Error al crear el producto");
          }
        } catch (error) {
          console.error("Error al crear producto:", error);
          alert("Ocurrió un error al crear al producto");
        } finally {
          setIsLoading(false);
        }
    };

    const setearCategorias = async () => {
          try{
            const basePath = process.env.NEXT_PUBLIC_CLIENT_API_URL || '';
            setCategorias(await fetchCategorias(basePath));
          } catch (error) {
            console.error("Error al cargar categorías:", error);
          }
        }
    const handleCategoriaChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
      setFormData(prevState => ({
          ...prevState,
          categoria: e.target.value
      }));
    };
    useEffect( () => {
      setearCategorias();
    }, []);

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
        <label htmlFor="descripcion" className="block mb-2">Descripción</label>
        <input
          type="text"
          id="descripcion"
          name="descripcion"
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div>
        <label htmlFor="stockActual" className="block mb-2">Stock</label>
        <input
          type="number"
          id="stockActual"
          name="stockActual"
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div>
        <label htmlFor="stockMinimo" className="block mb-2">Stock mínimo</label>
        <input
          type="number"
          id="stockMinimo"
          name="stockMinimo"
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div>
        <label htmlFor="precio" className="block mb-2">Precio</label>
        <input
          type="number"
          id="precio"
          name="precio"
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div>
        <label htmlFor="precio" className="block mb-2">Descuento</label>
        <input
          type="number"
          id="descuento"
          name="descuento"
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div>
        <label htmlFor="codigo" className="block mb-2">Código</label>
        <input
          type="text"
          id="codigo"
          name="codigo"
          onChange={handleChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      <div>
          <label htmlFor="categoria" className="block mb-2">Categoría</label>
          <select
              id="categoria"
              name="categoria"
              onChange={handleCategoriaChange}
              required
              className="w-full p-2 border rounded text-gray-700 bg-gray-200"
          >
              <option value="">Selecciona una categoría</option>
              {categorias.map((categoria) => (
                  <option key={categoria} value={categoria}>
                      {categoria}
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
            {isLoading ? 'Guardando...' : 'Guardar'}
          </div>
        </button>
      </div>
    </form>
    );
  }