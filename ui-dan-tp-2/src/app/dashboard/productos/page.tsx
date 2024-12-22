import ProductosBuscarCrear from "@/components/productos/BuscarCrear";
import EsqueletoTabla from "@/components/productos/tablaEsqueleto";
import { fetchProductosBuscar } from "@/components/productos/fetch-ms-productos";
import React, { Suspense } from "react";
import { Metadata } from "next";
import Paginacion from "@/components/productos/paginacion";
import ProductoTable from "@/components/productos/producto-table";
  
export const metadata: Metadata = {
    title: 'Productos',
  };

export default async function pageProductos( props: {
	searchParams?: Promise<{
		nombre?: string;
		codigo?: string;
		precioMin?: string;
		precioMax?: string;
		stockMin?: string;
		stockMax?: string;
		page?: number;
	}>;
}) {

    const searchParams = await props.searchParams;
    const nombre = searchParams?.nombre || '';
    const codigo = searchParams?.codigo || '';
    const precioMin = searchParams?.precioMin || '';
    const precioMax = searchParams?.precioMax || '';
    const stockMin = searchParams?.stockMin || '';
    const stockMax = searchParams?.stockMax || '';
    const currentPage = searchParams?.page || 0;

	const basePath = process.env.NEXT_SERVER_API_URL || '';
  
    const respuesta = await fetchProductosBuscar(basePath, nombre, codigo, precioMin, precioMax, stockMin, stockMax, currentPage);

	const totalPages = respuesta.totalPages;

	return (
		<div className="w-full" >
			<div className='p-2'>
				<ProductosBuscarCrear />
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
							<p className='flex justify-center'>Precio</p>
							</th>
							<th scope="col" className="px-3 py-5 font-medium">
							<p className='flex justify-center'>Stock</p>
							</th>
							<th scope="col" className="px-3 py-5 font-medium">
							<p className='flex justify-center'>Categoría</p>
							</th>
							<th scope="col" className="relative py-3 pl-6 pr-3">
							<p className='flex justify-center'>Opciones</p>
							</th>
						</tr>
						</thead>
						<tbody className="bg-red-800">
							<Suspense fallback={<EsqueletoTabla />} >
								<ProductoTable 	nombre={nombre}
												codigo={codigo}
												precioMin={precioMin}
												precioMax={precioMax}
												stockMin={stockMin}
												stockMax={stockMax}
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