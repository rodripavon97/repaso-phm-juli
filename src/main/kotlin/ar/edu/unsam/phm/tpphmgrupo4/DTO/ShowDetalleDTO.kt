package ar.edu.unsam.phm.tpphmgrupo4.DTO

import ar.edu.unsam.phm.tpphmgrupo4.*
import java.time.LocalDate
import java.time.LocalTime

class ShowDetalleDTO {
    var id: Int = 0
    lateinit var fotoArtista: String
    lateinit var nombreBanda: String
    lateinit var nombreRecital: String
    lateinit var ubicacion: String
    lateinit var fecha: List<LocalDate>
    lateinit var hora:List<LocalTime>
    var puntaje: Double? = null
    lateinit var comentarios: List<Comentario>
    lateinit var souldOut: Number
    var allSouldOut: Boolean = false
    var funcionesDisponibles : Boolean = false
    lateinit var usuarioEnEspera: Number
    var totalRecaudado: Double? = null
    var totalCosto: Double? = null
    lateinit var entradasVendidasTotales: Number
    lateinit var ubicacionMapa: UbicacionMapa
    lateinit var comentariosLista: List<Comentario>
    lateinit var personasEnEspera: Number
    lateinit var ubicacionCosto: Map<Ubicacion, Int>
    lateinit var entradasVendidasPorUbicacion: Map<Ubicacion, Int>


    companion object {
        fun fromShowDetalle(show: Show): ShowDetalleDTO = ShowDetalleDTO().also {
            it.id = show.id
            it.fotoArtista = show.imagen
            it.nombreBanda = show.nombreBanda
            it.nombreRecital = show.nombreRecital
            it.ubicacion = show.instalacion.nombre
            it.fecha = show.funciones.map { it.fecha }
            it.hora = show.funciones.map { it.hora }
            it.puntaje = if (show.puntajePromedio().isNaN()) 0.0 else show.puntajePromedio()
            it.comentarios = show.comentarios
            it.souldOut = show.cantidadDeFuncionesSoldOut()
            it.allSouldOut = show.showSoldOut()
            it.funcionesDisponibles = show.noHayFuncionesDisponibles()
            it.usuarioEnEspera = show.cantidadUsuariosEnEspera()
            it.totalRecaudado = show.mondoDeIngresos()
            it.totalCosto = show.costoShow()
            it.entradasVendidasTotales = show.entradasTotaleVendidas()
            it.ubicacionMapa = show.instalacion.ubicacion
            it.comentariosLista = show.listaDeComentarios()
            it.personasEnEspera = show.cantidadUsuariosEnEspera()
            it.ubicacionCosto = show.instalacion.obtenerCostosUbicaciones()
            it.entradasVendidasPorUbicacion = obtenerEntradasVendidasPorUbicacion(show)

        }

        private fun obtenerEntradasVendidasPorUbicacion(show: Show): Map<Ubicacion, Int> {
            val entradasPorUbicacion = mutableMapOf<Ubicacion, Int>()
            for (funcion in show.funciones) {
                for (entrada in funcion.entradasVendidas) {
                    val ubicacion = entrada.ubicacion
                    entradasPorUbicacion[ubicacion] = entradasPorUbicacion.getOrDefault(ubicacion, 0) + 1
                }
            }
            return entradasPorUbicacion
        }
    }
}