const cantidadPorPagina = 5;
// const basePath = 'localhost';
// const basePath = 'http://haproxy:80';

interface Producto {
        id: number;
        nombre: string;
        descripcion: string;
        stockActual: number;
        stockMinimo: number;
        precio: number;
        descuento: number;
        codigo: string;
        categoria: string;
      }

export async function fetchProductosGetAll( basePath: string ) {

	const res = await fetch(`${basePath}/productos/api/productos`)
        .then((response) => response.json())
        .catch((error) => console.error("Error:", error));;

	return res;
}

export async function fetchProductoUpdate( basePath: string, productoActualizado: Producto) {
        try {
                const response = await fetch(`${basePath}/productos/api/productos/actualizar`, {
                        method: "PUT",
                        headers: {
                        "Content-Type": "application/json",
                        },
                        body: JSON.stringify(productoActualizado), // Convierte el objeto a JSON
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

export async function fetchProductoCreate( basePath: string, clienteActualizado: Producto) {
        try {
                const response = await fetch(`${basePath}/productos/api/productos`, {
                        method: "POST",
                        headers: {
                        "Content-Type": "application/json",
                        },
                        body: JSON.stringify(clienteActualizado), // Convierte el objeto a JSON
                });
                
                if (response.ok) {
                        const data = await response.json();
                        // console.log("Cliente creado:", data);
                        return data;
                } else {
                        console.error("Error al crear el producto:", response.status);
                }
        } catch (error) {
                console.error("Error en la solicitud:", error);
        }
}

export async function fetchProductoDelete( basePath: string, id: number): Promise<boolean> {
        try {
                const response = await fetch(`${basePath}/productos/api/productos/${id}`, {
                        method: "DELETE",
                        headers: {
                        "Content-Type": "application/json",
                        },
                });
                
                if (response.ok) {
                        // console.log(`Cliente ${id} eliminado`);
                        return true;
                } else {
                        console.error("Error al eliminar el cliente:", response.status);
                        return false;
                }
        } catch (error) {
                console.error("Error en la solicitud:", error);
                return false;
        }
}

export async function fetchProductosBuscar( basePath: string, nombre: string, 
                                        codigo: string, 
                                        precioMin: string, 
                                        precioMax: string, 
                                        stockMin: string, 
                                        stockMax: string, 
                                        page: number ) {
        try {
                const params = new URLSearchParams();

                if (nombre?.trim()) params.set('nombre', nombre.trim());
                if (codigo?.trim()) params.set('codigo', codigo.trim());
                if (precioMin?.trim()) params.set('precioMin', precioMin.trim());
                if (precioMax?.trim()) params.set('precioMax', precioMax.trim());
                if (stockMin?.trim()) params.set('stockMin', stockMin.trim());
                if (stockMax?.trim()) params.set('stockMax', stockMax.trim());

                const url = `${basePath}/productos/api/productos/buscar?${params.toString()}&page=${page}&size=${cantidadPorPagina}`;
                console.log('URL de búsqueda:', url); // Para debugging
        
                const response = await fetch( url, {
                        method: "GET",
                        headers: {
                        "Content-Type": "application/json",
                },
                });
      
                if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                }

                const data = await response.json();
                return data;

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

export async function fetchBuscarProducto( basePath: string, id: number ) {

	const res = await fetch(`${basePath}/productos/api/productos/${id}`)
        .then((response) => response.json())
        .catch((error) => console.error("Error:", error));;

	return res;
}

export async function fetchCategorias ( basePath: string ){
        const response = await fetch(`${basePath}/productos/api/productos/categorias`);
        const categorias = await response.json();
        return categorias;
}