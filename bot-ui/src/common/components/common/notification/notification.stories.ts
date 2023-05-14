import type {Meta, StoryObj} from '@storybook/vue3';
import {toast, ToastifyContainer, updateGlobalOptions} from "vue3-toastify";
import 'vue3-toastify/dist/index.css';

const meta: Meta<typeof ToastifyContainer> = {
    title: 'Design System/Notifications',
    component: ToastifyContainer,
};

export default meta;
type Story = StoryObj<typeof ToastifyContainer>;

export const Default: Story = {
    render: (args: any) => ({
        components: {ToastifyContainer},
        setup() {
            updateGlobalOptions({
                containerId: "toast-container",
                autoClose: false,
            })
            toast.success("Hello world");
            return {args};
        },
        template: '<ToastifyContainer id="toast-container" class="block"></ToastifyContainer>',
    }),
};