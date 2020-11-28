package fr.delphes.twitch.eventSub.payload

enum class EventSubSubscriptionStatus {
    enabled,
    webhook_callback_verification_pending,
    webhook_callback_verification_failed,
    notification_failures_exceeded,
    authorization_revoked,
    user_removed
}