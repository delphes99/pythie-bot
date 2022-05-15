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
    '<ui-textfield v-model="args.modelValue" :password="args.password" :label="args.label" />',
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

export const Password = Template.bind({})
Password.args = {
  label: "Some label",
  modelValue: "some value",
  password: true,
}
