package isi.dan.ms.pedidos.exception;

public class PedidoNotFoundException extends Exception{
    public PedidoNotFoundException(String id){
        super("Pedido "+id+" no encontrado");
    }
}
