import {toast} from 'vue3-toastify';

export class NotificationService {
    success(message: string) {
        toast.success(message);
    }

    error(message: string) {
        toast.error(message);
    }
}