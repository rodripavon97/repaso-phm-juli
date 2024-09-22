package ar.edu.unsam.phm.tpphmgrupo4.DTO

import ar.edu.unsam.phm.tpphmgrupo4.Entrada
import ar.edu.unsam.phm.tpphmgrupo4.Show
import ar.edu.unsam.phm.tpphmgrupo4.UsuarioComun
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.roundToInt

class ShowDTO {
    var id: Int = 0
    lateinit var imagen: String
    lateinit var nombreBanda: String
    lateinit var nombreRecital: String
    lateinit var ubicacion: String
    lateinit var fecha: List<LocalDate>
    lateinit var hora:List<LocalTime>
    var precioLocacionBarata: Double = 0.0
    var precioLocacionCara: Double = 0.0
    lateinit var amigosQueVanAlShow: List<UsuarioAmigosDTO>
    var puntaje: Double? = null
    var comentariosTotales: Int = 0
    var venta : Double = 0.0
    companion object {
        fun fromShow(show: Show, listaAmigosDTO: List<UsuarioAmigosDTO>): ShowDTO = ShowDTO().also {
            it.id = show.id
            it.imagen = show.imagen
            it.nombreBanda = show.nombreBanda
            it.nombreRecital = show.nombreRecital
            it.ubicacion = show.instalacion.nombre
            it.fecha = show.funciones.map { it.fecha }
            it.hora = show.funciones.map { it.hora }
            val entradaBarata = Entrada(
                1, show.instalacion.ubicacionMenor(), LocalDate.now(),
                show.precioBaseEntrada(), show.showEstado, show.funciones[0].id, show.id
            )
            val entradaCara = Entrada(
                1, show.instalacion.ubicacionMayor(), LocalDate.now(),
                show.precioBaseEntrada(), show.showEstado, show.funciones[0].id, show.id
            )
            it.precioLocacionBarata = entradaBarata.precioFinal().roundToInt().toDouble()
            it.precioLocacionCara = entradaCara.precioFinal().roundToInt().toDouble()
            it.amigosQueVanAlShow = listaAmigosDTO
            it.puntaje = if (show.puntajePromedio().isNaN()) 0.0 else show.puntajePromedio()
            it.comentariosTotales = show.cantidadDeComentarios().toDouble().roundToInt()
            it.venta = show.mondoDeIngresos()
            println(it.venta)
        }
    }
}