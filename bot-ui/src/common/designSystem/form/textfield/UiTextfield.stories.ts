import {Meta, StoryObj} from "@storybook/vue3";
import UiTextfield from "./UiTextfield.vue"

const meta: Meta<typeof UiTextfield> = {
    component: UiTextfield,
    title: "Design System/Form/Textfield",
}

export default meta
type Story = StoryObj<typeof UiTextfield>;

export const Empty = {
    args: {
        label: "Empty select",
    }
}

export const Without_label = {
    args: {
        modelValue: "some value",
    }
}

export const With_value = {
    args: {
        label: "Some label",
        modelValue: "some value",
    }
}

export const Password = {
    args: {
        label: "Some label",
        modelValue: "some value",
        password: true,
    }
}