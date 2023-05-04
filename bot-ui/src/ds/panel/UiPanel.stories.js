import {lorem_ipsum} from "../../../.storybook/constant"
import UiPanel from "./UiPanel.vue"
import {UiPanelMenuItem} from "./UiPanelMenuItem"

export default {
    title: "Design System/Panel",
    component: UiPanel,
    argTypes: {
        menuItems: {
            control: "array",
        },
    },
}

const Template = (args) => ({
    components: {UiPanel},
    setup() {
        return {
            args,
            toMenu: (items) => {
                return items?.map(item => new UiPanelMenuItem(item)) ?? null
            },
        }
    },
    template:
        `
          <ui-panel :title="args.title" :menu="toMenu(args.menuItems)">
          {{ args.content }}
          </ui-panel>`,
})

export const Empty = Template.bind({})
Empty.args = {
    title: "Without content",
}

export const With_text = Template.bind({})
With_text.args = {
    title: "With text",
    content: lorem_ipsum,
}

export const With_menu = Template.bind({})
With_menu.args = {
    title: "With menu",
    content: lorem_ipsum,
    menuItems: [
        "Action 1",
        "Action 2",
        "Action 3",
    ],
}
