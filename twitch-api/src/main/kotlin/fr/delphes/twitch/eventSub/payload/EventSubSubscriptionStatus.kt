package fr.delphes.twitch.eventSub.payload

enum class EventSubSubscriptionStatus {
    enabled,
    authorization_revoked,
    webhook_callback_verification_pending
}