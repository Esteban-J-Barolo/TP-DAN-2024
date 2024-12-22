"use client";
import {
    PencilIcon,
    TrashIcon } from '@heroicons/react/24/outline';
import Link from "next/link";
import { useState } from 'react';
import { fetchProductoDelete } from './fetch-ms-productos';
import { usePathname, useSearchParams, useRouter } from 'next/navigation';

export function ActualziarProducto({ id }: { id: number }) {
    return (
      <Link
        href={`/dashboard/productos/editar/${id}`}
        className='p-2 bg-red-600 text-white rounded hover:bg-blue-700'
      >
        <PencilIcon className="w-6" />
      </Link>
    );
}

export function EliminarProducto({ id }: { id: number }) {
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
      
    // Funciones para abrir y cerrar el modal
    const openDialog = () => setIsDialogOpen(true);
    const closeDialog = () => setIsDialogOpen(false);

    // Función para manejar la confirmación
    const handleConfirm = async () => {
        setIsLoading(true);
        const basePath = process.env.NEXT_PUBLIC_CLIENT_API_URL || '';
        const result = await fetchProductoDelete(basePath, id);
        
        closeDialog();
        
        if (result) {
            alert("Producto eliminado correctamente");
            handleSearch("");
        }else{
            alert("Error al eliminar al producto");
        }

        setIsLoading(true);
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
                        <button className="btn bg-red-800 rounded px-2 py-1 hover:bg-blue-700 hover:text-white" onClick={closeDialog}>
                            Cancelar
                        </button>
                        <button
                            disabled={isLoading}
                            className="btn btn-error bg-red-400 rounded px-2 py-1 text-gray-800 hover:bg-blue-700 hover:text-white"
                            onClick={handleConfirm} // Acción de eliminar
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