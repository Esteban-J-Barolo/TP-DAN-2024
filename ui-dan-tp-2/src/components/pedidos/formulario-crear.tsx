"use client";
import { useEffect, useState } from "react";
import { fetchPedidoCreate} from "./fetch-ms-pedidos";
import { fetchClientesGetAll, fetchObrasGetAll } from "../clientes/fetch-ms-clientes";
import { fetchProductosGetAll } from "../productos/fetch-ms-productos";

interface Cliente {
  id: number;
  nombre: string;
  correoElectronico: string;
  cuit: string;
  maximoDescubierto: number;
}
interface Obra {
  id: number;
  direccion: string;
  esRemodelacion: boolean;
  lat: number;
  lng: number;
  cliente: Cliente;
  presupuesto: number;
  estado: string;
}
interface Producto {
        id: number;
        nombre: string;
        descripcion: string;
        precio: number;
}
interface DetallePedido{
        producto: Producto;
        cantidad: number;
        precioUnitario: number;
        descuento: number;
        precioFinal: number;
}
interface PedidoRequest {
  cliente: Cliente;
  obra: Obra;
  detallesPedido: Array<DetallePedido>;
  observacion: string;
}
export default function CrearPedidoFormulario() {
    
  const [isLoadingProductos, setIsLoadingProductos] = useState(false);
  const [isLoadingClientes, setIsLoadingClientes] = useState(false);
  const [isLoadingObras, setIsLoadingObras] = useState(false);
  const [isLoadingCreate, setIsLoadingCreate] = useState(false);
  const [clientes, setClientes] = useState<Cliente[]>([]);
  const [obras, setObras] = useState<Obra[]>([]);
  const [productos, setProductos] = useState<Producto[]>([]);
  const [detallesPedido, setDetallesPedido] = useState<DetallePedido[]>([]);

    const [formData, setFormData] = useState<PedidoRequest>({
      cliente: {
        id: 0,
        nombre: '',
        correoElectronico: '',
        cuit: '',
        maximoDescubierto: 0
      },
      obra: {
        id: 0,
        direccion: '',
        esRemodelacion: false,
        lat: 0,
        lng: 0,
        cliente: {
          id: 0,
          nombre: '',
          correoElectronico: '',
          cuit: '',
          maximoDescubierto: 0
        },
        presupuesto: 0,
        estado: ''
      },
      detallesPedido: [],
      observacion: ''
    });

    const handleClienteChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
      const selectedCliente = JSON.parse(e.target.value);
      setFormData(prev => ({
        ...prev,
        cliente: selectedCliente
      }));
    };

    const handleObraChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
      const selectedObra = JSON.parse(e.target.value);
      setFormData(prev => ({
        ...prev,
        obra: selectedObra
      }));
    };

    const handleObservacionChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      setFormData(prev => ({
        ...prev,
        observacion: e.target.value
      }));
    };

    const setearProductos = async () => {
          try{
            setIsLoadingProductos(true);

            const basePath = process.env.NEXT_PUBLIC_CLIENT_API_URL || '';

            const resProd = await fetchProductosGetAll(basePath);
            setProductos(resProd);
          } catch (error) {
            console.error("Error al cargar estados:", error);
          } finally {
            setIsLoadingProductos(false);
          }
    }
    
    const setearClientes = async () => {
      try{
        setIsLoadingClientes(true);
        const basePath = process.env.NEXT_PUBLIC_CLIENT_API_URL || '';
        const resCli = await fetchClientesGetAll(basePath);
        setClientes(resCli);
      } catch (error) {
        console.error("Error al cargar clientes:", error);
      } finally {
        setIsLoadingClientes(false);
      }
    }
    
    const setearObras = async () => {
      try{
        setIsLoadingObras(true);
        const basePath = process.env.NEXT_PUBLIC_CLIENT_API_URL || '';
        const resObras = await fetchObrasGetAll(basePath);
        setObras(resObras);
      } catch (error) {
        console.error("Error al cargar obras:", error);
      } finally {
        setIsLoadingObras(false);
      }
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsLoadingCreate(true);
        try {
          const basePath = process.env.NEXT_PUBLIC_CLIENT_API_URL || '';
          const resultado = await fetchPedidoCreate(basePath, formData);
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
          setIsLoadingCreate(false);
        }
    };

    useEffect( () => {
      setearClientes();
      setearProductos();
      setearObras();
    }, []);

    // Primero, agregar este estado para manejar el producto seleccionado
const [productoSeleccionado, setProductoSeleccionado] = useState<Producto | null>(null);

// Modificar el handleProductoChange
const handleProductoChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
  const producto = JSON.parse(e.target.value);
  setProductoSeleccionado(producto);
};

// Agregar función para manejar la cantidad
const [cantidad, setCantidad] = useState<number>(0);
const handleCantidadChange = (e: React.ChangeEvent<HTMLInputElement>) => {
  setCantidad(Number(e.target.value));
};

