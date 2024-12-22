import { fetchBuscarCliente } from "@/components/clientes/fetch-ms-clientes";
import EditarClienteFormulario from "@/components/clientes/formulario-editar";
import Link from "next/link";


export default async function pageClienteEditar(props: { params: Promise<{ id: number }> }) {

    const params = await props.params;
    const id = params.id;

	const basePath = process.env.NEXT_SERVER_API_URL || '';
    
    const cliente = await fetchBuscarCliente(basePath, id);

	return (
        <div className="w-full grid grid-cols-2">
            <h2 className="text-3xl py-2 font-bold">Editar cliente</h2>
            <div className="justify-self-end">
                <Link
                    href='/dashboard/clientes'
                    className='p-2 bg-red-600 text-white rounded hover:bg-blue-700'
                >
                    Volver
                </Link>
            </div>
            <div className="col-span-2 py-8 grid grid-cols-4 gap-4 ">
                <EditarClienteFormulario cliente={cliente} />
            </div>
        </div>
		);
}