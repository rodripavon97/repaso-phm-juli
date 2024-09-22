package ar.edu.unsam.phm.tpphmgrupo4

import ar.edu.unsam.phm.tpphmgrupo4.Usuario
import java.time.LocalDate

class Comentario(
    var idShow: Int,
    var fotoUsuario: String,
    var fotoBanda: String,
    var nombreUsuario: String,
    var nombreBanda: String,
    var fecha: LocalDate,
    var contenido: String,
    var puntuacion: Float
): Entity{
    override var id: Int = Entity.idInicial
}