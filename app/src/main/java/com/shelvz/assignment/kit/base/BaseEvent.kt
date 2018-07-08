package com.shelvz.assignment.kit.base

sealed class BaseEvent {
    class PullLatest(val status: SyncStatus) : BaseEvent()
}

sealed class SyncStatus {
    class Started : SyncStatus()
    class InProgress : SyncStatus()
    class Finished : SyncStatus()
    class Failed : SyncStatus()
}
