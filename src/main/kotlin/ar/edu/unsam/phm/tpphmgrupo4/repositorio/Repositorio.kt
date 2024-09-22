package ar.edu.unsam.phm.tpphmgrupo4.repositorio

import ar.edu.unsam.phm.tpphmgrupo4.*
import ar.edu.unsam.phm.tpphmgrupo4.Exceptions.IdInvalido
import ar.edu.unsam.phm.tpphmgrupo4.Exceptions.UnathorizedUser
import org.springframework.stereotype.Component

/*
* el repositorio es una clase con un generic
* el repositorio en memoria me permite interactuar con los services
* al llamar al generic llama todo lo que pide la clase repo, que es una lista de elementos
*  y llama al id incial 1
* vamos bien juli?
*
* perdon estaba  leyendo, sii voy bien
*
* tranqui julii
* */
@Component
class Repositorio<T : Entity> {
    val elementos: MutableSet<T> = mutableSetOf()
    var idActual: Int = 1
    /*
    * entonces aca me creo metodos para el get, update, delete, create*/
    fun getAll(): MutableSet<T> {
        return elementos
    }

    fun getById(id: Int): T {
        return elementos.find { id == it.id }
            ?: throw IdInvalido("Elemento invalido: El elemento no existe en el repositorio")
    }

    fun delete(elemento: T) {
        val elementoAEliminar = getById(elemento.id)
        elementos.remove(elementoAEliminar)
    }

    fun create(elemento: T) {
        validarInexistencia(elemento)
        agregar(elemento)
    }

    /*Aca es un verificador si existe el id, usa el metodo any si ningun elemento es igual al id*/
    /* Esto te lo subo a un git asi lo tenes tranqui Juli
    *
    * graciaas, estaba leyewndo a  las apuradas jajaja
    * dale
    * de los mensajes de validaciones tenes dudas? los  mensajes de validaciones?ah ya entendi, nono
    * es lo que usamos en proyecto, de aca les tiraba eso a vos y vero
    * claroo eso lo entiendo
    * con repo vamos bien entonces?
    * sisi por ahora
    * volvemos a show asi vemos como se encara esa parte
    * daleee
    * tranqui con eso, igual te explico el paso a paso de los repo*/
    fun existeId(id: Int): Boolean {
        return elementos.any { elemento -> elemento.id == id }
    }

    private fun agregar(elemento: T) {
        asignarId(elemento)
        elementos.add(elemento)
    }

    private fun asignarId(elemento: T) {
        elemento.id = idActual++
    }

    /*
    * aca vamos a validar inexistencia de  los id, para crear elementos y/o objetos en el back. No se tendria que crear elementos
    * nuevos con un id existente, despues de los siguiente tp, mysql, mongo, redis y neo lo resuelve sin problemas. el tema
    * es en memoria*/
    private fun validarInexistencia(elemento: T) {
        if (existeId(elemento.id)) {
            throw IdInvalido("Error ID: Ya existe un elemento con ese ID")
        }
    }

    fun clear() {
        elementos.clear()
    }

    /*
    * el update funciona asi, llama en el paramentro del metodo un elemento
    * entonces para editar algo busca el id del elemento que necesita editar o actualizar, llama al metodo getbyid (usa el id del elemento
    * elimina el elemento que tiene el id y lo agrega de nuevo*/
    fun update(elemento : T){
        var elementoExistente = getById(elemento.id)
        elementos.remove(elementoExistente)
        elementos.add(elemento)
    }
}

@Component
class RepositorioShow : Repositorio<Show> () {



}

@Component
class RepositorioUsuario: Repositorio <Usuario>() {
    fun findUserByUsernameAndPassword(username: String, password: String): Usuario? {
        return getAll().find { it.username == username && it.password == password } ?: throw UnathorizedUser("usuario y/o contrasenia incorrecta")
    }
}

@Component
class RepositorioInstalacion: Repositorio<Instalacion>()

@Component
class RepositorioEntrada: Repositorio<Entrada>()
