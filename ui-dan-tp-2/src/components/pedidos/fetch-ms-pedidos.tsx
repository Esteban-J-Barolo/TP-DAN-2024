const cantidadPorPagina = 7;
// const basePath = 'http://localhost';
// const basePath = 'http://haproxy:80';

interface Cliente {
        id: number;
        nombre: string;
        correoElectronico: string;
        cuit: string;
        maximoDescubierto: number;
      }
interface Obra {
        id: number;
        direccion: string;
        esRemodelacion: boolean;
        lat: number;
        lng: number;
        cliente: Cliente;
        presupuesto: number;
        estado: string;
}
// interface HistorialPedido {
//         fecha: string;
//         estado: string;
//         userEstado: string;
//         detalle: string;
// }
interface Producto {
        id: number;
        nombre: string;
        descripcion: string;
        precio: number;
}
interface DetallePedido{
        producto: Producto;
        cantidad: number;
        precioUnitario: number;
        descuento: number;
        precioFinal: number;
}
// interface Pedido {
//         id: string;
//         fecha: string;
//         numeroPedido: number;
//         usuario: string;
//         observaciones: string;
//         cliente: Cliente;
//         obra: Obra;
//         total: number;
//         estado: string;
//         estados: Array<HistorialPedido>;
//         detalle: Array<DetallePedido>;
//       }

interface PedidoRequest {
        cliente: Cliente;
        obra: Obra;
        detallesPedido: Array<DetallePedido>;
        observacion: string;
}
// export async function fetchClientesGetAll( page: number ) {

// 	const res = await fetch(`localhost:6080/api/clientes?page=${page}&size=${cantidadPorPagina}`)
//         .then((response) => response.json())
//         .catch((error) => console.error("Error:", error));;

// 	return res;
// }
interface PedidoDTO {
        id: string;
        observaciones: string;
        total: number;
        estado: string;
      }
export async function fetchPedidoUpdate( basePath: string, pedidoActualizado: PedidoDTO) {
        try {
                const response = await fetch(`${basePath}/pedidos/api/pedidos/actualizar`, {
                        method: "PUT",
                        headers: {
                        "Content-Type": "application/json",
                        },
                        body: JSON.stringify(pedidoActualizado), // Convierte el objeto a JSON
                });
                
                if (response.ok) {
                        const data = await response.json();
                        // console.log("Cliente actualizado:", data);
                        return data;
                } else {
                        console.error("Error al actualizar el producto:", response.status);
                }
        } catch (error) {
                console.error("Error en la solicitud:", error);
        }
}

export async function fetchPedidoCreate( 
                                        basePath: string,
                                        pedidoReq: PedidoRequest
                                ) {
        try {
                const response = await fetch(`${basePath}/pedidos/api/pedidos`, {
                        method: "POST",
                        headers: {
                        "Content-Type": "application/json",
                        },
                        body: JSON.stringify(pedidoReq), // Convierte el objeto a JSON
                });
                
                if (response.ok) {
                        const data = await response.json();
                        // console.log("Cliente creado:", data);
                        return data;
                } else {
                        console.error("Error al crear el pedido:", response.status);
                }
        } catch (error) {
                console.error("Error en la solicitud:", error);
        }
}

export async function fetchPedidoDelete( basePath: string, id: string): Promise<boolean> {
        try {
                const response = await fetch(`${basePath}/pedidos/api/pedidos/${id}`, {
                        method: "DELETE",
                        headers: {
                        "Content-Type": "application/json",
                        },
                });
                
                if (response.ok) {
                        // console.log(`Cliente ${id} eliminado`);
                        return true;
                } else {
                        console.error("Error al eliminar el pedido:", response.status);
                        return false;
                }
        } catch (error) {
                console.error("Error en la solicitud:", error);
                return false;
        }
}

export async function fetchPedidoBuscar(     
                                                basePath: string,
                                                numero: string, 
                                                estado: string, 
                                                clienteId: string, 
                                                page: string 
                                        ) {
        try {
                const params = new URLSearchParams();

                if (numero) params.set('numero', numero);
                if (estado) params.set('estado', estado);
                if (clienteId) params.set('clienteId', clienteId);
  
                const response = await fetch(`${basePath}/pedidos/api/pedidos/buscar?${params.toString()}&page=${page}&size=${cantidadPorPagina}`, {
                method: "GET",
                headers: {
                "Content-Type": "application/json",
                },
          });
      
          if (response.ok) {
            const data = await response.json();
        //     console.log("Clientes encontrados:", data);
            return data;
          } else {
            console.error("Error al buscar clientes:", response.status);
          }
        } catch (error) {
          console.error("Error en la solicitud:", error);
        }
}

// export async function fetchProductosCantidadDePaginas( query: string, page: number ) {
//         try {
//                 const response = await fetch(`${basePath}/productos/api/productos/buscar?nombre=${query}&page=${page}&size=${cantidadPorPagina}`, {
//                 method: "GET",
//                 headers: {
//                         "Content-Type": "application/json",
//                         },
//                 });
      
//           if (response.ok) {
//             const data = await response.json();
//             return data.totalPages;
//           } else {
//             console.error("Error al buscar productos:", response.status);
//           }
//         } catch (error) {
//           console.error("Error en la solicitud:", error);
//         }
// }

export async function fetchBuscarPedido( basePath: string, id: string ) {

	const res = await fetch(`${basePath}/pedidos/api/pedidos/${id}`)
        .then((response) => response.json())
        .catch((error) => console.error("Error:", error));;

	return res;
}

export async function fetchEstadosPedido ( basePath: string ){
        const response = await fetch(`${basePath}/pedidos/api/pedidos/estados-pedido`);
        const estados = await response.json();
        return estados;
}