import { fetchBuscarCliente } from "@/components/clientes/fetch-ms-clientes";
import CrearObraFormulario from "@/components/clientes/formulario-obra";
import Link from "next/link";

export default async function pageClienteAgregarObra(props: { params: Promise<{ id: number }> }) {

    const params = await props.params;
    const id = params.id;

	const basePath = process.env.NEXT_SERVER_API_URL || '';
    
    const cliente = await fetchBuscarCliente(basePath, id);

	return (
        <div>
            <div className="justify-self-end">
                <Link
                    href='/dashboard/clientes'
                    className='px-4 p-2 bg-red-600 text-white rounded hover:bg-blue-700'
                >
                    Volver
                </Link>
            </div>
            <h2 className="text-3xl py-2 font-bold">Agregar nueva obra</h2>
            <div className="col-span-2 grid grid-cols-4 py-4 px-4 gap-4">
                <CrearObraFormulario cliente={cliente} />
            </div>
        </div>
    );
}