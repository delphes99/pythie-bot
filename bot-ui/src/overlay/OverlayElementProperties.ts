export interface OverlayElementProperties {
  type: string
  representation: string
  equals(other: OverlayElementProperties): boolean
  renderComponent(): RenderComponent
  propertiesComponent(): PropertiesComponent
}

export type RenderComponent = any
export type PropertiesComponent = any