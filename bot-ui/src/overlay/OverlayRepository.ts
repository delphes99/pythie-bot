import {notEmpty} from "@/common/utils/Null.utils"
import Overlay from "@/overlay/Overlay"
import {fromJson} from "@/overlay/OverlayElement"
import axios from "axios"

export default class OverlayRepository {
    backendUrl: string

    constructor(backendUrl: string) {
        this.backendUrl = backendUrl
    }

    list(): Promise<Overlay[]> {
        return axios.get(`${this.backendUrl}/api/overlays`).then((response) => {
            const data = response.data as Overlay[]
            data.forEach((overlay) => {
                overlay.elements = overlay.elements
                    .map((element) => fromJson(element))
                    .filter(notEmpty)
            })
            return data
        })
    }

    async get(id: string): Promise<Overlay> {
        return await this.list().then((all) => all.find((o) => o.id === id) as Overlay)
    }

    async save(overlay: Overlay): Promise<boolean> {
        return await axios
            .post(`${this.backendUrl}/api/overlay`, overlay, {
                headers: {"Content-Type": "application/json"},
            })
            .then(() => true)
    }

    async deleteOverlay(overlay: Overlay): Promise<boolean> {
        return await axios
            .delete(`${this.backendUrl}/api/overlay/${overlay.id}`)
            .then(() => true)
    }
}
