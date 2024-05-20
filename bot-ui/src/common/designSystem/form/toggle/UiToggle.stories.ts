import UiToggle from "@/common/designSystem/form/toggle/UiToggle.vue";
import {Meta, StoryObj} from "@storybook/vue3";

const meta: Meta<typeof UiToggle> = {
    component: UiToggle,
    title: "Design System/Form/Toggle",
}

export default meta
type Story = StoryObj<typeof UiToggle>;

export const Unchecked = {
    args: {
        modelValue: false,
    }
}

export const Checked = {
    args: {
        modelValue: true,
    }
}