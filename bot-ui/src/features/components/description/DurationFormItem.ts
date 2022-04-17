import DurationEditComponent from "@/features/components/description/edit/DurationEditComponent.vue"
import DurationViewComponent from "@/features/components/description/view/DurationViewComponent.vue"
import {FormItem} from "@/features/components/description/FormItem"

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