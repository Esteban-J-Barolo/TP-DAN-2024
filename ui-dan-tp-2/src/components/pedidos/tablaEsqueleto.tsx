export default function EsqueletoTabla() {
    return(
        <>
            <tr className="w-full border-b py-3 text-sm last-of-type:border-none [&:first-child>td:first-child]:rounded-tl-lg [&:first-child>td:last-child]:rounded-tr-lg [&:last-child>td:first-child]:rounded-bl-lg [&:last-child>td:last-child]:rounded-br-lg">
                <td className="whitespace-nowrap py-3 pl-6 pr-3">
                    <div className="flex items-center gap-3">
                    <p>Cargando nombres ...</p>
                    </div>
                </td>
                <td className="whitespace-nowrap py-3 pl-6 pr-3">
                    <div className="flex items-center gap-3">
                    <p>Cargando CUITs ...</p>
                    </div>
                </td>
                <td>
                    <div className="flex items-center gap-3">
                    <p>Botones ...</p>
                    </div>
                </td>
                </tr>
        </>
    );
}