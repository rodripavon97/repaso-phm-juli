package ar.edu.unsam.phm.tpphmgrupo4.DTO

import ar.edu.unsam.phm.tpphmgrupo4.Comentario
import java.time.LocalDate

class ComentarioDTO {
    var id: Int?=null
    var idShow: Int?=null
    lateinit var fotoUsuario: String
    lateinit var fotoBanda: String
    lateinit var nombreUsuario: String
    lateinit var nombreBanda: String
    lateinit var fecha: LocalDate
    lateinit var contenido: String
    var puntuacion: Float?=null

    companion object {
        fun fromComentario(comentario: Comentario) : ComentarioDTO = ComentarioDTO().also {
            it.id = comentario.id
            it.idShow = comentario.idShow
            it.fotoUsuario = comentario.fotoUsuario
            it.fotoBanda = comentario.fotoBanda
            it.nombreUsuario = comentario.nombreUsuario
            it.nombreBanda = comentario.nombreBanda
            it.fecha = comentario.fecha
            it.contenido = comentario.contenido
            it.puntuacion = comentario.puntuacion
        }
    }
}