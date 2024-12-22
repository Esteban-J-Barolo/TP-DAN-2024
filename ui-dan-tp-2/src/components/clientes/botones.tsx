"use client";
import { 
    BriefcaseIcon,
    PencilIcon,
    TrashIcon } from '@heroicons/react/24/outline';
import Link from "next/link";
import { useState } from 'react';
import { fetchClientesDelete } from './fetch-ms-clientes';
import { usePathname, useSearchParams, useRouter } from 'next/navigation';

export function ActualziarCliente({ id }: { id: number }) {
    return (
      <Link
        href={`/dashboard/clientes/editar/${id}`}
        className='p-2 bg-red-600 text-white rounded hover:bg-blue-700'
      >
        <PencilIcon className="w-6" />
      </Link>
    );
}

export function AgregarAClienteObra({ id }: { id: number }) {
    return (
      <Link
        href={`/dashboard/clientes/agregarobra/${id}`}
        className='p-2 bg-red-600 text-white rounded hover:bg-blue-700'
      >
        <BriefcaseIcon className="w-6" />
      </Link>
    );
}

export function EliminarCliente({ id }: { id: number }) {
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
      
    // Funciones para abrir y cerrar el modal
    const openDialog = () => setIsDialogOpen(true);
    const closeDialog = () => setIsDialogOpen(false);

    // Función para manejar la confirmación
    const handleConfirm = async () => {
        setIsLoading(true);

        const basePath = process.env.NEXT_PUBLIC_CLIENT_API_URL || '';
        
        const result = await fetchClientesDelete(basePath, id);
        
        closeDialog();
        
        if (result) {
            alert("Cliente eliminado correctamente");
            handleSearch("");
        }else{
            alert("Error al eliminar al Cliente");
        }
        setIsLoading(false);
    };

    const searchParams = useSearchParams();
    const { replace } = useRouter();
    const pathname = usePathname();
    
    const handleSearch = (term: string) => {
        console.log(`Searching... ${term}`);
    
        const params = new URLSearchParams(searchParams);
    
        params.set('page', '0');
    
        if (term) {
        params.set('query', term);
        } else {
        params.delete('query');
        }
        replace(`${pathname}?${params.toString()}`);
    };
    
    return (
        <div>
            <button 
            className='p-2 bg-red-600 text-white rounded hover:bg-blue-700'
            onClick={openDialog}>
                <TrashIcon className="w-6" />
            </button>
            {isDialogOpen && (
                <dialog open className="fixed inset-0 modal modal-open w-96 p-6 bg-black rounded-lg shadow-lg text-gray-300">
                <div className="modal-box">
                    <h3 className="font-bold text-lg">Confirmar eliminación</h3>
                    <p className="py-4">¿Estás seguro de que deseas eliminar este elemento?</p>
                    <div className="modal-action flex justify-between w-full">
                        <button className="btn bg-red-800 rounded px-2 py-1 hover:bg-blue-700 hover:text-white" 
                            onClick={closeDialog}
                            disabled={isLoading}>
                            Cancelar
                        </button>
                        <button
                            className="btn btn-error bg-red-400 rounded px-2 py-1 text-gray-800 hover:bg-blue-700 hover:text-white"
                            onClick={handleConfirm} // Acción de eliminar
                            disabled={isLoading}
                        >
                             {isLoading ? (
                                <div className="animate-spin rounded-full h-5 w-5 border-t-2 border-white p-2"></div>
                                ) : null}
                                {isLoading ? '' : 'Eliminar'}
                        </button>
                    </div>
                </div>
                </dialog>
            )}
        </div>
    );
  }