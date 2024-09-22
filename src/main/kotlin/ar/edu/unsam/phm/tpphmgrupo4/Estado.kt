package ar.edu.unsam.phm.tpphmgrupo4


/* Aca definimos por estado como un enum y por el parametro el porcentaje de la rentabilidad */
enum class Estado (val rentabilidad : Double) {
    PrecioBase(0.8),
    VentaPlena(1.0),
    Megashow(1.3)
}