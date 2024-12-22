import { fetchPedidoBuscar } from './fetch-ms-pedidos';
import { ActualziarPedido, EliminarPedido } from './botones';

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
interface Pedido {
  id: string;
  fecha: string;
  numeroPedido: number;
  usuario: string;
  observaciones: string;
  cliente: Cliente;
  obra: Obra;
  total: number;
  estado: string;
}

export default async function PedidoTable({
  numero,
  estado,
  clienteId,
  currentPage,
}: {
  numero: string;
  estado: string;
  clienteId: string;
  currentPage: string;
}) {

  const basePath = process.env.NEXT_SERVER_API_URL || '';
  const resp = await fetchPedidoBuscar(basePath, numero, estado, clienteId, currentPage);
  const pedidos = resp.content;

  return (
        <>
          {pedidos?.map((pedido: Pedido) => (
            <tr
              key={pedido.id}
              className="w-full border-b py-3 text-sm last-of-type:border-none [&:first-child>td:first-child]:rounded-tl-lg [&:first-child>td:last-child]:rounded-tr-lg [&:last-child>td:first-child]:rounded-bl-lg [&:last-child>td:last-child]:rounded-br-lg"
            >
              <td className="whitespace-nowrap py-3 pl-6 pr-3">
                <div className="flex justify-center gap-3">
                  <p>{pedido.numeroPedido}</p>
                </div>
              </td>
              <td className="whitespace-nowrap py-3 pl-6 pr-3">
                <div className="flex justify-center gap-3">
                  <p>{pedido.usuario}</p>
                </div>
              </td>
              <td className="whitespace-nowrap py-3 pl-6 pr-3">
                <div className="flex justify-center gap-3">
                  <p>{pedido.cliente.nombre}</p>
                </div>
              </td>
              <td className="whitespace-nowrap py-3 pl-6 pr-3">
                <div className="flex justify-center gap-3">
                  <p>{pedido.obra.direccion}</p>
                </div>
              </td>
              <td className="whitespace-nowrap py-3 pl-6 pr-3">
                <div className="flex justify-center gap-3">
                  <p>{pedido.estado}</p>
                </div>
              </td>
              <td>
                <div className='flex justify-center space-x-4'>
                  <ActualziarPedido id={pedido.id} />
                  <EliminarPedido id={pedido.id} />
                </div>
              </td>
            </tr>
          ))
          }
        </>
  );
}