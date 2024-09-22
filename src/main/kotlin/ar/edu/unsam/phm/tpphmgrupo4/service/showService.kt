package ar.edu.unsam.phm.tpphmgrupo4.service

import ar.edu.unsam.phm.tpphmgrupo4.*
import ar.edu.unsam.phm.tpphmgrupo4.DTO.FuncionDTO
import ar.edu.unsam.phm.tpphmgrupo4.DTO.UsuarioAmigosDTO
import ar.edu.unsam.phm.tpphmgrupo4.Exceptions.NotFoundException

import ar.edu.unsam.phm.tpphmgrupo4.repositorio.Repositorio
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class showService {


    @Autowired lateinit var  showRepositorio: Repositorio<Show>
    @Autowired lateinit var  funcionRepositorio: Repositorio<Funcion>
    @Autowired lateinit var  usuarioRepositorio: Repositorio<Usuario>


    fun getShowByID(id: Int): Show {
        return showRepositorio.getById(id)
    }

    fun getAllShows(): List<Show> {
        val shows = showRepositorio.getAll().toList()
        return shows.filter { show ->
            show.estaAbierto()
        }
    }


    fun filterAmigos(idUsuario: Int?, show: Show): List<UsuarioAmigosDTO> {
        if (idUsuario != null) {
            val usuario = usuarioRepositorio.getById(idUsuario) as UsuarioComun
            val amigos = usuario.amigos.map { usuarioRepositorio.getById(it) as UsuarioComun }
                .filter { it.listaIdShows().contains(show.id) }
            return amigos.map { UsuarioAmigosDTO.fromAmigosDTO(it) }
        } else {
            return emptyList()
        }
    }

    fun getAllShowAdmin ( idUsuario: Int?,artista: String?,locacion: String?) : List<Show>{
        var getShows = getAllShows()
        if (idUsuario != null) {
            usuarioRepositorio.getById(idUsuario) as? UsuarioAdmin
        }
        if (artista != null) {
            getShows = getShows.filter { it.nombreBanda.contains(artista, ignoreCase = true) }
        }
        if (locacion != null) {
            getShows = getShows.filter { it.instalacion.nombre.contains(locacion, ignoreCase = true) }
        }
        return getShows
    }

    fun getAllShowWithFilters(
        idUsuario: Int?,
        artista: String?,
        locacion: String?,
        conAmigos: Boolean? = null
    ): List<Show> {
        var getShows = getAllShows()
        if (idUsuario != null) {
            val user = usuarioRepositorio.getById(idUsuario) as? UsuarioComun
            if (user != null && conAmigos != null) {
                getShows = if (conAmigos) {
                    getShows.filter { show ->
                        filterAmigos(idUsuario, show).isNotEmpty()
                    }
                } else {
                    getShows.filter { show ->
                        filterAmigos(idUsuario, show).isEmpty()
                    }
                }
            }
        }
        if (artista != null) {
            getShows = getShows.filter { it.nombreBanda.contains(artista, ignoreCase = true) }
        }
        if (locacion != null) {
            getShows = getShows.filter { it.instalacion.nombre.contains(locacion, ignoreCase = true) }
        }
        if (idUsuario == null && artista == null && locacion == null && conAmigos == null) {
            return getShows
        }
        return getShows
    }

    fun navegarShowDetallePorId(idShow: Int): Show {
        return if (idShow != null) {
            showRepositorio.getById(idShow)
        } else {
            throw NotFoundException("no existe show con ese id")
        }
    }

    fun agragarUsuarioAEspera(idShow: Int, idUsuario: Int): UsuarioComun {
        val show = showRepositorio.getById(idShow)
        val usuario = usuarioRepositorio.getById(idUsuario) as UsuarioComun
        show.agregarUsuarioEnEspera(usuario)
        showRepositorio.update(show)
        return usuario
    }

    fun getUsuariosEnEspera(idShow: Int): List<UsuarioComun> {
        val show = showRepositorio.getById(idShow)
        return show?.usuariosEnEspera ?: emptyList()
    }

    fun crearFuncion(idShow: Int, dto: FuncionDTO): Funcion {
        val nuevaFuncion = Funcion(
            dto.fecha, dto.hora, dto.precioBaseEntrada, dto.estado, dto.numeroEntradasTotales
        )
        val show = showRepositorio.getById(idShow)
        funcionRepositorio.create(nuevaFuncion)
        show.agregarFuncion(dto.fecha,dto.hora)
        showRepositorio.update(show)
        return nuevaFuncion
    }

    fun eliminarShow(idShow: Int) {
        val showAEliminar = showRepositorio.getById(idShow)
        showRepositorio.delete(showAEliminar)

    }

    fun editarDatos(idShow: Int, nombreBanda: String, nombreRecital: String) {
        val show = getShowByID(idShow) as Show
        show.cambiarNombres(nombreBanda, nombreRecital)
    }
}

