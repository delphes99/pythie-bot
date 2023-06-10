import UiToggle from "@/common/designSystem/form/toggle/ui-toggle.vue";
import {Meta, StoryObj} from "@storybook/vue3";

const meta: Meta<typeof UiToggle> = {
    component: UiToggle,
    title: "Design System/Form/Toggle",
    argTypes: {
        checked: Boolean,
        label: String,
    },
    args: {},
}

export default meta
type Story = StoryObj<typeof UiToggle>;

export const Default: Story = {
    render: (args: any) => ({
        components: {UiToggle},
        setup() {
            return {args};
        },
        template: `
          <ui-toggle :label="args.label" :model-value="args.checked"/>
        `,
    }),
};

export const Unchecked = {
    ...Default,
    args: {
        checked: false,
    }
}

export const Checked = {
    ...Default,
    args: {
        checked: true,
    }
}