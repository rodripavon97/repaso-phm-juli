package ar.edu.unsam.phm.tpphmgrupo4.DTO

import ar.edu.unsam.phm.tpphmgrupo4.Entrada
import ar.edu.unsam.phm.tpphmgrupo4.Show
import ar.edu.unsam.phm.tpphmgrupo4.UsuarioComun
import java.time.LocalDate
import kotlin.math.roundToInt

class CarritoDTO {
    var id :Int?=null
    var idShow :Int?=null
    var precioEntrada: Double ?= null
    var precioTotalCarrito: Double = 0.0
    lateinit var imagen: String
    lateinit var nombreBanda: String
    lateinit var nombreRecital: String
    lateinit var ubicacion: String
    lateinit var fecha: List<LocalDate>
    var puntaje: Double? = null
    var comentariosTotales: Int = 0
    var sizeCarrito : Int ?= 0
    lateinit var amigosQueVanAlShow: List<UsuarioAmigosDTO>
    var estaAbierto : Boolean ?= true

    companion object {
        fun toEntradaCarritoDTO(entrada: Entrada, show: Show, usuarioComun: UsuarioComun, listaAmigosDTO: List<UsuarioAmigosDTO>): CarritoDTO = CarritoDTO().also {
            it.id = entrada.id
            it.idShow = show.id
            it.imagen = show.imagen
            it.nombreBanda = show.nombreBanda
            it.nombreRecital = show.nombreRecital
            it.ubicacion = show.instalacion.nombre
            it.fecha = show.funciones.map { it.fecha }
            it.puntaje = if (show.puntajePromedio().isNaN()) 0.0 else show.puntajePromedio()
            it.comentariosTotales = show.cantidadDeComentarios()
            it.precioEntrada = entrada.precioFinal().roundToInt().toDouble()
            it.precioTotalCarrito = usuarioComun.totalCarrito()
            it.sizeCarrito = usuarioComun.entradasEnCarrito()
            it.amigosQueVanAlShow = listaAmigosDTO
            it.estaAbierto = show.estaAbierto()
        }
    }


}