import {Meta, StoryObj} from "@storybook/vue3";
import UiSelect from "./UiSelect.vue"
import {UiSelectOption} from "./UiSelectOption"

const meta: Meta<typeof UiSelect> = {
    component: UiSelect,
    title: "Design System/Form/Select",
    argTypes: {
        optionItems: Array,
    },
    args: {
        optionItems: [],
    },
    render: (args) => ({
        components: {UiSelect},
        setup() {
            return {
                args,
                mapOptionsItems: UiSelectOption.forString
            };
        },
        template: `
          <UiSelect :label="args.label" :options="mapOptionsItems(args.optionItems)"/>
        `,
    }),
}

export default meta
type Story = StoryObj<typeof UiSelect>;

export const Empty = {
    args: {
        label: "Empty select",
        optionItems: [],
    }
}

export const One_element = {
    args: {
        label: "Select one element",
        optionItems: ["first"],
    }
}

export const Four_elements = {
    args: {
        label: "Select some element",
        optionItems: ["first", "second", "third", "fourth"],
    }
}