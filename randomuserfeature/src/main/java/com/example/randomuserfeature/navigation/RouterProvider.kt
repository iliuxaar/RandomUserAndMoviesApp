package com.example.randomuserfeature.navigation


class RouterProvider {
    companion object {

        private var sRouter: Router? = null

        fun registerRouter(router: Router) {
            sRouter = router
        }

        val router: Router
            get() {
                if (sRouter == null) {
                    throw IllegalStateException("You need register some Router firstly!")
                }
                return sRouter as Router
            }
    }
}