package ar.edu.unsam.phm.tpphmgrupo4
import ar.edu.unsam.phm.tpphmgrupo4.DTO.UsuarioAmigosDTO
import java.time.LocalDate
import java.time.LocalTime

/*
* De los shows se conoce:
Nombre de la banda
La instalación donde se realiza
Nombre del recital (o gira)
El costo de un show que es el costo de la instalación (estadio o teatro) + el monto que cobra la banda.
*/

/*
*
* en este caso creamos lo que necesita el show con sus parametros en la clase, imagen se agrego ya que en el front se necesita la imgen
* Instalacion se instancia en una clase que se crea ya que puede ser un estadio o teatro
* las funciones es una lista mutable de la clase funcion
* el show sabe que comentario se recibio
* y el usuarioEnEspera llama a una lista de usuario comun
*
* voy bien, lo del entity lo entendiste bien? es el mismo que se usa en algo 3 masoo, me ppodrias explicar?
* justo estan los chicos viendo eso en algo 3 me sirve de repaso
* bieen
* como vamos hasta ahora juli?
*
*
* por ahora bieen, es  un poco mucho todo junto pero le voy agarrando el  hilo
* tranqui asi estuvimos al empezar con el tp
* me imaginoo, voy bienb por ahora entonces,
* te explico la clase funcion y maniana vemos el resto, me cayeron visitas a casa
* te creo un repo privado en git
* */

/*el estado del show viene de un enum Estado, que tiene los 3 estados que pide el enunciado*/
class Show(
    var nombreBanda: String,
    var nombreRecital: String,
    var imagen: String,
    var costoBanda: Double,
    var instalacion: Instalacion,
    var showEstado: Estado = Estado.PrecioBase,
    var funciones: MutableList<Funcion>,
) : Entity{
    val comentarios = mutableListOf<Comentario>()
    val usuariosEnEspera = mutableListOf<UsuarioComun>()
    override var id: Int = Entity.idInicial
    var totalIngresos: Double = 0.0

    /*aca se hace el calculo del costo del show*/
    fun costoShow(): Double {
        return costoBanda + instalacion.costoFijo()
    }

    fun limpiarEntradasDisponiblesParaTodasLasFunciones() {
        funciones.forEach { it.limpiarEntradasDisponibles() }
    }

    /*el calculo del precio base viene de aca
    *
    *  El costo de cada entrada se calcula como el costo fijo de la locación y de la banda (varía para cada show), todo esto dividido la cantidad de plazas totales para acceder al concierto.*/
    fun precioBaseEntrada() : Double {
        return costoShow() / instalacion.totalCapacidad()
    }

    fun agregarFuncion(fecha: LocalDate, hora: LocalTime) {
        funciones.add(Funcion(fecha, hora, precioBaseEntrada(), showEstado, instalacion.totalCapacidad()))
    }

    /*Aca se agrega las entradas a una funcion usando su ubicacion y funcion
    * verifica si la funciones tiene una funcion, llama al metodo funcion agrega entrada pasando la ubicacion y su id*/
    fun agregarEntrada(funcion: Funcion, ubicacion: Ubicacion) {
        if (funciones.contains(funcion)) {
            funcion.agregarEntrada(ubicacion, this.id)
        }
    }

    /* el cambiar estado paso a ser responsabilidad de la funcion, antes lo pensabamos que el show lo hacia*/
    fun cambiarEstado(estado: Estado) {
        showEstado = estado
        funciones.forEach { it.cambiarEstado(estado) }
    }

    fun estaAbierto() : Boolean {
        return funciones.any { it.fecha.isAfter(LocalDate.now()) }
    }

    /* en este caso se agrega un metodo para hacer comentario, este despues se usa en el service para cuando un usuario hace un comentario al show*/
    fun agregarComentario(comentario: Comentario) {
        comentarios.add(comentario)
    }

    fun borrarComentario(idComentario: Int) {
        comentarios.remove(comentarios[idComentario])
    }

    /*Aca el metodo puntaje promedio mapea los puntajes de los comentarios y con el metodo average calcula el promedio de puntaje
    * el average lo puso alan, yo no sabia en su momento de ese metodo :v*/
    fun puntajePromedio(): Double {
        val puntajes = comentarios.map { it.puntuacion }
        return puntajes.average()
    }

    fun cantidadDeComentarios(): Int {
        return comentarios.count()
    }

    fun listaDeComentarios(): List<Comentario>{
        return comentarios
    }


    fun mondoDeIngresos(): Double{
        actualizarEntradas()
        totalIngresos = funciones.sumOf { it.dineroRecaudado() }
        return totalIngresos
    }

    fun entradasTotaleVendidas(): Int{
        return funciones.sumOf { it.entradasVendidasTotales() }
    }

    fun cantidadDeFuncionesSoldOut():Int{
        return funciones.count { it.funcionAgotada() }
    }

    fun actualizarEntradas() {
        funciones.forEach { it.actualizarEntradas() }
    }

    fun noHayFuncionesDisponibles():Boolean{
        return cantidadDeFunciones() == cantidadDeFuncionesSoldOut()
    }

    fun cantidadDeFunciones():Int{
        return funciones.size
    }

    fun showSoldOut():Boolean{
        return funciones.all { it.funcionAgotada() }
    }

    fun setearFuncionesAgotadas(): Boolean {
        return !showSoldOut()
    }

    fun agregarUsuarioEnEspera(user:UsuarioComun){
        if (showSoldOut()) {
            usuariosEnEspera.add(user)
        }
    }

    fun sumarUsuarioEnEspera(): Int {
        return usuariosEnEspera.size + 1
    }

    fun cantidadUsuariosEnEspera(): Int {
        return usuariosEnEspera.size
    }

    fun rentabilidad(): Double {
        return  (((costoShow() - mondoDeIngresos()) / mondoDeIngresos()))* 100
    }

    fun cambiarNombres(nombreBanda: String, nombreRecital: String) {
        this.nombreBanda = nombreBanda
        this.nombreRecital = nombreRecital
    }
    fun puedeAgregarNuevaFuncion():Boolean = this.showSoldOut() && this.cantidadUsuariosEnEspera() > 0

}
