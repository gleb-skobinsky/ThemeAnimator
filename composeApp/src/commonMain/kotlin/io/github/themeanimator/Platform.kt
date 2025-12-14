package io.github.themeanimator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform