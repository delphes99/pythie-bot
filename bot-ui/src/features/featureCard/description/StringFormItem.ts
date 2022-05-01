import StringEditComponent from "@/features/featureCard/description/edit/StringEditComponent.vue"
import { FormItem } from "@/features/featureCard/description/FormItem"
import StringViewComponent from "@/features/featureCard/description/view/StringViewComponent.vue"

export class StringFormItem extends FormItem {
  value: string | null

  constructor(id: string, field: string, value: string | null) {
    super(id, field)
    this.value = value
  }

  appendToResult(result: any) {
    result[this.field] = this.value
  }

  isEmpty(): boolean {
    return this.value == null
  }

  viewComponent() {
    return StringViewComponent
  }

  editComponent() {
    return StringEditComponent
  }
}