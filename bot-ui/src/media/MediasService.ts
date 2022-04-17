import { Media } from "@/media/Media"

export default class MediasService {
  backendUrl: string

  constructor(backendUrl: string) {
    this.backendUrl = backendUrl
  }

  async list(): Promise<Media[]> {
    const response = await fetch(`${this.backendUrl}/medias/files`)

    return response.json().then((data) => data.map((json: any) => json.filename).map(Media.of))
  }
}
