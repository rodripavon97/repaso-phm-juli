package ar.edu.unsam.phm.tpphmgrupo4

import java.time.LocalDate

/**
De las entradas se sabe:
Número
Ubicación
Precio (Luego detallaremos el cálculo del mismo)
Fecha
 el show id se une al show (aca aplicamos un concepto de base de datos para que la entrada este unida al show
 usamos el id de su funcion y show) esto lo podemos ver despues su implementacion
 se utiliza un flag para saber si la entrada esta vendida
 **/

/*
*
* como vamos a hasta este momento?
* entonces, la  entrada esta unidaa su show,  y de ahi usan un flag? creo que sigo entonces
* como hacen esa parte-?
* exacto, las entradas en la vida real tiene un id show y de la funcion, lo pensamos de esa forma
* no lo  habia pensado, esta bueno,
* algunas cosas los resolve con los id, ya que despues con los id podes traer infos o hacer un repo con consultas de sql
* o no sql (por eso despues del tp 0 el repo este no sirve, es mas para ver como venimos de algo 3)
* la idea en su momento la tiro ceci yo lo veia de otra forma pero es mas comodo tener el info del id de show y funcion
* en el tp 2 no podes combinar dos db con mismas propiedades, por eso llaman al id de esa funciion y show
* entonces el id se comporta como un string largo (a lo que trabaja mongo, medio que estoy tirando spoilers xd)
* */

/* tire mucha data de como ver phm en estos momentos

bastante jaja pero creo queentendi, despues leo bien y te digo
*
dale igual manana le siguimos con esto tranqui

ahi lo subo a un repo*/
class Entrada(
    var numeroEntrada: Int,
    var ubicacion: Ubicacion,
    var fechaEntrada: LocalDate,
    var precioBase: Double,
    var showEstado: Estado,
    var funcionId: Int,
    var showId : Int
) : Entity{
    var estaVendida = false
    override var id: Int = Entity.idInicial

    fun cambiarEstado(estado: Estado) {
        showEstado = estado
    }

    /* el metodo usa el costo de la ubicaciom + el precio base */
    fun precio(): Double {
        return ubicacion.costo + precioBase
    }

    /*aca se calcula el precio final usando la rentabilidad de cada show*/
    fun precioFinal(): Double {
        return precio() * showEstado.rentabilidad
    }

    fun vender() {
        estaVendida = true
    }

}