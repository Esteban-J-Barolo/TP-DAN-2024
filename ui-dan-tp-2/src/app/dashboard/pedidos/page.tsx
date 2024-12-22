import PedidosBuscarCrear from "@/components/pedidos/BuscarCrear";
import EsqueletoTabla from "@/components/pedidos/tablaEsqueleto";
import React, { Suspense } from "react";
import { Metadata } from "next";
import Paginacion from "@/components/pedidos/paginacion";
import PedidosTable from "@/components/pedidos/pedidos-table";
import { fetchPedidoBuscar } from "@/components/pedidos/fetch-ms-pedidos";
  
export const metadata: Metadata = {
    title: 'Pedidos',
  };

export default async function pagePedidos( props: {
	searchParams?: Promise<{
		numero?: string;
		estado?: string;
		clienteId?: string;
		page?: string;
	}>;
}) {

    const searchParams = await props.searchParams;
    const numero = searchParams?.numero || '';
    const estado = searchParams?.estado || '';
    const clienteId = searchParams?.clienteId || '';
    const currentPage = searchParams?.page || "0";

	const basePath = process.env.NEXT_SERVER_API_URL || '';
  
    const respuesta = await fetchPedidoBuscar(basePath, numero, estado, clienteId, currentPage);

	const totalPages = respuesta.totalPages;

	return (
		<div className="w-full" >
			<div className='p-2'>
				<PedidosBuscarCrear />
			</div>
			<div className="inline-block min-w-full align-middle">
				<div className="rounded-lg bg-red-900 p-2 md:pt-0">
					<table className="hidden min-w-full text-gray-100 md:table">
						<thead className="rounded-lg text-left text-sm font-normal">
						<tr>
							<th scope="col" className="px-4 py-5 font-medium sm:pl-6">
							<p className='flex justify-center'>N° Pedido</p>
							</th>
							<th scope="col" className="px-3 py-5 font-medium">
							<p className='flex justify-center'>Usuario</p>
							</th>
							<th scope="col" className="px-3 py-5 font-medium">
							<p className='flex justify-center'>Nombre cliente</p>
							</th>
							<th scope="col" className="px-3 py-5 font-medium">
							<p className='flex justify-center'>Direccion de obra</p>
							</th>
							<th scope="col" className="px-3 py-5 font-medium">
							<p className='flex justify-center'>Estado obra</p>
							</th>
							<th scope="col" className="relative py-3 pl-6 pr-3">
							<p className='flex justify-center'>Opciones</p>
							</th>
						</tr>
						</thead>
						<tbody className="bg-red-800">
							<Suspense fallback={<EsqueletoTabla />} >
								<PedidosTable 	numero={numero}
												estado={estado}
												clienteId={clienteId}
												currentPage={currentPage} />
							</Suspense>
						</tbody>
					</table>
				</div>
			</div>
			<div className="mt-2">
				<Paginacion totalPaginas={totalPages} />
			</div>
		</div>
		
		)
}