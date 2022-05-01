import { FormItem } from "@/features/components/description/FormItem"
import OutgoingEventsEditComponent from "@/features/components/description/edit/OutgoingEventsEditComponent.vue"
import OutgoingEventsViewComponent from "@/features/components/description/view/OutgoingEventsViewComponent.vue"
import OutgoingEvent from "@/features/outgoingevents/OutgoingEvent"
import TwitchOutgoingSendMessage from "@/features/outgoingevents/TwitchOutgoingSendMessage"

export class OutgoingEventsFormItem extends FormItem {
  events: OutgoingEvent[]

  constructor(id: string, field: string, events: OutgoingEvent[]) {
    super(id, field)
    this.events = events
  }

  appendToResult(result: any) {
    result[this.field] = this.events?.map(
      (event) => new TwitchOutgoingSendMessage(event.text, result.channel),
    )
  }

  isEmpty(): boolean {
    return this.events == null
  }

  viewComponent() {
    return OutgoingEventsViewComponent
  }

  editComponent() {
    return OutgoingEventsEditComponent
  }
}