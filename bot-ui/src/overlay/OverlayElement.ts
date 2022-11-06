import ImageComponent from "@/overlay/editor/component/imageComponent/ImageComponent"
import TextComponent from "@/overlay/editor/component/textComponent/TextComponent"
import { OverlayElementGeneralProperties } from "@/overlay/OverlayElementGeneralProperties"
import { OverlayElementProperties } from "@/overlay/OverlayElementProperties"

export default class OverlayElement<T extends OverlayElementProperties> {
  general: OverlayElementGeneralProperties
  properties: T

  constructor(general: OverlayElementGeneralProperties, properties: T) {
    this.general = general
    this.properties = properties
  }

  modifyGeneral(generateNew: (oldValue: OverlayElementGeneralProperties) => OverlayElementGeneralProperties): OverlayElement<T> {
    return new OverlayElement(generateNew(this.general), this.properties)
  }

  get id(): string {
    return this.general.id
  }

  equals(other: OverlayElement<T>): boolean {
    return (
      other &&
      this.general.equals(other.general) &&
      this.properties.equals(other.properties)
    )
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
  } else if (json.type === "Image") {
    return ImageComponent.fromObject(json)
  } else {
    return null
  }
}
