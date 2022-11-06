export class OverlayElementGeneralProperties {
  id: string
  left: number
  top: number
  sortOrder: number

  constructor(id: string, left: number, top: number, sortOrder: number) {
    this.id = id
    this.left = left
    this.top = top
    this.sortOrder = sortOrder
  }

  public static fromJson(json: {
    id: string
    left: number
    top: number
    sortOrder: number
  }): OverlayElementGeneralProperties {
    return new OverlayElementGeneralProperties(json.id, json.left, json.top, json.sortOrder)
  }
}