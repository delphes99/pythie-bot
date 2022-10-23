import { OverlayElement } from "@/overlay/OverlayElement"
import { v4 as uuid } from "uuid"

export default class TextComponent implements OverlayElement {
  id: string
  left: number
  top: number
  type = "Text"
  text: string
  color: string

  constructor(left: number, top: number, text: string, color: string, id: string = uuid()) {
    this.id = id
    this.left = left
    this.top = top
    this.text = text
    this.color = color
  }

  equals(other: OverlayElement): boolean {
    return (
      other instanceof TextComponent &&
      this.id === other.id &&
      this.left === other.left &&
      this.top === other.top &&
      this.text === other.text &&
      this.color === other.color
    )
  }
  public get representation() {
    return this.text
  }
}

export function fromObject(obj: {
  left: number
  top: number
  text: string
  color: string
  id: string
}): TextComponent {
  const { left, top, text, color, id } = obj
  return new TextComponent(left, top, text, color, id)
}