// Función para agregar producto a la lista
const agregarProducto = () => {
  if (!productoSeleccionado || cantidad <= 0) {
    alert('Seleccione un producto y una cantidad válida');
    return;
  }

  const nuevoDetalle: DetallePedido = {
    producto: productoSeleccionado,
    cantidad: cantidad,
    precioUnitario: productoSeleccionado.precio,
    descuento: 0,
    precioFinal: productoSeleccionado.precio * cantidad
  };

  setDetallesPedido(prev => [...prev, nuevoDetalle]);
  setFormData(prev => ({
    ...prev,
    detallesPedido: [...prev.detallesPedido, nuevoDetalle]
  }));

  // Limpiar campos
  setCantidad(0);
  setProductoSeleccionado(null);
};

    return(
        
    <form onSubmit={handleSubmit} className="col-span-4 grid grid-cols-2 gap-4">
        <div>
        <label htmlFor="observacion" className="block mb-2">Observación</label>
        <input
          type="text"
          id="observacion"
          name="observacion"
          onChange={handleObservacionChange}
          required
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      
      <div>
          <label htmlFor="categoria" className="block mb-2">Clientes</label>
          <select
              id="categoria"
              name="categoria"
              onChange={handleClienteChange}
              required
              className="w-full p-2 border rounded text-gray-700 bg-gray-200"
              disabled={isLoadingClientes}
          >
              {isLoadingClientes ? 
                            <option value="">Cargando...</option> 
                            : <option value="">Selecciona un cliente</option>}
              {clientes.map((cliente) => (
                  <option key={cliente.id} value={JSON.stringify(cliente)}>
                      {cliente.nombre}
                  </option>
              ))}
          </select>
      </div>
      <div>
          <label htmlFor="categoria" className="block mb-2">Obras</label>
          <select
              id="categoria"
              name="categoria"
              onChange={handleObraChange}
              required
              className="w-full p-2 border rounded text-gray-700 bg-gray-200"
              disabled={isLoadingObras}
          >
              {isLoadingObras ? 
                            <option value="">Cargando...</option> 
                            : <option value="">Selecciona una obra</option>}
              {obras.map((obra) => (
                  <option key={obra.id} value={JSON.stringify(obra)}>
                      {obra.direccion}
                  </option>
              ))}
          </select>
      </div>

      <div>
          <label htmlFor="categoria" className="block mb-2">Productos</label>
          <select
              id="categoria"
              name="categoria"
              onChange={handleProductoChange}
              value={productoSeleccionado ? JSON.stringify(productoSeleccionado) : ''}
              className="w-full p-2 border rounded text-gray-700 bg-gray-200"
              disabled={isLoadingProductos}
          >
              {isLoadingProductos ? 
                            <option value="">Cargando...</option> 
                            : <option value="">Selecciona un producto</option>}
              {productos.map((producto) => (
                  <option key={producto.id} value={JSON.stringify(producto)}>
                      {producto.nombre}
                  </option>
              ))}
          </select>
      </div>
      <div>
        <label htmlFor="cantidad" className="block mb-2">Cantidad</label>
        <input
          type="number"
          id="cantidad"
          name="cantidad"
          onChange={handleCantidadChange}
          value={cantidad}
          className="w-full p-2 border rounded text-gray-700 bg-gray-200"
        />
      </div>
      
      <div className="col-span-2">
        <button 
            type="button"
            onClick={agregarProducto}
            className="justify-end item-end px-4 p-2 bg-blue-600 text-white rounded hover:bg-blue-700"
          >
            Agregar Producto
          </button>
      </div>

      <div className="col-span-2 mt-4">
        <table className="w-full border-collapse border">
          <thead>
            <tr className="bg-red-800">
              <th className="border p-2">Producto</th>
              <th className="border p-2">Cantidad</th>
              <th className="border p-2">Acciones</th>
            </tr>
          </thead>
          <tbody>
            {detallesPedido.map((detalle, index) => (
              <tr key={index}>
                <td className="border p-2">{detalle.producto.nombre}</td>
                <td className="border p-2">{detalle.cantidad}</td>
                <td className="border p-2">
                  <div className="flex justify-center w-full">
                  <button 
                    type="button"
                    onClick={() => {
                      const nuevosDetalles = detallesPedido.filter((_, i) => i !== index);
                      setDetallesPedido(nuevosDetalles);
                      setFormData(prev => ({
                        ...prev,
                        detallesPedido: nuevosDetalles
                      }));
                    }}
                    className="px-2 py-1 bg-red-600 text-white rounded hover:bg-red-700"
                  >
                    Eliminar
                  </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="col-span-2 flex justify-center">
        <button 
          type="submit" 
          disabled={isLoadingCreate}
          className='px-4 p-2 bg-red-600 text-white rounded hover:bg-blue-700'
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