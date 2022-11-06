import TextComponent from "@/overlay/component/textComponent/textComponent"
import { OverlayElementGeneralProperties } from "@/overlay/OverlayElementGeneralProperties"
import { OverlayElementProperties } from "@/overlay/OverlayElementProperties"

export default class OverlayElement<T extends OverlayElementProperties> {
  general: OverlayElementGeneralProperties
  properties: T

  constructor(general: OverlayElementGeneralProperties, properties: T) {
    this.general = general
    this.properties = properties
  }

  get id(): string {
    return this.general.id
  }
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export function fromJson(json: any): OverlayElement<any> | null {
  const generalProperties = OverlayElementGeneralProperties.fromJson(json.general)
  const specificProperties = specificPropertiesFromJson(json.properties)

  if(!specificProperties) {
    return null
  }

  return new OverlayElement(generalProperties, specificProperties)
}

function specificPropertiesFromJson(json: any): OverlayElementProperties | null {
  if (json.type === "Text") {
    return TextComponent.fromObject(json)
  } else {
    return null
  }
}
