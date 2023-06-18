export interface OverlayElementProperties {
  type: string
  renderComponent(): RenderComponent
}

export type RenderComponent = any