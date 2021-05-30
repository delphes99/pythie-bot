import { OverlayElement } from "@/overlay/OverlayElement";
import { v4 as uuidv4 } from "uuid";

export default class TextComponent implements OverlayElement {
  id: string;
  left: number;
  top: number;
  type = "Text";
  text: string;

  constructor(left: number, top: number, text: string, id: string = uuidv4()) {
    this.id = id;
    this.left = left;
    this.top = top;
    this.text = text;
  }
}
