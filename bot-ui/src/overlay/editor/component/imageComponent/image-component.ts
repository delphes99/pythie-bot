import ImageComponentProperties from "@/overlay/editor/component/imageComponent/image-component-properties.vue"
import ImageComponentRender from "@/overlay/editor/component/imageComponent/image-component-render.vue"
import {OverlayElementProperties, PropertiesComponent, RenderComponent} from "@/overlay/OverlayElementProperties"

export default class ImageComponent implements OverlayElementProperties {
    type = "Image"
    url: string
    width?: number
    height?: number

    constructor(url: string, width?: number, height?: number) {
        this.url = url
        this.width = width
        this.height = height
    }

    public get representation() {
        return this.url
    }

    public static fromObject(obj: {
        url: string
        width?: number
        height?: number
    }): ImageComponent {
        const {url, width, height} = obj
        return new ImageComponent(url, width, height)
    }

    renderComponent(): RenderComponent {
        return ImageComponentRender
    }

    propertiesComponent(): PropertiesComponent {
        return ImageComponentProperties
    }

    equals(other: OverlayElementProperties): boolean {
        return (
            other instanceof ImageComponent &&
            this.url === other.url &&
            this.width === other.width &&
            this.height === other.height
        )
    }
}