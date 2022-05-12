import UiButton from "./UiButton.vue"

export default {
  title: "Design System/Button",
  component: UiButton,
  argTypes: {
    onClick: { action: "onClick" },
    type: {
      control: { type: "select" },
      options: ["primary", "secondary", "warning"],
    },
  },
}

const Template = (args) => ({
  components: { UiButton },
  setup() {
    return { args }
  },
  template: '<ui-button :type="args.type" :label="args.label" :router-link="args.routerLink" :link="args.link" />',
})

export const Primary = Template.bind({})
Primary.args = {
  type: "primary",
  label: "Primary button",
}

export const Secondary = Template.bind({})
Secondary.args = {
  type: "secondary",
  label: "Secondary Button",
}

export const Warning = Template.bind({})
Warning.args = {
  type: "warning",
  label: "Warning",
}

export const Router_link = Template.bind({})
Router_link.args = {
  label: "Warning",
  routerLink: "router-link",
}

export const Link = Template.bind({})
Router_link.args = {
  label: "Warning",
  link: "router-link",
}
