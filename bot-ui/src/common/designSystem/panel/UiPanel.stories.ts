import {Meta, StoryObj} from "@storybook/vue3";
import {lorem_ipsum} from "../../../../.storybook/constant"
import UiPanel from "./UiPanel.vue"
import {UiPanelMenuItem} from "./UiPanelMenuItem.js"

const meta: Meta<typeof UiPanel> = {
    component: UiPanel,
    title: "Design System/Panel",
    argTypes: {
        menuItems: {
            control: "array",
        },
    },
    render: (args: any) => ({
        components: {UiPanel},
        setup() {
            return {
                args,
                toMenu: (items: string[]) => {
                    return items?.map(item => new UiPanelMenuItem(item)) ?? null
                },
            }
        },
        template: `
          <UiPanel :title="args.title" :menu="toMenu(args.menuItems)">
            {{ args.content }}
          </UiPanel>`,
    })
}

export default meta
type Story = StoryObj<typeof UiPanel>;

export const Empty = {
    args: {
        title: "Without content",
    }
}

export const With_text = {
    args: {
        title: "With text",
        content: lorem_ipsum,
    }
}

export const With_menu = {
    args: {
        title: "With menu",
        content: lorem_ipsum,
        menuItems: [
            "Action 1",
            "Action 2",
            "Action 3",
        ],
    }
}