package component.main

import ajax.Api
import component.adapters.LinkComponentHrefAdapter
import component.bootstrap.*
import component.store.LogoutStoreAction
import component.store.StoreState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.dom.img
import react.redux.rConnect
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
    val title: String, // TODO check unused prop
    val backRoute: String?, // TODO check unused prop
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

    Navbar {
        attrs {
            bg = "dark"
            variant = NavbarVariant.dark
        }
        NavbarBrand {
            img {
                attrs {
                    src = "/static/images/ramona-small.jpg"
                    width = "30px"
                    height = "30px"
                }
            }
            +" Ramona"
        }
        Nav {
            attrs.className = "mr-auto"
            NavLink {
                attrs.`as` = LinkComponentHrefAdapter
                attrs.href = "/"
                +"Home"
            }
            NavLink {
                attrs.`as` = LinkComponentHrefAdapter
                attrs.href = "/standups"
                +"Standups"
            }
        }
        NavbarText {
            attrs.className = "mr-1"
            +"Signed in as:"
        }
        NavbarText {
            attrs.className = "font-weight-bold text-light mr-2"
            +(props.userName ?: "")
        }
        Button {
            attrs {
                size = ButtonSize.sm
                variant = ButtonVariant.outline_info
                onClick = { doLogout() }
            }
            +"Logout"
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
