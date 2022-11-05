import { v4 as uuid } from "uuid"

export class OverlayElementGeneralProperties {
  id: string
  left: number
  top: number

  constructor(left: number, top: number, id: string = uuid()) {
    this.id = id
    this.left = left
    this.top = top
  }

  modifyLeft(left: number): OverlayElementGeneralProperties {
     return this.modifyCoordinate(left, this.top)
  }

  modifyTop(top: number): OverlayElementGeneralProperties {
    return this.modifyCoordinate(this.left, top)
  }

  modifyCoordinate(left: number, top: number): OverlayElementGeneralProperties {
    return new OverlayElementGeneralProperties(left, top, this.id)
  }

  equals(other: OverlayElementGeneralProperties): boolean {
    return (
      other &&
      this.id === other.id &&
      this.left === other.left &&
      this.top === other.top
    )
  }

  public static fromJson(json: {
    id: string
    left: number
    top: number
  }): OverlayElementGeneralProperties {
    return new OverlayElementGeneralProperties(json.left, json.top, json.id)
  }
}