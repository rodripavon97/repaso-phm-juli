package ar.edu.unsam.phm.tpphmgrupo4.Controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EjemploController {

    @GetMapping("/ejemplo")
    @Operation(summary = "Devuelve simplemente hola y muestra cómo documentar Swagger")
    fun defaultGet(): String = "hola"

}