package ar.edu.unsam.phm.tpphmgrupo4
/*
*
* Aca el entity actua como un generador de ID
*
* prtegunta tonta...con que se come el companion?
* https://medium.com/@mobiledev4you/companion-object-in-kotlin-c3a1203cd63c es mas o menos esto es como un metodo estaticos pero con ciertas reponsabilidades
* ahi reviso
* usa un extension function que crea el id, y el metodo nuevo crea un nuevo elemento
* vamos bien por ahi?
*
* el 99% lo entendi*/
interface Entity {

        /*
        * entonces aca lo instacia con un id inicial 0
        * y por afuera se define una variable id */
        companion object {
                const val idInicial = 0
        }
        var id: Int

        fun esNuevo(): Boolean = id == idInicial
}