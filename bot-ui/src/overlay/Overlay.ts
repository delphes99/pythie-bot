import { OverlayElement } from "@/overlay/OverlayElement.ts";
import Resolution from "@/overlay/Resolution.ts";

export default class Overlay {
  id: string;
  title: string;
  resolution: Resolution;
  elements: OverlayElement[];

  constructor(
    id: string,
    title: string,
    resolution: Resolution,
    elements: OverlayElement[] = []
  ) {
    this.id = id;
    this.title = title;
    this.resolution = resolution;
    this.elements = elements;
  }
}
