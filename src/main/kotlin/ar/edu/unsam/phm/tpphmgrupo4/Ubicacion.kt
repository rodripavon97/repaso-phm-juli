package ar.edu.unsam.phm.tpphmgrupo4

/*
* aca se llama a la ubicacion de cada estadio o teatro
* */
enum class Ubicacion(val costo: Int) {
    Palco(20000),
    Campo(15000),
    PlateaAlta(10000),
    PlateaBaja(15000),
    Pullman(10000)
}
