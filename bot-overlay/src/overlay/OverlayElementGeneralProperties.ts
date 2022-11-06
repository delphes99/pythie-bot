export class OverlayElementGeneralProperties {
  id: string
  left: number
  top: number

  constructor(left: number, top: number, id: string) {
    this.id = id
    this.left = left
    this.top = top
  }

  public static fromJson(json: {
    id: string
    left: number
    top: number
  }): OverlayElementGeneralProperties {
    return new OverlayElementGeneralProperties(json.left, json.top, json.id)
  }
}