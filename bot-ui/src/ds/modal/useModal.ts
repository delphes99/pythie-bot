import {ref} from "vue"

export function useModal(isOpen = false) {
    const isOpenRef = ref(isOpen)

    function open() {
        isOpenRef.value = true
    }

    function close() {
        isOpenRef.value = false
    }

    return {
        isOpen: isOpenRef,
        open,
        close,
    }
}