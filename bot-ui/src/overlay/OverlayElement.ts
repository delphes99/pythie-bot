import TextComponent from "@/overlay/editor/textComponent"

export interface OverlayElement {
  id: string
  type: string
  representation: string
  equals(other: OverlayElement): boolean
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export function fromJson(json: any): OverlayElement | null {
  if (json.type === "Text") {
    return new TextComponent(json.left, json.top, json.text, json.color, json.id)
  } else {
    return null
  }
}
