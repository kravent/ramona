package component

import component.login.loginInterceptor
import component.store.storeProvider
import react.RBuilder
import react.ReactElement
import react.router.dom.browserRouter

fun RBuilder.app(): ReactElement =
    storeProvider {
        loginInterceptor {
            browserRouter {
                appRouter()
            }
        }
    }
