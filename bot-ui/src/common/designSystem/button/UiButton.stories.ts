import UiButtonType from "@/common/designSystem/button/UiButtonType";
import {Meta, StoryObj} from "@storybook/vue3";
import UiButton from "./UiButton.vue"

const meta: Meta<typeof UiButton> = {
    title: "Design System/Button",
    component: UiButton,
    argTypes: {
        type: {
            options: Object.values(UiButtonType),
        }
    },
    args: {
        type: UiButtonType.Primary,
        label: "Some label",
        routerLink: undefined,
        link: undefined,
    }
}

export default meta
type Story = StoryObj<typeof UiButton>

export const Primary = {
    args: {
        type: "primary",
        label: "Primary button",
    }
}

export const Secondary = {
    args: {
        type: "secondary",
        label: "Secondary Button",
    }
}

export const Warning = {
    args: {
        type: "warning",
        label: "Warning",
    }
}

export const Router_link = {
    args: {
        label: "Router link",
        routerLink: "router-link",
    }
}

export const Link = {
    args: {
        label: "External link",
        link: "router-link",
    }
}
