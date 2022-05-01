import Feature from "@/features/configurations/Feature"
import FeatureDescriptionType from "@/features/configurations/FeatureDescriptionType"
import { DurationFormItem } from "@/features/featureCard/description/DurationFormItem"
import { OutgoingEventsFormItem } from "@/features/featureCard/description/OutgoingEventsFormItem"
import { StringFormItem } from "@/features/featureCard/description/StringFormItem"
import OutgoingEvent from "@/features/outgoingevents/OutgoingEvent"

export function mapToFormItems(feature: Feature) {
    return feature.descriptionItems.map((desc) => {
        switch (desc.type) {
            case FeatureDescriptionType.STRING:
                return new StringFormItem(
                    feature.identifier + "_" + desc.name,
                    desc.name,
                    desc.currentValue as string,
                )
            case FeatureDescriptionType.OUTGOING_EVENTS:
                return new OutgoingEventsFormItem(
                    feature.identifier + "_" + desc.name,
                    desc.name,
                    desc.currentValue as OutgoingEvent[],
                )
            case FeatureDescriptionType.DURATION:
                return new DurationFormItem(
                    feature.identifier + "_" + desc.name,
                    desc.name,
                    desc.currentValue as bigint,
                )
        }
    })
}