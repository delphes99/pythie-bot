import { OverlayElementProperties, RenderComponent } from "@/overlay/OverlayElementProperties"
import TextComponentRender from "@/overlay/component/textComponent/TextComponentRender.vue"

export default class TextComponent implements OverlayElementProperties {
  type = "Text"
  text: string
  font: string
  fontSize: string
  color: string

  constructor(
    text: string,
    color: string,
    font: string,
    fontSize: string,
  ) {
    this.text = text
    this.color = color
    this.font = font
    this.fontSize = fontSize
  }

  renderComponent(): RenderComponent {
    return TextComponentRender
  }

  public static fromObject(obj: {
    text: string
    font: string
    fontSize: string
    color: string
  }): TextComponent {
    const { text, font, fontSize, color } = obj
    return new TextComponent(text, color, font, fontSize)
  }
}