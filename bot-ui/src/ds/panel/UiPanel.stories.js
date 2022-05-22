import { lorem_ipsum } from "../../../.storybook/constant"
import UiPanel from "./UiPanel.vue"

export default {
  title: "Design System/Panel",
  component: UiPanel,
  argTypes: {},
}

const Template = (args) => ({
  components: { UiPanel },
  setup() {
    return {
      args,
    }
  },
  template:
    `<ui-panel :title="args.title">
      {{ args.content }}
    </ui-panel>`,
})

export const Empty = Template.bind({})
Empty.args = {
  title: "Without content",
}

export const With_text = Template.bind({})
With_text.args = {
  title: "Without content",
  content: lorem_ipsum,
}
