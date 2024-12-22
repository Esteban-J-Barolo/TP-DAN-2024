import ClientesBuscarCrear from "@/components/clientes/BuscarCrear";
import EsqueletoTabla from "@/components/clientes/tablaEsqueleto";
import { fetchClientesCantidadDePaginas } from "@/components/clientes/fetch-ms-clientes";
import React, { Suspense } from "react";
import { Metadata } from "next";
import Paginacion from "@/components/clientes/paginacion";
import ClientesTable from "@/components/clientes/ClientesTable";
  
export const metadata: Metadata = {
    title: 'Clientes',
  };

export default async function pageCliente( props: {
	searchParams?: Promise<{
	query?: string;
	page?: string;
	}>;
}) {

    const searchParams = await props.searchParams;
    const query = searchParams?.query || '';
    const currentPage = Number(searchParams?.page) || 0;

	const basePath = process.env.NEXT_SERVER_API_URL || '';
  
    const totalPages = await fetchClientesCantidadDePaginas(basePath, query, currentPage);

	return (
		<div className="w-full" >
			<div className='p-2'>
				<ClientesBuscarCrear />
			</div>
			<div className="inline-block min-w-full align-middle">
				<div className="rounded-lg bg-red-900 p-2 md:pt-0">
					<table className="hidden min-w-full text-gray-100 md:table">
						<thead className="rounded-lg text-left text-sm font-normal">
						<tr>
							<th scope="col" className="px-4 py-5 font-medium sm:pl-6">
							<p className='flex justify-center'>Nombre</p>
							</th>
							<th scope="col" className="px-3 py-5 font-medium">
							<p className='flex justify-center'>CUIT</p>
							</th>
							<th scope="col" className="relative py-3 pl-6 pr-3">
							<p className='flex justify-center'>Opciones</p>
							</th>
						</tr>
						</thead>
						<tbody className="bg-red-800">
							<Suspense fallback={<EsqueletoTabla />} >
								<ClientesTable query={query} currentPage={currentPage}/>
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