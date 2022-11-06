import OverlayElement from "@/overlay/OverlayElement"
import { OverlayElementProperties } from "@/overlay/OverlayElementProperties"
import Resolution from "@/overlay/Resolution"

export default class Overlay {
  id: string
  title: string
  resolution: Resolution
  elements: OverlayElement<OverlayElementProperties>[]

  constructor(
    id: string,
    title: string,
    resolution: Resolution,
    elements: OverlayElement<OverlayElementProperties>[] = [],
  ) {
    this.id = id
    this.title = title
    this.resolution = resolution
    this.elements = elements
  }
}
