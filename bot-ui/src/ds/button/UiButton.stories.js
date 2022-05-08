import UiButton from "./UiButton.vue"

export default {
  title: "Design System/Button",
  component: UiButton,
  argTypes: {
    onClick: { action: "onClick" },
    type: {
      control: { type: "select" },
      options: ["primary", "secondary"],
    },
  },
}

const Template = (args) => ({
  components: { UiButton },
  setup() {
    return { args }
  },
  template: '<ui-button :type="args.type" :label="args.label" />',
})

export const Primary = Template.bind({})
Primary.args = {
  type: "primary",
  label: "Button",
}

export const Secondary = Template.bind({})
Secondary.args = {
  type: "secondary",
  label: "Button",
}
