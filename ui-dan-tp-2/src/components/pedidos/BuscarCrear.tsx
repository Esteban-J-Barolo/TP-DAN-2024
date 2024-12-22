"use client";
import Link from "next/link";
import { usePathname, useRouter, useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";
import { fetchClientesGetAll } from "../clientes/fetch-ms-clientes";
import { fetchEstadosPedido } from "./fetch-ms-pedidos";

interface Cliente {
    id: number;
    nombre: string;
  }

export default function PedidosBuscarCrear(){
    const [filtros, setFiltros] = useState({
        numero: "",
        clienteId: "",
        estado: ""
    });
    const [clientes, setClientes] = useState(Array<Cliente>);
    const [estados, setEstados] = useState([]);
    const [isLoadingCli, setIsLoadingCli] = useState(false);
    const [isLoadingEstados, setIsLoadingEstados] = useState(false);

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

    const handleSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFiltros(prev => ({
            ...prev,
            [name]: value
        }));
    };

    // const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));
    const buscarClientes = async () => {
        setIsLoadingCli(true);
        // await delay(10000);
        const basePath = process.env.NEXT_PUBLIC_CLIENT_API_URL || '';
        const respCli = await fetchClientesGetAll(basePath);
        setClientes(respCli);
        setIsLoadingCli(false);
    }
    const buscarEstadosPedido = async () => {
        setIsLoadingEstados(true);
        // await delay(5000);

        const basePath = process.env.NEXT_PUBLIC_CLIENT_API_URL || '';

        const ests = await fetchEstadosPedido(basePath);
        setEstados(ests);
        setIsLoadingEstados(false);
    }

    useEffect(() => {
        buscarClientes();
        buscarEstadosPedido();
    }, [])

    return (
        <form onSubmit={handleSearch} className="">
            <div className="flex items-center justify-between p-2">
                <Link
                    href='/dashboard/pedidos/agregar'
                    className='p-2 bg-red-600 text-white rounded hover:bg-blue-700'
                >
                    Agregar
                </Link>
            </div>
            
            <div className="grid grid-cols-3 gap-4 border border-gray-800 rounded p-2">
                <div>
                    <label className="block mb-2">N° Pedido</label>
                    <input
                        type="number"
                        name="numero"
                        value={filtros.numero}
                        onChange={handleInputChange}
                        placeholder="Número del pedido"
                        className="w-full px-4 py-2 bg-gray-200 text-gray-700 border border-gray-300 rounded-lg"
                    />
                </div>
                <div>
                    <label className="block mb-2">Cliente</label>
                    <select
                        id="clienteId"
                        name="clienteId"
                        onChange={handleSelectChange}
                        className="w-full p-2 border rounded text-gray-700 bg-gray-200"
                        disabled={isLoadingCli}
                    >
                        {isLoadingCli ? 
                            <option value="">Cargando...</option> 
                            : <option value="">Selecciona un cliente</option>}
                        {clientes.map((cliente) => (
                            <option key={cliente.id} value={cliente.id}>
                                {cliente.nombre}
                            </option>
                        ))}
                    </select>
                </div>
                <div>
                    <label className="block mb-2">Estado obra</label>
                    <select
                        id="estado"
                        name="estado"
                        onChange={handleSelectChange}
                        className="w-full p-2 border rounded text-gray-700 bg-gray-200"
                        disabled={isLoadingEstados}
                    >
                        {isLoadingEstados ? 
                            <option value="">Cargando...</option> 
                            : <option value="">Selecciona un estado</option>}
                        {estados.map((estado) => (
                            <option key={estado} value={estado}>
                                {estado}
                            </option>
                        ))}
                    </select>
                </div>
            
                <div className="col-span-3 flex justify-end">
                    <button 
                        type="submit"
                        className='p-2 bg-red-600 text-white rounded hover:bg-blue-700'
                    >
                        Buscar
                    </button>
                </div>
            </div>
            
            {/* <div className="flex justify-end">
                <button 
                    type="submit"
                    className='p-2 bg-red-600 text-white rounded hover:bg-blue-700'
                >
                    Buscar
                </button>
            </div> */}
        </form>
    );
}