import type {Meta, StoryObj} from '@storybook/vue3';
import {toast, ToastifyContainer, updateGlobalOptions} from "vue3-toastify";
import 'vue3-toastify/dist/index.css';
import UiButton from '../button/UiButton.vue';

const meta: Meta<typeof ToastifyContainer> = {
    component: ToastifyContainer,
    title: 'Design System/Notifications',
    render: (args: any) => ({
        components: {ToastifyContainer, UiButton},
        setup() {
            updateGlobalOptions({
                containerId: "toast-container",
                autoClose: false,
            })

            return {
                args,
                toast,
            };
        },
        template: `
          <ToastifyContainer id="toast-container" class="block"></ToastifyContainer>
          <UiButton label="Show toast" @on-click="toast.success('Hello world')"></UiButton>
        `,
    }),
};

export default meta;
type Story = StoryObj<typeof ToastifyContainer>;

export const Default: Story = {};