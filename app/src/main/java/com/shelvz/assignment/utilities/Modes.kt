package com.shelvz.assignment.utilities

sealed class Mode(id: Int) {
    class Local : Mode(LOCAL)
    class Remote : Mode(REMOTE)
    class None : Mode(None)

    companion object {
        const val LOCAL = 1
        const val REMOTE = 2
        const val None = -1
        fun with(id: Int): Mode {
            return when (id) {
                LOCAL -> Local()
                REMOTE -> Remote()
                else -> None()
            }
        }
    }
}

sealed class Action(id: Int) {
    class Reload : Action(RELOAD)
    class Add : Action(ADD)
    class Prepend : Action(PREPEND)
    class None : Action(None)
    companion object {
        const val RELOAD = 1
        const val ADD = 2
        const val PREPEND = 3
        const val None = -1
        fun with(id: Int): Action {
            return when (id) {
                RELOAD -> Action.Reload()
                ADD -> Action.Add()
                PREPEND -> Action.Prepend()
                else -> Action.None()
            }
        }
    }
}
