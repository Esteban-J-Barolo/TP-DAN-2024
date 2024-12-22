"use client";
import Link from "next/link";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import { useState } from "react";

export default function ProductosBuscarCrear(){
    const [filtros, setFiltros] = useState({
        nombre: "",
        codigo: "",
        precioMin: "",
        precioMax: "",
        stockMin: "",
        stockMax: ""
    });

    const searchParams = useSearchParams();
    const { replace } = useRouter();
    const pathname = usePathname();
  
    const handleSearch = (e: React.FormEvent) => {
        e.preventDefault();
        
        const params = new URLSearchParams(searchParams);
        
        // Resetear página a 0
        params.set('page', '0');
        
        // Añadir cada filtro si tiene valor
        Object.entries(filtros).forEach(([key, value]) => {
            if (value) {
                params.set(key, value);
            } else {
                params.delete(key);
            }
        });

        replace(`${pathname}?${params.toString()}`);
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFiltros(prev => ({
            ...prev,
            [name]: value
        }));
    };

    return (
        <form onSubmit={handleSearch} className="space-y-4">
            <div className="flex items-center justify-between">
                <Link
                    href='/dashboard/productos/agregar'
                    className='p-2 bg-red-600 text-white rounded hover:bg-blue-700'
                >
                    Agregar
                </Link>
            </div>
            
            <div className="grid grid-cols-3 gap-4">
                <div>
                    <label className="block mb-2">Nombre</label>
                    <input
                        type="text"
                        name="nombre"
                        value={filtros.nombre}
                        onChange={handleInputChange}
                        placeholder="Nombre del producto"
                        className="w-full px-4 py-2 bg-gray-200 text-gray-700 border border-gray-300 rounded-lg"
                    />
                </div>
                
                <div>
                    <label className="block mb-2">Código</label>
                    <input
                        type="text"
                        name="codigo"
                        value={filtros.codigo}
                        onChange={handleInputChange}
                        placeholder="Código del producto"
                        className="w-full px-4 py-2 bg-gray-200 text-gray-700 border border-gray-300 rounded-lg"
                    />
                </div>
                
                <div>
                    <label className="block mb-2">Precio Mínimo</label>
                    <input
                        type="number"
                        name="precioMin"
                        value={filtros.precioMin}
                        onChange={handleInputChange}
                        placeholder="Precio mínimo"
                        className="w-full px-4 py-2 bg-gray-200 text-gray-700 border border-gray-300 rounded-lg"
                    />
                </div>
                
                <div>
                    <label className="block mb-2">Precio Máximo</label>
                    <input
                        type="number"
                        name="precioMax"
                        value={filtros.precioMax}
                        onChange={handleInputChange}
                        placeholder="Precio máximo"
                        className="w-full px-4 py-2 bg-gray-200 text-gray-700 border border-gray-300 rounded-lg"
                    />
                </div>
                
                <div>
                    <label className="block mb-2">Stock Mínimo</label>
                    <input
                        type="number"
                        name="stockMin"
                        value={filtros.stockMin}
                        onChange={handleInputChange}
                        placeholder="Stock mínimo"
                        className="w-full px-4 py-2 bg-gray-200 text-gray-700 border border-gray-300 rounded-lg"
                    />
                </div>
                
                <div>
                    <label className="block mb-2">Stock Máximo</label>
                    <input
                        type="number"
                        name="stockMax"
                        value={filtros.stockMax}
                        onChange={handleInputChange}
                        placeholder="Stock máximo"
                        className="w-full px-4 py-2 bg-gray-200 text-gray-700 border border-gray-300 rounded-lg"
                    />
                </div>
            </div>
            
            <div className="flex justify-end">
                <button 
                    type="submit"
                    className='p-2 bg-red-600 text-white rounded hover:bg-blue-700'
                >
                    Buscar
                </button>
            </div>
        </form>
    );
}