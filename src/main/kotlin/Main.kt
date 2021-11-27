data class Tienda (val nombre: String, val clientes: List<Cliente>){
    fun obtenerConjuntoDeClientes(): Set<Cliente> = clientes.toSet()  //Ej 4.3.1

    fun obtenerCiudadesDeClientes(): Set<Ciudad> = clientes.map{it.ciudad}.toSet() //Ej 4.3.2
    fun obtenerClientesPor(ciudad:Ciudad): List<Cliente> = clientes.filter{it.ciudad == ciudad}

    fun checkTodosClientesSonDe(ciudad:Ciudad): Boolean = clientes.all{it.ciudad == ciudad} //ej 4.3.3
    fun hayClientesDe(ciudad:Ciudad): Boolean = clientes.any{it.ciudad == ciudad} //ej 4.3.3
    fun cuentaClientesDe(ciudad:Ciudad): Int = clientes.count{it.ciudad == ciudad} //ej 4.3.3
    fun encuentraClienteDe(ciudad:Ciudad): Cliente? = clientes.find{it.ciudad == ciudad} //ej 4.3.3

    fun obtenerClientesOrdenadosPorPedidos(): List<Cliente> = clientes.sortedByDescending{it.pedidos.size} // Ej. 4.3.4

    //Ej 4.3.5
    fun obtenerClientesConPedidosSinEntregar(): Set<Cliente> {
        val clientesConPedidosPendientes = clientes.filter {
            val(entregados, noEntregados) = it.pedidos.partition{it.estaEntregado}
            entregados.size < noEntregados.size
        }
        //val test = clientes.map{Pedido::productos}.filter{nombre == "Leche"}
        //val test2 = clientes.flatMap(Cliente::obtenerProductosPedidos).map{Producto::nombre}
        return clientesConPedidosPendientes.toSet()
    }

    //Ej 4.3.6 (Parte 2/2)
    fun obtenerProductosPedidos(): Set<Producto> = clientes.flatMap(Cliente::obtenerProductosPedidos).toSet()

    //Ej 4.3.7
    fun obtenerProductosPedidosPorTodos(): Set<Producto>{
        val conjuntoProductos = clientes.flatMap {it.obtenerProductosPedidos()}.toSet()
        return clientes.fold(conjuntoProductos){
                pedidos, cliente -> pedidos.intersect(cliente.obtenerProductosPedidos())
        }
    }

    //Ej 4.3.8 (Parte 2/2)
    fun obtenerNumeroVecesProductoPedido(producto:Producto):Int = clientes.flatMap(Cliente::obtenerProductosPedidos).count{it == producto}

    //Ej 4.3.9
    fun agrupaClientesPorCiudad(): Map<Ciudad, List<Cliente>> = clientes.groupBy{it.ciudad}

    //Ej 4.3.10 (Parte 1/3)
    fun mapeaNombreACliente(): Map<String, Cliente> = clientes.associateBy(Cliente::nombre)
    //Ej 4.3.10 (Parte 2/3)
    fun mapeaClienteACiudad(): Map<Cliente, Ciudad> = clientes.associateWith(Cliente::ciudad)
    //Ej 4.3.10 (Parte 3/3)
    fun mapeaNombreClienteACiudad(): Map<String, Ciudad> = clientes.associate{it.nombre to it.ciudad}

    //Ej 4.3.11 (Parte 1/2)
    fun obtenerClientesConMaxPedidos(): Cliente? = clientes.maxByOrNull{it.pedidos.size}


}

data class Cliente(val nombre:String, val ciudad: Ciudad, val pedidos: List<Pedido>){
    override fun toString() = "$nombre from ${ciudad.nombre}"

    //Ej 4.3.6 (Parte 1/2)
    fun  obtenerProductosPedidos(): List<Producto> = pedidos.flatMap(Pedido::productos)


    //Ej 4.3.8 (Parte 1/2)  Producto más caro DE ENTRE LOS ENTREGADOS
    fun encuentraProductoMasCaro(): Producto?{
        val pedidosEntregados = pedidos.filter(Pedido::estaEntregado)
        val productosEntregados = pedidosEntregados.flatMap(Pedido::productos)
        return productosEntregados.maxByOrNull(Producto::precio)
    }

    //Ej 4.3.11 (Parte 2/2)  Producto más caro de todos los pedidos, AUNQUE NO ESTÉ ENTREGADO
    fun obtenerProductoMasCaroPedido(): Producto? = pedidos.flatMap(Pedido::productos).maxByOrNull(Producto::precio)

    //Ej 4.3.12
    fun dineroGastado(): Double = pedidos.flatMap{it.productos}.sumOf{it.precio}
}

data class Pedido(val productos: List<Producto>, val estaEntregado: Boolean)

data class Producto(val nombre:String, val precio:Double){
    override fun toString() = "'$nombre' for $precio"
}

data class Ciudad(val nombre: String){
    override fun toString() = nombre
}


fun main() {
    val cadiz = Ciudad("Cádiz")
    val moscu = Ciudad("Moscú")

    val prod1 = Producto("Leche", 3.50)
    val prod2 = Producto("Harina", 0.90)
    val prod3 = Producto("Aceite", 6.25)
    val prod4 = Producto("Huevos", 5.0)
    val prod5 = Producto("Aceite", 5.0)
    val listaProductos1 = listOf(prod1, prod2, prod3)
    val listaProductos2 = listOf(prod1, prod2, prod3, prod4, prod5)

    val pedido1 = Pedido(listaProductos1, false)
    val pedido2 = Pedido(listaProductos2, true)
    val listaPedidos1 = listOf(pedido1, pedido2)
    val listaPedidos2 = listOf(pedido1)

    val alejandro = Cliente("Alejandro", cadiz, listaPedidos1)
    val manuel = Cliente("Manuel", moscu, listaPedidos2)
    val listaClientes = listOf(alejandro, manuel)

    val tienda = Tienda("Alimerka", listaClientes)

    //print(tienda.obtenerConjuntoDeClientes())
    println(tienda.obtenerCiudadesDeClientes())
    println(tienda.obtenerClientesPor(moscu))

    println(alejandro.obtenerProductosPedidos())
    println(tienda.obtenerProductosPedidos())

    println("producto más caro: " + alejandro.obtenerProductoMasCaroPedido())
    println("dinero gastado = " + alejandro.dineroGastado())

    println(tienda.obtenerNumeroVecesProductoPedido(prod1))

    println(tienda.checkTodosClientesSonDe(cadiz))
    println(tienda.hayClientesDe(cadiz))
    println(tienda.cuentaClientesDe(moscu))
}