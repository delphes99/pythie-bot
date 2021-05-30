import { ref, UnwrapRef } from "vue";

export function useLoadingPromise<T>(loadingResource: Promise<UnwrapRef<T>>) {
  const isLoading = ref(true);
  const errorLoading = ref(null);
  const data = ref<T | null>(null);

  loadingResource
    .then(load => {
      data.value = load;
      isLoading.value = false;
    })
    .catch(error => {
      errorLoading.value = error;
    });

  return {
    isLoading,
    errorLoading,
    data
  };
}
