package ar.edu.unsam.phm.tpphmgrupo4.Controller

import ar.edu.unsam.phm.tpphmgrupo4.*
import ar.edu.unsam.phm.tpphmgrupo4.DTO.*

import ar.edu.unsam.phm.tpphmgrupo4.service.UsuarioService
import ar.edu.unsam.phm.tpphmgrupo4.service.showService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin("*")
class UsuarioController {
    @Autowired lateinit var serviceUsuario : UsuarioService
    @Autowired lateinit var serviceShow : showService

    @PostMapping("/usuario-logueado")
    @Operation(summary = "Trae el DTO del usuario logueado")
    fun traerUsuarioLogin(@RequestBody user: LoginDTO): Any {
        val usuario = serviceUsuario.loginUsuario(user)
        return when (usuario) {
            is UsuarioDTO -> usuario
            is AdminDTO -> usuario
            else -> throw IllegalStateException("Tipo de usuario no reconocido")
        }
    }

    @GetMapping("/user/{idUser}")
    @Operation(summary = "Trae los datos del usuario por su id")
    fun getUsuarioComunByID(@PathVariable idUser: Int): Any{
        return serviceUsuario.getDataUserByID(idUser)
    }


    @GetMapping("/carrito/{idUser}")
    @Operation(summary = "Trae el carrito de usuario por ID")
    fun getCarrito (@PathVariable idUser : Int) : MutableList<CarritoDTO> {
        return serviceUsuario.carritoCompra(idUser).map {
            entrada : Entrada -> CarritoDTO.toEntradaCarritoDTO(
                entrada,
                serviceShow.getShowByID(entrada.showId),
                serviceUsuario.getUserByID(idUser) as UsuarioComun,
                serviceUsuario.amigosQueVanAShow(idUser, entrada.showId)
            )
        }.toMutableList()
    }

    @PostMapping("/agregar-carrito/{idUser}/{idShow}/{idEntrada}/{cantidad}/{ubi}")
    @Operation(summary = "Agrega entradas al carrito de un usuario")
    fun agregarAlCarrito(@PathVariable idUser: Int, @PathVariable idShow: Int,@PathVariable idFuncion: Int,@PathVariable cantidad: Int,@PathVariable ubicacion: Ubicacion,) {
        serviceUsuario.agregarCarrito(idUser, idShow, idFuncion, cantidad, ubicacion)
    }

    @PatchMapping("/editar-datos-usuario/{idUser}")
    @Operation(summary = "Cambia el nombre y apellido del usuario")
    fun editarDatosUsuario (@PathVariable idUser : Int, @RequestBody user: UsuarioEditarDTO) {
        serviceUsuario.editarDatos(idUser, user.nombre, user.apellido)
    }

    @PatchMapping("/sumar-credito/{idUser}/{credito}")
    @Operation(summary = "Añade creditos para el usuario")
    fun sumarCredito (@PathVariable idUser : Int, @PathVariable credito : Double) {
        serviceUsuario.sumarCredito(idUser, credito)
    }

    @PutMapping("/quitar-amigo/{idUser}/{idAmigo}")
    @Operation(summary = "Añade creditos para el usuario")
    fun quitarAmigo (@PathVariable idUser : Int, @PathVariable idAmigo : Int) {
        serviceUsuario.quitarAmigo(idUser, idAmigo)
    }

    @GetMapping("/entradas-compradas/{idUser}")
    @Operation(summary = "Trae las entradas del usuario por ID")
    fun getEntradasCompradas (@PathVariable idUser : Int) : MutableList<CarritoDTO> {
        return serviceUsuario.entradasCompradas(idUser).map {
            entrada : Entrada -> CarritoDTO.toEntradaCarritoDTO(
                entrada,
                serviceShow.getShowByID(entrada.showId),
                serviceUsuario.getUserByID(idUser) as UsuarioComun,
                serviceUsuario.amigosQueVanAShow(idUser, entrada.showId)
            )
        }.toMutableList()
    }

    @PostMapping("/comprar-entradas/{idUser}")
    @Operation(summary = "Compra las entradas del carrito para el usuario")
    fun comprarEntradas(@PathVariable idUser: Int) {
        serviceUsuario.comprarEntradas(idUser)
    }

    @GetMapping("/lista-amigos/{idUser}")
    @Operation(summary = "Trae los amigos del usuario por ID")
    fun getAmigos (@PathVariable idUser : Int) : MutableList<UsuarioAmigosDTO> {
        return serviceUsuario.listaAmigos(idUser).map { amigo : Usuario -> UsuarioAmigosDTO.fromAmigosDTO(amigo as UsuarioComun)}.toMutableList()
    }

    @GetMapping("/lista-comentarios/{idUser}")
    @Operation(summary = "Trae los comentarios del usuario por ID")
    fun getComentarios (@PathVariable idUser : Int) : MutableList<ComentarioDTO> {
        return serviceUsuario.listaComentarios(idUser).map { comentario : Comentario -> ComentarioDTO.fromComentario(comentario)}.toMutableList()
    }

    @DeleteMapping("/carrito-vacio/{idUser}")
    @Operation(summary = "limpia el carrito del usuario por ID")
    fun removeCarrito(@PathVariable idUser: Int) {
        return serviceUsuario.vaciarCarrito(idUser)
    }

    @PostMapping("/dejar-comentario/{idUser}/{idShow}/{idEntrada}")
    @Operation(summary = "Deja un comentario en el show indicado")
    fun dejarComentario(@PathVariable idUser: Int, @PathVariable idShow: Int, @PathVariable idEntrada: Int, @RequestBody comentario : ComentarioNuevoDTO) {
        return serviceUsuario.dejarComentario(idUser, idShow, idEntrada, comentario.contenido, comentario.puntuacion!!)
    }

    @DeleteMapping("/borrar-comentario/{idUser}/{idShow}/{idComentario}")
    @Operation(summary = "elimina un comentario")
    fun borrarComentario(@PathVariable idUser: Int, @PathVariable idShow: Int, @PathVariable idComentario: Int) {
        return serviceUsuario.borrarComentario(idUser, idShow, idComentario)
    }
}