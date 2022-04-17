import StringEditComponent from "@/features/components/description/edit/StringEditComponent.vue"
import StringViewComponent from "@/features/components/description/view/StringViewComponent.vue"
import {FormItem} from "@/features/components/description/FormItem"

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