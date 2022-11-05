import OverlayElement from "@/overlay/OverlayElement"
import { OverlayElementProperties } from "@/overlay/OverlayElementProperties"
import { defineStore } from "pinia"

interface OverlayEditorStoreState {
  components: OverlayElement<OverlayElementProperties>[]
  selectedElementId: string | null
}

export const useOverlayEditorStore = defineStore("overlayEditor",
  {
    state: (): OverlayEditorStoreState => ({
        components: [],
        selectedElementId: null,
      }
    ),
    getters: {
      selectedElement(): OverlayElement<OverlayElementProperties> | null {
        return this.components.find(c => c.id === this.selectedElementId) || null
      },
    },
    actions: {
      init(components: OverlayElement<OverlayElementProperties>[]) {
        this.components = components
        this.selectedElementId = null
      },
      selection(selection: OverlayElement<OverlayElementProperties>) {
        if (!this.components.find(component => selection.id === component.id)) {
          throw new Error(`Unknown component ! ${selection.id}`)
        }

        this.selectedElementId = selection.id
      },
      updateComponent(componentToUpdate: OverlayElement<OverlayElementProperties>) {
        if (!this.components.find(component => componentToUpdate.id === component.id)) {
          throw new Error(`Unknown component : ${componentToUpdate.id}`)
        }

        this.components = [
          ...this.components.filter((e) => e.id !== componentToUpdate.id),
          componentToUpdate,
        ]
      },
      addComponent(componentToUpdate: OverlayElement<OverlayElementProperties>) {
        if (this.components.find(component => componentToUpdate.id === component.id)) {
          throw new Error(`Component already added : ${componentToUpdate.id}`)
        }

        this.components = [
          ...this.components,
          componentToUpdate,
        ]
      },
    },
  })