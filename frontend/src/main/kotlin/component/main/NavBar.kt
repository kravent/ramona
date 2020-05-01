package component.main

import ajax.Api
import component.store.LogoutStoreAction
import component.store.StoreState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.button
import react.dom.div
import react.dom.span
import react.redux.rConnect
import react.router.dom.routeLink
import redux.RAction
import redux.WrapperAction

interface ConnectedNavBarProps : RProps {
    var title: String
    var backRoute: String?
}

interface NavBarStateProps : RProps {
    var userName: String?
}

interface NavBarDispatchProps : RProps {
    var onLogout: () -> Unit
}

data class NavBarProps(
    val title: String,
    val backRoute: String?,
    val userName: String?,
    val onLogout: () -> Unit
) : RProps

val NavBar = rFunction("NavBarComponent") { props: NavBarProps ->
    fun doLogout() {
        MainScope().launch {
            Api.logout()
            props.onLogout()
        }
    }

    div {
        props.backRoute?.let { routeLink(to = it) { +"Back" } }
        span {
            +props.title
        }
        button {
            attrs.onClickFunction = { doLogout() }
            +"Logout (${props.userName})"
        }
    }
}

val NavBarConnector =
    rConnect<StoreState, RAction, WrapperAction, ConnectedNavBarProps, NavBarStateProps, NavBarDispatchProps, NavBarProps>(
        { state, _ ->
            userName = state.loggedUser
        },
        { dispatch, _ ->
            onLogout = { dispatch(LogoutStoreAction) }
        }
    )

val ConnectedNavBar = NavBarConnector(NavBar)

fun RBuilder.navBar(
    title: String,
    backRoute: String? = null
): ReactElement = ConnectedNavBar {
    attrs.title = title
    attrs.backRoute = backRoute
}
