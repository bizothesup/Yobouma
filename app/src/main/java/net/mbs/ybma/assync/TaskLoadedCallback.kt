package net.mbs.ybma.assync

interface TaskLoadedCallback {
    fun onTaskDone(vararg values: Any?)
}