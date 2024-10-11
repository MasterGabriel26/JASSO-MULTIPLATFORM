package com.jasso.inteligenciaenventas

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform