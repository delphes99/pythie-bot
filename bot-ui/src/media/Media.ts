export class Media {
    fileName: string

    constructor(fileName: string) {
        this.fileName = fileName
    }

    static of(fileName: string): Media {
        return new Media(fileName)
    }
}