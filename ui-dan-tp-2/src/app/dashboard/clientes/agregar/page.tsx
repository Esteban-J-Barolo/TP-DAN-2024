import CrearClienteFormulario from "@/components/clientes/formulario-crear";
import Link from "next/link";

export default function pageClienteAgregar( ) {
    
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
            <h2 className="text-3xl py-2 font-bold">Nuevo cliente</h2>
            <div className="col-span-2 grid grid-cols-4 py-4 px-4 gap-4">
                <CrearClienteFormulario />
            </div>
        </div>
		)
}