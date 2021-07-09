import TextComponent from "@/overlay/editor/textComponent.ts";

export interface OverlayElement {
  id: string;
  representation: string;
  equals(other: OverlayElement): boolean;
}

export function fromJson(json: any): OverlayElement | null {
  if (json.type === "Text") {
    return new TextComponent(json.left, json.top, json.text, json.id);
  } else {
    return null;
  }
}
