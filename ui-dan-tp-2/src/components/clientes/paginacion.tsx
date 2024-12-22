'use client';

import { usePathname, useRouter, useSearchParams } from "next/navigation";

export default function Paginacion({ totalPaginas }: { totalPaginas: number }) {
    const pathname = usePathname();
    const searchParams = useSearchParams();
    const currentPage = Number(searchParams.get('page')) || 0;
    const { replace } = useRouter();

    const createPageURL = (pageNumber: number | string) => {
        const params = new URLSearchParams(searchParams);
        params.set('page', pageNumber.toString());
        replace(`${pathname}?${params.toString()}`);
    };

    return (
        <div className='flex items-center justify-between w-full'>
            <button
            className={ `p-2 bg-red-600 text-white rounded ${currentPage === 0 ? 'bg-red-800 hover:none' : 'hover:bg-blue-700'} ` }
            disabled={currentPage === 0} onClick={() => createPageURL(currentPage-1)}>
                Anterior
            </button>
            <span className="text-white text-sm text-gray-700">
            Página {currentPage+1} de {totalPaginas}
            </span>
            <button 
            className={ `p-2 bg-red-600 text-white rounded ${currentPage === totalPaginas-1 ? 'bg-red-800 hover:none' : 'hover:bg-blue-700'} ` }
            disabled={currentPage === totalPaginas-1 } onClick={() => createPageURL(currentPage+1)}>
                Siguiente
            </button>
        </div>
    );
}