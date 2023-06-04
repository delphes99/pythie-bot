import {lorem_ipsum} from "../../../../.storybook/constant"
import UiCard from "./UiCard.vue"

export default {
    title: "Design System/Card",
    component: UiCard,
    argTypes: {},
}

const Template = (args) => ({
    components: {UiCard},
    setup() {
        return {args}
    },
    template: `
      <ui-card :title="args.title">
      {{ args.content }}
      </ui-card>`,
})

export const With_text = Template.bind({})
With_text.args = {
    title: "With text",
    content: lorem_ipsum,
}
