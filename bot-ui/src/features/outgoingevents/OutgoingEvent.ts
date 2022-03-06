import OutgoingEventType from "@/features/outgoingevents/OutgoingEventType"

export default interface OutgoingEvent {
  id: string
  type: OutgoingEventType
  text: string
}
