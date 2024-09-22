package ar.edu.unsam.phm.tpphmgrupo4.DTO


import ar.edu.unsam.phm.tpphmgrupo4.Estado
import ar.edu.unsam.phm.tpphmgrupo4.Funcion
import ar.edu.unsam.phm.tpphmgrupo4.Show
import java.time.LocalDate
import java.time.LocalTime

class FuncionDTO {
    var idShow: Int = 0
    var id: Int = 0
    lateinit var fecha: LocalDate
    lateinit var hora: LocalTime
    var precioBaseEntrada: Double = 0.0
    lateinit var estado: Estado
    var numeroEntradasTotales: Int = 0


    companion object {
        fun fromShow(funcion: Funcion,show: Show): FuncionDTO = FuncionDTO().also {
            it.id = funcion.id
            it.fecha = funcion.fecha
            it.hora = funcion.hora
            it.precioBaseEntrada = funcion.precioBaseEntrada
            it.estado = when (funcion.showEstado) {
                Estado.PrecioBase -> Estado.PrecioBase
                Estado.VentaPlena -> Estado.VentaPlena
                Estado.Megashow -> Estado.Megashow
            }
            it.numeroEntradasTotales = funcion.numeroEntradasTotales
            it.idShow =show.id


        }
    }
}