import UiButtonType from "@/common/designSystem/button/ui-button.type";
import {Meta, StoryObj} from "@storybook/vue3";
import UiButton from "./ui-button.vue"

const meta: Meta<typeof UiButton> = {
    title: "Design System/Button",
    component: UiButton,
    argTypes: {
        onClick: {action: "onClick"},
        type: {
            control: {type: "select"},
            options: Object.values(UiButtonType),
        },
        routerLink: {
            required: false,
        },
        link: {
            required: false,
        }
    },
    args: {
        type: UiButtonType.Primary,
        label: "Some label",
        routerLink: null,
        link: null,
    },
}

export default meta
type Story = StoryObj<typeof UiButton>;

export const Default: Story = {
    render: (args: any) => ({
        components: {UiButton},
        setup() {
            return {args};
        },
        template: '<ui-button :type="args.type" :label="args.label" :router-link="args.routerLink" :link="args.link" />',
    }),
};

export const Primary = {
    ...Default,
    args: {
        type: "primary",
        label: "Primary button",
    }
}

export const Secondary = {
    ...Default,
    args: {
        type: "secondary",
        label: "Secondary Button",
    }
}

export const Warning = {
    ...Default,
    args: {
        type: "warning",
        label: "Warning",
    }
}

export const Router_link = {
    ...Default,
    args: {
        label: "Router link",
        routerLink: "router-link",
    }
}

export const Link = {
    ...Default,
    args: {
        label: "External link",
        link: "router-link",
    }
}
