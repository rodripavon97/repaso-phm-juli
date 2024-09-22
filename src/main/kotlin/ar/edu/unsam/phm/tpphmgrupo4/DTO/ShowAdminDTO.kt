package ar.edu.unsam.phm.tpphmgrupo4.DTO

import ar.edu.unsam.phm.tpphmgrupo4.Entrada
import ar.edu.unsam.phm.tpphmgrupo4.Show
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.roundToInt

class ShowAdminDTO {
    var id: Int = 0
    lateinit var imagen: String
    lateinit var nombreBanda: String
    lateinit var nombreRecital: String
    lateinit var ubicacion: String
    lateinit var fecha: List<LocalDate>
    lateinit var hora:List<LocalTime>
    var precioLocacionBarata: Double = 0.0
    var precioLocacionCara: Double = 0.0
    lateinit var souldOut: Number
    var rentabilidad: Double = 0.0
    lateinit var personasEnEspera: Number
    var ventas: Double = 0.0
    var puedeAgregarFuncion: Boolean = false


    companion object {
        fun fromshowAdminDTO( show : Show )  : ShowAdminDTO = ShowAdminDTO().also {
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
            it.souldOut= show.cantidadDeFuncionesSoldOut()
            it.rentabilidad = show.rentabilidad()
            it.personasEnEspera = show.cantidadUsuariosEnEspera()
            it.ventas = show.mondoDeIngresos()
            it.puedeAgregarFuncion = show.puedeAgregarNuevaFuncion()
        }
    }
}