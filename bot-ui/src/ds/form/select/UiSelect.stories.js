import UiSelect from "./UiSelect.vue"
import { UiSelectOption } from "./UiSelectOption"

export default {
  title: "Design System/Form/Select",
  component: UiSelect,
  argTypes: {
    options: {
      disable: true,
    },
    optionItems: {
      control: "array",
    },
  },
}

const Template = (args) => ({
  components: { UiSelect },
  setup() {
    return {
      args,
      mapOptions: UiSelectOption.forString,
    }
  },
  template:
    '<ui-select :label="args.label" :options="mapOptions(args.optionItems)" />',
})

export const Empty = Template.bind({})
Empty.args = {
  label: "Empty select",
  optionItems: [],
}

export const Four_elements = Template.bind({})
Four_elements.args = {
  label: "Select some element",
  optionItems: ["first", "second", "third", "fourth"],
}
