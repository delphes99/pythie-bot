import Feature from "@/features/feature"
import FeatureDescriptionType from "@/features/FeatureDescriptionType"
import { DurationFormItem } from "@/features/featureCard/description/DurationFormItem"
import { OutgoingEventsFormItem } from "@/features/featureCard/description/OutgoingEventsFormItem"
import { StringFormItem } from "@/features/featureCard/description/StringFormItem"
import OutgoingEvent from "@/features/outgoingevents/OutgoingEvent"
import { v4 as uuid } from "uuid"

export function mapToFormItems(feature: Feature) {
  return feature.descriptionItems.map((desc) => {
    switch (desc.type) {
      case FeatureDescriptionType.STRING:
        return new StringFormItem(
          uuid(),
          desc.name,
          desc.value as string,
        )
      case FeatureDescriptionType.OUTGOING_EVENTS:
        return new OutgoingEventsFormItem(
          uuid(),
          desc.name,
          desc.value as OutgoingEvent[],
        )
      case FeatureDescriptionType.DURATION:
        return new DurationFormItem(
          uuid(),
          desc.name,
          desc.value as bigint,
        )
    }
  })
}