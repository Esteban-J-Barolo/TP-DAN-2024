"use client";
import Link from "next/link";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import { useState } from "react";

export default function ClientesBuscarCrear(){
    const [palabraBuscar, setPalabraBuscar] = useState("");

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
        <div className="flex items-center justify-between">
            <Link
                href='/dashboard/clientes/agregar'
                className='p-2 bg-red-600 text-white rounded hover:bg-blue-700'
            >
                Agregar
            </Link>
            <div className="flex items-center space-x-4">
                <label>Nombre:</label>
                <input
                type="text"
                onChange={(e) => setPalabraBuscar(e.target.value)}
                placeholder="Buscar . . ."
                className="w-64 px-4 py-2 bg-gray-200 text-gray-700 border border-gray-300 rounded-lg shadow focus:outline-none focus:ring-2 focus:ring-red-500"
                />
                <button 
                    className='p-2 bg-red-600 text-white rounded hover:bg-blue-700'
                    onClick={() => {
                        handleSearch(palabraBuscar);
                    }}
                >
                    Buscar
                </button>
            </div>
        </div>
    );
}