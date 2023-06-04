export class Media {
    constructor(
        readonly fileName: string
    ) {
    }

    static of(fileName: string): Media {
        return new Media(fileName)
    }
}