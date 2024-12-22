import { fetchClientesBuscar } from './fetch-ms-clientes';
import { ActualziarCliente, AgregarAClienteObra, EliminarCliente } from './botones';

interface Cliente {
  id: number;
  nombre: string;
  correoElectronico: string;
  cuit: string;
  maximoDescubierto: number;
  maximoObrasActivas: number;
}

export default async function ClientesTable({
  query,
  currentPage,
}: {
  query: string;
  currentPage: number;
}) {

  const basePath = process.env.NEXT_SERVER_API_URL || '';

  const resp = await fetchClientesBuscar(basePath, query, currentPage);
  const clientes = resp.content;

  return (
        <>
          {clientes?.map((cliente: Cliente) => (
            <tr
              key={cliente.id}
              className="w-full border-b py-3 text-sm last-of-type:border-none [&:first-child>td:first-child]:rounded-tl-lg [&:first-child>td:last-child]:rounded-tr-lg [&:last-child>td:first-child]:rounded-bl-lg [&:last-child>td:last-child]:rounded-br-lg"
            >
              <td className="whitespace-nowrap py-3 pl-6 pr-3">
                <div className="flex justify-center gap-3">
                  <p>{cliente.nombre}</p>
                </div>
              </td>
              <td className="whitespace-nowrap py-3 pl-6 pr-3">
                <div className="flex justify-center gap-3">
                  <p>{cliente.cuit}</p>
                </div>
              </td>
              <td>
                <div className='flex justify-center space-x-4'>
                  <AgregarAClienteObra id={cliente.id} />
                  <ActualziarCliente id={cliente.id} />
                  <EliminarCliente id={cliente.id} />
                </div>
              </td>
            </tr>
          ))
          }
        </>
  );
}