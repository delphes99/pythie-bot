import { OverlayElementProperties, RenderComponent } from "@/overlay/OverlayElementProperties"
import ImageComponentRender from "@/overlay/component/imageComponent/ImageComponentRender.vue"

export default class ImageComponent implements OverlayElementProperties {
  type = "Image"
  url: string
  width?: number
  height?: number

  constructor(url: string, width?: number, height?: number) {
    this.url = url
    this.width = width
    this.height = height
  }

  renderComponent(): RenderComponent {
    return ImageComponentRender
  }

  public static fromObject(obj: {
    url: string
    width?: number
    height?: number
  }): ImageComponent {
    const { url, width, height } = obj
    return new ImageComponent(url, width, height)
  }
}