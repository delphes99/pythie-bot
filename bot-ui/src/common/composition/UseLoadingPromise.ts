import { ref, UnwrapRef, Ref } from "vue"

export interface LoadPromise<T> {
  isLoading: Ref<boolean>
  errorLoading: Ref
  data: Ref<UnwrapRef<T> | null>
}

export function useLoadingPromise<T>(
  loadingResource: Promise<UnwrapRef<T>>,
): LoadPromise<T> {
  const isLoading = ref(true)
  const errorLoading = ref(null)
  const data = ref<T | null>(null)

  loadingResource
    .then((load) => {
      data.value = load
      isLoading.value = false
    })
    .catch((error) => {
      errorLoading.value = error
    })

  return {
    isLoading,
    errorLoading,
    data,
  }
}
