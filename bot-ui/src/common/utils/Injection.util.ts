import {inject, InjectionKey} from "vue";

export function autowired<T>(key: InjectionKey<T>) {
    return inject(key) ?? throwError(`No value for '${key.toString()}' provided`);
}

function throwError(errorMessage: string): never {
    throw new Error(errorMessage);
}