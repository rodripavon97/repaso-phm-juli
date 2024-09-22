package ar.edu.unsam.phm.tpphmgrupo4

import ar.edu.unsam.phm.tpphmgrupo4.Exceptions.StockException
import java.time.LocalDate
import java.time.LocalTime
/*
En en el enunciado no decia nada pero mirando la vista detalles de un show
creamos la fecha y hora de la funcion con su precio base de su entrada
el estado
numero de entradas totales, disponibles y vendidas (panel adm)
 */
class Funcion(
    var fecha: LocalDate,
    var hora: LocalTime,
    var precioBaseEntrada: Double,
    var showEstado: Estado,
    var numeroEntradasTotales: Int
) : Entity{
    var entradasDisponibles = mutableListOf<Entrada>()
    var entradasVendidas = mutableListOf<Entrada>()
    override var id: Int = Entity.idInicial

    fun cambiarEstado(estado: Estado) {
        showEstado = estado
        entradasDisponibles.forEach { cambiarEstado(estado) }
    }

    fun limpiarEntradasDisponibles() {
        entradasDisponibles.clear()
    }

    fun agregarEntrada(ubicacion: Ubicacion, showId: Int) {
        entradasDisponibles.add(
            Entrada(
            entradasDisponibles.size+1,
            ubicacion,
            this.fecha,
            this.precioBaseEntrada,
            this.showEstado,
            this.id,
            showId
        )
        )
    }

    fun venderEntrada(entrada: Entrada) {
        if (!funcionAgotada()) {
            if (entradasDisponibles.contains(entrada)){
                entradasVendidas.add(entrada)
                entradasDisponibles.remove(entrada)
                entrada.vender()
            } else {
                throw StockException("La entrada no es de esta función")
            }
        } else {
            throw StockException("Se acabó el stock de entradas para esta función")
        }
    }

    fun actualizarEntradas() {
        val nuevasVendidas = entradasDisponibles.filter { it.estaVendida }.toMutableList()
        entradasVendidas.addAll(nuevasVendidas)
        entradasDisponibles = entradasDisponibles.filter { !it.estaVendida }.toMutableList()
    }
    /*Aca se hace la suma del precio final y llama a la clase entrada*/
    fun dineroRecaudado(): Double {
        return entradasVendidas.sumOf { it.precioFinal() }
    }

    fun funcionAgotada():Boolean{
        return entradasDisponibles.isEmpty()
    }

    fun entradasVendidasTotales(): Int {
        return entradasVendidas.size
    }

    fun entradasVendidasPorUbicacion(ubicacion: Ubicacion): Int {
        return entradasVendidas.filter { it.ubicacion == ubicacion }.size
    }
}
