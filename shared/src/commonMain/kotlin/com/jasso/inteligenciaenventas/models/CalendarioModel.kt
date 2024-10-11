package com.jasso.inteligenciaenventas.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CalendarioModel(
    @SerialName("asunto") val asunto: String?,
    @SerialName("nombreCliente") val nombreCliente: String?,
    @SerialName("tipoAgenda") val tipoAgenda: String?,
    @SerialName("fechaAgenda") val fechaAgenda: Long?,
    @SerialName("fechaCreacion") val fechaCreacion: Long?,
    @SerialName("horaInicio") val horaInicio: String?,
    @SerialName("lugar") val lugar: String?,
    @SerialName("descripcion") val descripcion: String?,
    @SerialName("userId") val userId: String?
){
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
    )
}

