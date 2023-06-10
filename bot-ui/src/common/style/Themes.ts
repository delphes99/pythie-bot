export class Theme {
    private constructor(
        readonly name: string,
        readonly className: string,
        readonly monochrome: boolean
    ) {
    }

    monoChrome(): Theme {
        return new Theme(`${this.className}-monochrome`, this.name, true)
    }

    static of(name: string) {
        const defaultTheme = new Theme(name, name, false)
        return {
            default: defaultTheme,
            monoChrome: defaultTheme.monoChrome()
        }
    }
}

export const {default: darkTheme, monoChrome: darkMonochromeTheme} = Theme.of("dark")

export const {default: lightTheme, monoChrome: lightMonochromeTheme} = Theme.of("light")

export const themes = [
    darkTheme,
    darkMonochromeTheme,
    lightTheme,
    lightMonochromeTheme,
]