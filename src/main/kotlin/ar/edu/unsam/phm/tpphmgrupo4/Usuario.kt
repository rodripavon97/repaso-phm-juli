package ar.edu.unsam.phm.tpphmgrupo4

import ar.edu.unsam.phm.tpphmgrupo4.Exceptions.ComentarioExistenteException
import ar.edu.unsam.phm.tpphmgrupo4.Exceptions.ComentarioSinEntradaException
import ar.edu.unsam.phm.tpphmgrupo4.Exceptions.ComentarioTempranoException
import ar.edu.unsam.phm.tpphmgrupo4.Exceptions.SaldoException
import ar.edu.unsam.phm.tpphmgrupo4.Exceptions.SaldoValidationException
import ar.edu.unsam.phm.tpphmgrupo4.Exceptions.UnauthorizedEditData
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period

/**
El sistema va a tener dos roles: usuario comprador y administrador, dependiendo del rol van a poder ver distintas pantallas.
Aca se puede pensar como una herencia, porque definis roles y para no repetir codigo
 se crea una clase padre usuario con dichos datos, hay un flag (estado que dice si es adm o no)*/
/* mas o menos se entiende ahi*/
/*
La extension Entity es donde sale el repo en memoria, por eso esta un extension del id
*/

abstract class Usuario : Entity {
    abstract var nombre: String
    abstract var apellido: String
    abstract var fechaDeNacimiento: LocalDate
    abstract var fotoPerfil: String
    abstract val esAdmin: Boolean
    abstract var username: String
    abstract var password: String
    override var id: Int = Entity.idInicial

    fun calcularIngresosFuturos(show: Show): Double {
        return show.funciones.size * show.costoBanda
    }
}

/**
 * en usuario comun llamas a las propiedades padre de usuario y agregas las propiedades especificas del usuario
 */

class UsuarioComun(
    override var nombre: String, override var apellido: String,
    override var fechaDeNacimiento: LocalDate, override var username: String,
    override var password: String, override var fotoPerfil: String,
) : Usuario() {
    override val esAdmin = false
    var edad = Period.between(fechaDeNacimiento, LocalDate.now()).years
    var dni: Int = 0
    var saldo: Double = 0.0
    /* La lista de amigos se penso como una lista de amigo, la idea aca es que usando su id devuelva su info*/
    var amigos: MutableList<Int> = mutableListOf()
    /* El carrito en el tp 0 lo pensamos como una lista de entradas, aca se podria pensar en una clase Carrito (en
    el ultimo tp lo arreglamos :v  entonces seria la  clase carrito y ahi irian las entradas compradas, no?
    exactamente, pero lo dejamos para el ultimo tp, aca directamente mandas una lista de entradas
    voy entendiendoo, despoues leo la ultima version tambien
    Dale juli. despues del tp 0, en el 1 se elimina el repo en memoria
    aaah no sabia, genial
     */
    var carrito: MutableList<Entrada> = mutableListOf()
    var entradasCompradas: MutableList<Entrada> = mutableListOf()
    /*
    * en el enunciado dice que se puede hacer y dejar comentarios una solucion fue crear una clase Comentario*/
    var comentarios: MutableList<Comentario> = mutableListOf()


    fun cambiarNombres(nombre: String, apellido: String) {
        if (nombre.isNotEmpty() && apellido.isNotEmpty()) {
            this.nombre = nombre
            this.apellido = apellido
        } else {
            throw UnauthorizedEditData("No puede dejar vacio el campo nombre y/o apellido ")
        }

    }
    fun aumentarSaldo(monto: Double) {
        if (monto > 0.0) {
            saldo += monto
        } else {
            throw SaldoValidationException("El saldo no puede ser negativo o 0")
        }
    }

    fun disminuirSaldo(monto: Double) {
        saldo -= monto
    }
    fun agregarAmigo(amigoId: Int) {
        amigos.add(amigoId)
    }

    fun quitarAmigo(amigoId: Int) {
        amigos.remove(amigoId)
    }

    /*
    * Se crea un metodo para agregar al carrito en usuario ya que el carrito es una responsabilidad del usuario
    * ahora tiene sentodo jajajaja no pasa nada
    * te sigo
    * me perdi un poco
    * en que parte?
    * le erre a la escritura xd
    * */
   /* Ubicacion aca es un enum que tiene esa informacion de cada ubicacion*/
    fun agregarEntradasACarrito(funcion: Funcion, cantidad: Int, ubi: Ubicacion) {
        require(cantidad > 0) { "La cantidad debe ser mayor que cero" }
        for (i in 1..cantidad) {
            val entrada = funcion.entradasDisponibles.filter { it.ubicacion == ubi }.first()
            carrito.add(entrada)
        }
    }

    fun limpiarCarrito() {
        carrito.clear()
    }


    fun totalCarrito(): Double {
        return carrito.sumOf { it.precioFinal() }
    }

    fun entradasEnCarrito(): Int {
        return carrito.size
    }

    fun comprarEntradas() {
        if (saldo >= totalCarrito()) {
            carrito.forEach { it.vender() }
            disminuirSaldo(totalCarrito())
            entradasCompradas.addAll(carrito)
            limpiarCarrito()
        } else {
            throw SaldoException("El saldo es insuficiente para comprar esta(s) entrada(s)")
        }
    }

    fun listaIdShows(): List<Int> {
        return entradasCompradas.map { it.showId }
    }



    fun dejarComentario(show: Show, contenido: String, puntuacion: Float) {
        val funcionesEntradasId = entradasCompradas.map { it.funcionId }
        val funcionesShow = show.funciones
        val funcionEnComun = funcionesShow.filter { funcionesEntradasId.contains(it.id) }
        if (funcionEnComun.isNotEmpty()) {
            if (funcionEnComun[0].fecha.isBefore(LocalDate.now())) {
                if (this.comentarios.all { it.idShow != show.id }){
                    val comentarioCompleto = Comentario(
                        show.id,
                        this.fotoPerfil,
                        show.imagen,
                        this.username,
                        show.nombreBanda,
                        LocalDate.now(),
                        contenido,
                        puntuacion
                    )
                    show.agregarComentario(comentarioCompleto)
                    comentarios.add(comentarioCompleto)
                } else {
                    throw ComentarioExistenteException("Ya se ha dejado un comentario en este show")
                }
            } else {
                throw ComentarioTempranoException("Este show todavía no ocurrió")
            }
        } else {
            throw ComentarioSinEntradaException("El usuario no tiene entradas para este show")
        }
    }

    fun borrarComentario(idComentario: Int) {
        comentarios.remove(comentarios[idComentario])
    }
}

class UsuarioAdmin(
    override var nombre: String,
    override var apellido: String,
    override var fechaDeNacimiento: LocalDate,
    override var username: String,
    override var password: String,
    override var fotoPerfil: String,
) : Usuario() {

    override var esAdmin = true
fun agregarFuncion(show: Show, fecha: LocalDate, hora: LocalTime) {
    if (show.showSoldOut()) {
        val precioBase = show.precioBaseEntrada()
        val ingresosPotenciales = precioBase * show.instalacion.totalCapacidad()

        val costoShow = show.costoShow()

        if (ingresosPotenciales > costoShow) {
            show.agregarFuncion(fecha, hora)
        } else {
            throw RuntimeException("Crear una nueva función no es redituable en este momento")
        }
    } else {
        throw RuntimeException("Todavía quedan entradas para este show")
    }
}
}