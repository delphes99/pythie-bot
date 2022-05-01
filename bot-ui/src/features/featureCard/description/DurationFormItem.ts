import DurationEditComponent from "@/features/featureCard/description/edit/DurationEditComponent.vue"
import { FormItem } from "@/features/featureCard/description/FormItem"
import DurationViewComponent from "@/features/featureCard/description/view/DurationViewComponent.vue"

export class DurationFormItem extends FormItem {
  value: bigint | null

  constructor(id: string, field: string, value: bigint | null) {
    super(id, field)
    this.value = value
  }

  appendToResult(result: any) {
    result[this.field] = `PT${this.value?.toString()}S`
  }

  isEmpty(): boolean {
    return this.value == null
  }

  viewComponent() {
    return DurationViewComponent
  }

  editComponent() {
    return DurationEditComponent
  }
}