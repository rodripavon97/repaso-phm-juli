package ar.edu.unsam.phm.tpphmgrupo4.service

import ar.edu.unsam.phm.tpphmgrupo4.*
import ar.edu.unsam.phm.tpphmgrupo4.DTO.AdminDTO
import ar.edu.unsam.phm.tpphmgrupo4.DTO.LoginDTO
import ar.edu.unsam.phm.tpphmgrupo4.DTO.UsuarioAmigosDTO
import ar.edu.unsam.phm.tpphmgrupo4.DTO.UsuarioDTO
import ar.edu.unsam.phm.tpphmgrupo4.Exceptions.NotFoundException
import ar.edu.unsam.phm.tpphmgrupo4.repositorio.Repositorio
import ar.edu.unsam.phm.tpphmgrupo4.repositorio.RepositorioEntrada
import ar.edu.unsam.phm.tpphmgrupo4.repositorio.RepositorioShow
import ar.edu.unsam.phm.tpphmgrupo4.repositorio.RepositorioUsuario
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UsuarioService {
    @Autowired
    lateinit var repoUsuario: RepositorioUsuario
    @Autowired
    lateinit var repoShow: RepositorioShow
    @Autowired
    lateinit var repoEntrada: RepositorioEntrada


    fun loginUsuario(user: LoginDTO): Any {
        val userLogin = repoUsuario.findUserByUsernameAndPassword(user.username, user.password)

        return if (userLogin?.esAdmin == true) {
            AdminDTO.fromAdminDTO(userLogin as UsuarioAdmin)
        } else {
            UsuarioDTO.fromUsuario(userLogin as UsuarioComun)
        }
    }

    fun getUserByID(id: Int): Usuario {
        return repoUsuario.getById(id)
    }

    fun getDataUserByID(id: Int): Any {
        val usuario = getUserByID(id)
        return when(usuario){
            is UsuarioComun -> UsuarioDTO.fromUsuario(usuario)
            is UsuarioAdmin -> AdminDTO.fromAdminDTO(usuario)
            else -> throw  NotFoundException("Usuario no encontrado")
        }
    }

    fun editarDatos (idUsuario: Int, nombre: String, apellido: String){
        val usuario = getUserByID(idUsuario) as UsuarioComun
        usuario.cambiarNombres(nombre, apellido)
    }

    fun amigosQueVanAShow (idUsuario : Int, idShow: Int) : List<UsuarioAmigosDTO> {
        val usuario = getUserByID(idUsuario) as UsuarioComun
        val amigos = usuario.amigos.map { getUserByID(it) as UsuarioComun }
        val amigosFiltrados = amigos.filter { it.listaIdShows().contains(idShow) }
        return amigosFiltrados.map { UsuarioAmigosDTO.fromAmigosDTO(it) }
    }

    fun carritoCompra (idUsuario : Int) : List<Entrada>{
        val usuario = getUserByID(idUsuario) as UsuarioComun
        return usuario.carrito
    }

    fun vaciarCarrito(idUsuario: Int) {
        val usuario = repoUsuario.getById(idUsuario) as UsuarioComun
        usuario.carrito.clear()
        repoUsuario.update(usuario)
    }

    fun agregarCarrito(idUsuario: Int, idShow: Int, idFuncion: Int, cantidad: Int, ubi: Ubicacion) {
        val usuario = getUserByID(idUsuario) as UsuarioComun
        val show = repoShow.getById(idShow)
        val funcion = show.funciones[idFuncion]
        usuario.agregarEntradasACarrito(funcion, cantidad, ubi)
        repoUsuario.update(usuario)
    }
    fun comprarEntradas(idUsuario: Int) {
        val usuario = getUserByID(idUsuario) as UsuarioComun
        val entradas = usuario.carrito
        val show = repoShow.getById(entradas.first().showId)
        usuario.comprarEntradas()
        entradas.forEach {
            repoShow.getById(it.showId).actualizarEntradas()
            repoShow.update(show)
        }
        repoUsuario.update(usuario)
    }

    fun sumarCredito (idUsuario: Int, credito: Double){
        val usuario = getUserByID(idUsuario) as UsuarioComun
        usuario.aumentarSaldo(credito)
    }

    fun entradasCompradas (idUsuario: Int) : List<Entrada> {
        val usuario = repoUsuario.getById(idUsuario) as UsuarioComun
        return usuario.entradasCompradas
    }

    fun listaAmigos (idUsuario: Int) : List<Usuario> {
        val usuario = repoUsuario.getById(idUsuario) as UsuarioComun
        return usuario.amigos.map { repoUsuario.getById(it) }
    }

    fun listaComentarios (idUsuario: Int) : List<Comentario> {
        val usuario = repoUsuario.getById(idUsuario) as UsuarioComun
        return usuario.comentarios
    }

    fun quitarAmigo (idUsuario: Int, idAmigo: Int){
        val usuario = getUserByID(idUsuario) as UsuarioComun
        usuario.quitarAmigo(idAmigo)
    }

    fun dejarComentario (idUsuario: Int, idShow: Int, idEntrada: Int, comentario: String, puntuacion: Float) {
        val usuario = getUserByID(idUsuario) as UsuarioComun
        val show = repoShow.getById(idShow)
        usuario.dejarComentario(show, comentario, puntuacion)
    }

    fun borrarComentario (idUsuario: Int, idShow: Int, idComentario: Int) {
        val usuario = getUserByID(idUsuario) as UsuarioComun
        val show = repoShow.getById(idShow)
        usuario.borrarComentario(idComentario)
        show.borrarComentario(idComentario)
    }
}

