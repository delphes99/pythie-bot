import { OverlayElement } from "@/overlay/OverlayElement.ts";
import Resolution from "@/overlay/Resolution.ts";

export default interface Overlay {
  id: string;
  title: string;
  resolution: Resolution;
  elements: OverlayElement[];
}
