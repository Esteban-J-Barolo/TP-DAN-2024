import { fetchBuscarPedido } from "@/components/pedidos/fetch-ms-pedidos";
import EditarPedidoFormulario from "@/components/pedidos/formulario-editar";
import Link from "next/link";


export default async function pagePedidosEditar(props: { params: Promise<{ id: string }> }) {

    const params = await props.params;
    const id = params.id;

	const basePath = process.env.NEXT_SERVER_API_URL || '';
    
    const pedido = await fetchBuscarPedido(basePath, id);

	return (
        <div className="w-full grid grid-cols-2">
            <h2 className="text-3xl py-2 font-bold">Editar pedido</h2>
            <div className="justify-self-end">
                <Link
                    href='/dashboard/pedidos'
                    className='p-2 bg-red-600 text-white rounded hover:bg-blue-700'
                >
                    Volver
                </Link>
            </div>
            <div className="col-span-2 py-8 grid grid-cols-4 gap-4 ">
                <EditarPedidoFormulario pedido={pedido} />
            </div>
        </div>
		);
}