package ar.edu.unsam.phm.tpphmgrupo4.repositorio
import ar.edu.unsam.phm.tpphmgrupo4.DTO.LoginDTO

interface AuthLogin{
    fun loginUsuario(user: LoginDTO): Int
}