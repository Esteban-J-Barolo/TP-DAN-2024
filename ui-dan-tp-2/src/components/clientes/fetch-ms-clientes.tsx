const cantidadPorPagina = 10;
// const basePath = 'http://localhost';
// const basePath = 'http://haproxy:80';
interface Cliente {
        id: number;
        nombre: string;
        correoElectronico: string;
        cuit: string;
        maximoDescubierto: number;
        maximoObrasActivas: number;
      }
export async function fetchClientesGetAll(basePath: string) {

	const res = await fetch(`${basePath}/clientes/api/clientes`)
        .then((response) => response.json())
        .catch((error) => console.error("Error:", error));;

	return res;
}
export async function fetchObrasGetAll(basePath: string) {

	const res = await fetch(`${basePath}/clientes/api/obras`)
        .then((response) => response.json())
        .catch((error) => console.error("Error:", error));;

	return res;
}
interface Cliente2 {
        nombre: string;
        correoElectronico: string;
        cuit: string;
        maximoDescubierto: number;
        maximoObrasActivas: number;
      }
export async function fetchClientesUpdate(basePath: string, id: number, clienteActualizado: Cliente2) {
        try {
                const response = await fetch(`${basePath}/clientes/api/clientes/${id}`, {
                        method: "PUT",
                        headers: {
                        "Content-Type": "application/json",
                        },
                        body: JSON.stringify(clienteActualizado), // Convierte el objeto a JSON
                });
                
                if (response.ok) {
                        const data = await response.json();
                        // console.log("Cliente actualizado:", data);
                        return data;
                } else {
                        console.error("Error al actualizar el cliente:", response.status);
                }
        } catch (error) {
                console.error("Error en la solicitud:", error);
        }
}

export async function fetchClientesCreate(basePath: string, clienteActualizado: Cliente2) {
        try {
                const response = await fetch(`${basePath}/clientes/api/clientes`, {
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
                        console.error("Error al crear el cliente:", response.status);
                }
        } catch (error) {
                console.error("Error en la solicitud:", error);
        }
}

export async function fetchClientesDelete(basePath: string, id: number): Promise<boolean> {
        try {
                const response = await fetch(`http://localhost/clientes/api/clientes/${id}`, {
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

interface Obra {
        direccion: string;
        esRemodelacion: boolean;
        lat: number;
        lng: number;
        cliente: Cliente;
        presupuesto: number;
}
export async function fetchObraCreate(basePath: string, clienteActualizado: Obra) {
        try {
                const response = await fetch(`${basePath}/clientes/api/obras`, {
                        method: "POST",
                        headers: {
                        "Content-Type": "application/json",
                        },
                        body: JSON.stringify(clienteActualizado), // Convierte el objeto a JSON
                });
                
                if (response.ok) {
                        const data = await response.json();
                        // console.log("Obra creado:", data);
                        return data;
                } else {
                        console.error("Error al crear la Obra:", response.status);
                }
        } catch (error) {
                console.error("Error en la solicitud:", error);
        }
}

export async function fetchClientesBuscar(basePath: string, nombre: string, page: number ) {
        try {
      
                const response = await fetch(`${basePath}/clientes/api/clientes/buscar?nombre=${nombre}&page=${page}&size=${cantidadPorPagina}`, {
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

export async function fetchClientesCantidadDePaginas( basePath: string, query: string, page: number ) {
        try {
      
                const response = await fetch(`${basePath}/clientes/api/clientes/buscar?nombre=${query}&page=${page}&size=${cantidadPorPagina}`, {
                method: "GET",
                headers: {
                        "Content-Type": "application/json",
                        },
                });
      
          if (response.ok) {
            const data = await response.json();
            return data.totalPages;
          } else {
            console.error("Error al buscar clientes:", response.status);
          }
        } catch (error) {
          console.error("Error en la solicitud:", error);
        }
}

export async function fetchBuscarCliente( basePath: string, id: number ) {

	const res = await fetch(`${basePath}/clientes/api/clientes/${id}`)
        .then((response) => response.json())
        .catch((error) => console.error("Error:", error));;

	return res;
}