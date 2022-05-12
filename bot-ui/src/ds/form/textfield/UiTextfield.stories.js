import UiTextfield from "./UiTextfield.vue"

export default {
  title: "Design System/Form/Textfield",
  component: UiTextfield,
}

const Template = (args) => ({
  components: { UiTextfield },
  setup() {
    return {
      args,
    }
  },
  template:
    '<ui-textfield :label="args.label" v-model="args.modelValue" />',
})

export const Empty = Template.bind({})
Empty.args = {
  label: "Empty select",
}

export const Without_label = Template.bind({})
Without_label.args = {
  modelValue: "some value",
}

export const With_value = Template.bind({})
With_value.args = {
  label: "Some label",
  modelValue: "some value",
}
