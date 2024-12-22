import { fetchProductosBuscar } from './fetch-ms-productos';
import { ActualziarProducto, EliminarProducto } from './botones';

interface Producto {
  id: number;
  nombre: string;
  descripcion: string;
  stockActual: number;
  stockMinimo: number;
  precio: number;
  descuento: number;
  codigo: string;
  categoria: string;
}

export default async function ProductoTable({
  nombre,
  codigo,
  precioMin,
  precioMax,
  stockMin,
  stockMax,
  currentPage,
}: {
  nombre: string;
  codigo: string;
  precioMin: string;
  precioMax: string;
  stockMin: string;
  stockMax: string;
  currentPage: number;
}) {

  const basePath = process.env.NEXT_SERVER_API_URL || '';
  const resp = await fetchProductosBuscar(basePath, nombre, codigo, precioMin, precioMax, stockMin, stockMax, currentPage);
  const productos = resp.content;

  return (
        <>
          {productos?.map((producto: Producto) => (
            <tr
              key={producto.id}
              className="w-full border-b py-3 text-sm last-of-type:border-none [&:first-child>td:first-child]:rounded-tl-lg [&:first-child>td:last-child]:rounded-tr-lg [&:last-child>td:first-child]:rounded-bl-lg [&:last-child>td:last-child]:rounded-br-lg"
            >
              <td className="whitespace-nowrap py-3 pl-6 pr-3">
                <div className="flex justify-center gap-3">
                  <p>{producto.nombre}</p>
                </div>
              </td>
              <td className="whitespace-nowrap py-3 pl-6 pr-3">
                <div className="flex justify-center gap-3">
                  <p>{producto.precio}</p>
                </div>
              </td>
              <td className="whitespace-nowrap py-3 pl-6 pr-3">
                <div className="flex justify-center gap-3">
                  <p>{producto.stockActual}</p>
                </div>
              </td>
              <td className="whitespace-nowrap py-3 pl-6 pr-3">
                <div className="flex justify-center gap-3">
                  <p>{producto.categoria}</p>
                </div>
              </td>
              <td>
                <div className='flex justify-center space-x-4'>
                  <ActualziarProducto id={producto.id} />
                  <EliminarProducto id={producto.id} />
                </div>
              </td>
            </tr>
          ))
          }
        </>
  );
}