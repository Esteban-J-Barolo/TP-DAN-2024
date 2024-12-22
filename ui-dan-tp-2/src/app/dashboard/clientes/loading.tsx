export default async function loadingClientes(){
    return (
        <div className="flex items-center justify-center w-full h-full">
            <div className="animate-spin rounded-full h-12 w-12 border-t-4 border-red-600">
                {/* Cargando ... */}
            </div>
        </div>
    );
}