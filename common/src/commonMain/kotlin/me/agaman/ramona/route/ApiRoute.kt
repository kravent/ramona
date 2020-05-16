package me.agaman.ramona.route

import me.agaman.ramona.model.*
import kotlin.reflect.KClass

interface ApiRoute {
    val path: String
}
class GetApiRoute<ResponseClass : Any>(
    override val path: String,
    private val responseClass: KClass<ResponseClass>
) : ApiRoute
class AbstractGetApiRoute<ParamsClass: Any, ResponseClass : Any>(
    val path: String,
    private val responseClass: KClass<ResponseClass>,
    private val routeBuilder: (params: ParamsClass) -> String
) {
    fun build(params: ParamsClass) = GetApiRoute(routeBuilder(params), responseClass)
}
class PostApiRoute<RequestClass : Any, ResponseClass : Any>(
    override val path: String,
    private val requestClass: KClass<RequestClass>,
    private val responseClass: KClass<ResponseClass>
) : ApiRoute

object ApiRoutes {
    val STANDUP_SAVE = PostApiRoute("standup/save", StandupSaveRequest::class, StandupSaveResponse::class)
    val STANDUP_GET = AbstractGetApiRoute(
        "standup/get/{id}",
        StandupGetResponse::class
    ) { params: StandupGetRequest -> "standup/get/${params.id}" }
    val STANDUP_LIST = GetApiRoute("standup/list", StandupListResponse::class)
}
