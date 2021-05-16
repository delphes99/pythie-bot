import { OverlayElement } from "@/overlay/OverlayElement";
import Resolution from "@/overlay/Resolution";

export default interface Overlay {
  id: string;
  title: string;
  resolution: Resolution;
  elements: OverlayElement[];
}
