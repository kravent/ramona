package me.agaman.ramona.route

enum class Route(val path: String) {
    API("api"),
    LOGIN("api/login"),
    LOGOUT("api/logout"),
    STATIC("static"),
}
